package multipong;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerReceiveObject {

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
		RSATools.RSAPublic rsaPublic = new RSATools.RSAPublic();
		rsaPublic.getKey(socket);
		RSATools.RSAPrivate rsaPrivate = new RSATools.RSAPrivate();
		rsaPrivate.sendKey(socket);

		System.out.println("Generating randomness...");
		temp1 = RSATools.nonce();

		System.out.println("Exchanging bits...");
		rsaPublic.sendMessage(socket, temp1);
		temp2 = rsaPrivate.getMessage(socket);

		System.out.println("Making signature...");
		rsaPrivate.sendSignature(socket, temp1);
		verified = rsaPublic.getVerification(socket,temp2);

		System.out.println("Verified: " + verified);

		System.out.println("AES key now shared!\n");
		RSATools.AESHelper aesHelper = new RSATools.AESHelper(temp1 + temp2);
		System.out.println(RSATools.base64(aesHelper.keySpec.getEncoded()));

		/*************************/
		/* CREATED SYMMETRIC KEY */
		/*************************/

		/* speak */
		aesHelper.sendMessage(socket, serverText);

		Object o = aesHelper.getObject(socket);
		double[][] a = (double[][]) o;

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				System.out.print(a[i][j] + " ");
			}
			System.out.print("\n");
		}

		socket.close();
		server.close();
	}
}
