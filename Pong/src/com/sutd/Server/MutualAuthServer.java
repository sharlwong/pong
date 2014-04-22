package com.sutd.Server;

import com.badlogic.gdx.net.Socket;
import com.sutd.Network.RSATools;

public class MutualAuthServer {

	public static RSATools.AESHelper authenticate(Socket socket, String password) throws Exception {
		final int port = 3344;
		String serverNonce = RSATools.nonce();
		String clientNonce;
		String serverText = "Hello Client! Here the authentication from server";
		RSATools.AESHelper passwordAuthenticate = new RSATools.AESHelper(password);
		String temp1, temp2;
		int temp;
		boolean verified;

		System.out.println("Exchanging keys...");
		/* get client public key */
		RSATools.RSAEncryption encryption = new RSATools.RSAEncryption();
		encryption.getKey(socket);

		/* send server public key */
		RSATools.RSADecryption decryption = new RSATools.RSADecryption();
		decryption.sendKey(socket);

		System.out.println("Exchanging nonsense...");
		/* get client nonce */
		clientNonce = decryption.getMessage(socket);

		/* send server nonce */
		encryption.sendMessage(socket, serverNonce);

		/* encrypt client nonce ++ password */
		String encryptedTemp = passwordAuthenticate.encrypt(clientNonce + password);

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
		String received = passwordAuthenticate.decrypt(temp1 + temp2);
		if (received == null) { 
			encryption.sendMessage(socket, passwordAuthenticate.encrypt(""));
			return null;
		}


		/* verification */
		temp = serverNonce.length();
		verified = received.substring(0, temp).equals(serverNonce);
		verified = verified && received.substring(temp).equals(password);
		if (!verified) {
			System.out.println("Verification failed, returning...");
			encryption.sendMessage(socket, "");
			return null;
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
		encryption.sendMessage(socket, passwordAuthenticate.encrypt(temp1));

		/* get part  */
		temp2 = passwordAuthenticate.decrypt(decryption.getMessage(socket));

		/* combine */
		RSATools.AESHelper aesHelper = new RSATools.AESHelper(temp1 + temp2);

		System.out.println("AES key now shared!\n");
		System.out.println("AES KEY:" + temp1 + temp2);

		/*************************/
		/* CREATED SYMMETRIC KEY */
		/*************************/

		return aesHelper;
	}
}
