package multipong;

import java.net.InetAddress;
import java.net.Socket;

public class ClientSendObject {

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
		RSATools.RSAPrivate rsaPrivate = new RSATools.RSAPrivate();
		rsaPrivate.sendKey(socket);
		RSATools.RSAPublic rsaPublic = new RSATools.RSAPublic();
		rsaPublic.getKey(socket);

		System.out.println("Generating randomness...");
		temp2 = RSATools.nonce();

		System.out.println("Exchanging bits...");
		temp1 = rsaPrivate.getMessage(socket);
		;
		rsaPublic.sendMessage(socket, temp2);

		System.out.println("Exchanging signatures...");
		verified = rsaPublic.getVerification(socket, temp1);
		rsaPrivate.sendSignature(socket, temp2);

		System.out.println("Verified: " + verified);

		System.out.println("AES key now shared!\n");
		RSATools.AESHelper aesHelper = new RSATools.AESHelper(temp1 + temp2);

		/*************************/
		/* CREATED SYMMETRIC KEY */
		/*************************/

		double[][] a = new double[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				a[i][j] = ((double) i + 1) / (j + 7);
			}
		}

		/* listen */
		String s = aesHelper.getMessage(socket);
		System.out.println("Server: " + s);

		aesHelper.sendObject(socket, a);

		socket.close();
	}
}
