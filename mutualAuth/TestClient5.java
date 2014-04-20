package archived.security_lab.mutualAuth;

import java.net.InetAddress;
import java.net.Socket;

public class TestClient5 {

	public static void main(String[] args) throws Exception {
		String password = "1234567";
		final int port = 3344;
		Socket socket;
		String clientNonce = RSATools.nonce();
		String serverNonce;
		RSATools.AESHelper passwordAuthenticate = new RSATools.AESHelper(password);
		String temp1, temp2;
		int temp;
		boolean verified;

		System.out.println("Client start!");

		/* connect to server */
		socket = new Socket(InetAddress.getLocalHost(), port);

		/* part 0 */
		RSATools.RSADecryption decryption = new RSATools.RSADecryption();
		decryption.sendKey(socket);
		RSATools.RSAEncryption encryption = new RSATools.RSAEncryption();
		encryption.getKey(socket);

		System.out.println("Generating randomness...");
		temp2 = RSATools.nonce();

		System.out.println("Exchanging bits...");
		temp1 = decryption.getMessage(socket);;
		encryption.sendMessage(socket,temp2);

		System.out.println("Exchanging signatures...");
		RSATools.RSASign rsaSign = new RSATools.RSASign(decryption);
		RSATools.RSAVerify rsaVerify = new RSATools.RSAVerify(encryption);
		verified = rsaVerify.getVerification(socket,temp1);
		rsaSign.sendMessage(socket,temp2);

		System.out.println("Verified: " + verified);

		System.out.println("AES key now shared!\n");
		RSATools.AESHelper aesHelper = new RSATools.AESHelper(temp1 + temp2);

		/*************************/
		/* CREATED SYMMETRIC KEY */
		/*************************/

		/* listen */
		String s = aesHelper.getMessage(socket);
		System.out.println("Server: " + s);

		socket.close();
	}
}
