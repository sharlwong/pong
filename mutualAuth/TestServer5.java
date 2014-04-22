package archived.security_lab.mutualAuth;

import java.net.ServerSocket;
import java.net.Socket;

public class TestServer5 {

	public static void main(String[] args) throws Exception {
		final int port = 3344;
		ServerSocket server;
		Socket socket;
		String serverText = "Hello Client! Here the authentication from server";
		String temp1, temp2;
		boolean verified;

		System.out.println("Server start!");

		/* connect to client */
		server = new ServerSocket(port);
		socket = server.accept();

		/* part 0 */
		RSATools.RSAPublic encryption = new RSATools.RSAPublic();
		encryption.getKey(socket);
		RSATools.RSAPrivate decryption = new RSATools.RSAPrivate();
		decryption.sendKey(socket);

		System.out.println("Generating randomness...");
		temp1 = RSATools.nonce();

		System.out.println("Exchanging bits...");
		encryption.sendMessage(socket, temp1);
		temp2 = decryption.getMessage(socket);

		System.out.println("Making signature...");
		decryption.sendSignature(socket, temp1);
		verified = encryption.getVerification(socket, temp2);

		System.out.println("Verified: " + verified);

		System.out.println("AES key now shared!\n");
		RSATools.AESHelper aesHelper = new RSATools.AESHelper(temp1 + temp2);

		/*************************/
		/* CREATED SYMMETRIC KEY */
		/*************************/

		/* speak */
		aesHelper.sendMessage(socket, serverText);

		socket.close();
		server.close();
	}
}
