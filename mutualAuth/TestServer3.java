package archived.security_lab.mutualAuth;

import java.net.ServerSocket;
import java.net.Socket;

public class TestServer3 {

	public static void main(String[] args) throws Exception {
		String password = "1234567";
		final int port = 3344;
		ServerSocket server;
		Socket socket;
		String serverNonce = RSATools.nonce();
		String clientNonce;
		String serverText = "Hello Client! Here the authentication from server";
		RSATools.AESHelper passwordAuthenticate = new RSATools.AESHelper(password);
		String temp1, temp2;
		int temp;
		boolean verified;

		/* connect to client */
		server = new ServerSocket(port);
		System.out.println("Server is waiting for client on port " + server.getLocalPort());
		socket = server.accept();

		System.out.println("Exchanging keys...");
		/* get client public key */
		RSATools.RSAPublic encryption = new RSATools.RSAPublic();
		encryption.getKey(socket);

		/* send server public key */
		RSATools.RSAPrivate decryption = new RSATools.RSAPrivate();
		decryption.sendKey(socket);

		System.out.println("Exchanging nonsense...");
		/* get client nonce */
		clientNonce = decryption.getMessage(socket);

		/* send server nonce */
		encryption.sendMessage(socket, serverNonce);

		/* encrypt client nonce ++ password */
		String encryptedTemp = passwordAuthenticate.encryptString(clientNonce + password);

		System.out.println("Interlock start...");
		/* split into halves */
		temp = encryptedTemp.length() / 2;
		String encrypted1 = encryptedTemp.substring(0, temp);
		String encrypted2 = encryptedTemp.substring(temp);

		/* get part 1 */
		temp1 = decryption.getMessage(socket);

		/* send part 1 */
		encryption.sendMessage(socket, encrypted1);

		/* get part 2 */
		temp2 = decryption.getMessage(socket);

		/* decode */
		String received = passwordAuthenticate.decryptString(temp1 + temp2);


		/* verification */
		temp = serverNonce.length();
		try {
			verified = received.substring(0, temp).equals(serverNonce);
			verified = verified && received.substring(temp).equals(password);
			if (!verified) {
				System.out.println("Verification failed, exiting...");
				System.exit(1);
			}
		} catch (Exception e) {
			System.out.println("Verification failed, exiting...");
			System.exit(1);
		}

		/* send part 2 */
		encryption.sendMessage(socket, encrypted2);

		System.out.println("Verified!");

		/***********************************/
		/* DONE WITH MUTUAL AUTHENTICATION */
		/***********************************/

		System.out.println("Exchanging AES key parts...");
		/* make key part 1 */
		temp1 = RSATools.nonce();

		/* send part 1 */
		encryption.sendMessage(socket, passwordAuthenticate.encryptString(temp1));

		/* get part  */
		temp2 = passwordAuthenticate.decryptString(decryption.getMessage(socket));

		/* combine */
		RSATools.AESHelper aesHelper = new RSATools.AESHelper(temp1 + temp2);

		System.out.println("AES key now shared!\n");

		/*************************/
		/* CREATED SYMMETRIC KEY */
		/*************************/

		/* speak */
		aesHelper.sendMessage(socket, serverText);

		socket.close();
		server.close();
	}
}
