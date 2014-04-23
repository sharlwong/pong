package archived.security_lab.mutualAuth;

import java.net.InetAddress;
import java.net.Socket;

public class TestClient5 {

	public static void main(String[] args) throws Exception {
		final int port = 3344;
		Socket socket;
		String temp1, temp2;
		boolean verified;

		System.out.println("Client start!");

		/* connect to server */
		socket = new Socket(InetAddress.getLocalHost(), port);

		/* part 0 */
		RSATools.RSAPrivate decryption = new RSATools.RSAPrivate();
		decryption.sendKey(socket);
		RSATools.RSAPublic encryption = new RSATools.RSAPublic();
		encryption.getKey(socket);

		System.out.println("Generating randomness...");
		temp2 = RSATools.nonce();

		System.out.println("Exchanging bits...");
		temp1 = decryption.getMessage(socket);
		encryption.sendMessage(socket, temp2);

		System.out.println("Exchanging signatures...");
		verified = encryption.getVerification(socket, temp1);
		decryption.sendSignature(socket, temp2);

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
