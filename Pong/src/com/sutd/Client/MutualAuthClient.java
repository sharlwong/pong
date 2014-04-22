package com.sutd.Client;

import com.badlogic.gdx.net.Socket;
import com.sutd.Network.RSATools;
import com.sutd.Network.RSATools.AESHelper;

/*
 * Mutual Authenticate with T3 protocol.
 */
public class MutualAuthClient {

	/**
	 * Authenticate server
	 * @param socket The socket between the client and the server
	 * @return null if authentication failed, the AES key otherwise.
	 */
	public static AESHelper authenticate(Socket socket, String password) {
		final int port = 3344;
		String clientNonce = RSATools.nonce();
		String serverNonce;
		RSATools.AESHelper passwordAuthenticate = new RSATools.AESHelper(password);
		String temp1, temp2;
		int temp;
		boolean verified;

		System.out.println("Searching for connection...");
		/* connect to server */

		System.out.println("Exchanging keys...");
		/* send client public key */
		RSATools.RSADecryption decryption = new RSATools.RSADecryption();
		decryption.sendKey(socket);

		/* get server public key */
		RSATools.RSAEncryption encryption = new RSATools.RSAEncryption();
		encryption.getKey(socket);

		System.out.println("Exchanging nonsense...");
		/* send client nonce */
		encryption.sendMessage(socket, clientNonce);

		/* get server nonce */
		serverNonce = decryption.getMessage(socket);

		/* encrypt server nonce ++ password */
		String encryptedTemp = passwordAuthenticate.encrypt(serverNonce + password);

		System.out.println("Interlock start...");
		/* split into halves */
		temp = encryptedTemp.length() / 2;
		String encrypted1 = encryptedTemp.substring(0, temp);
		String encrypted2 = encryptedTemp.substring(temp);

		/* send part 1 */
		encryption.sendMessage(socket, encrypted1);

		/* get part 1 */
		temp1 = decryption.getMessage(socket);

		/* send part 2 */
		encryption.sendMessage(socket, encrypted2);

		/* get part 2 */
		temp2 = decryption.getMessage(socket);

		/* decode */
		String received = passwordAuthenticate.decrypt(temp1 + temp2);
		if (received == null) {
			System.out.println("Verification failed, exiting...");
			return null;
		}

		/* verification */
		temp = clientNonce.length();
		verified = received.substring(0, temp).equals(clientNonce);
		verified = verified && received.substring(temp).equals(password);
		if (!verified) {
			System.out.println("Verification failed, exiting...");
			return null;
		}

		System.out.println("Verified!");

		/***********************************/
		/* DONE WITH MUTUAL AUTHENTICATION */
		/***********************************/

		System.out.println("Exchanging AES key parts...");
		/* make key part 2 */
		temp2 = RSATools.nonce();

		/* get part 1 */
		temp1 = decryption.getMessage(socket);
		temp1 = passwordAuthenticate.decrypt(temp1);

		/* send part 2 */
		encryption.sendMessage(socket, passwordAuthenticate.encrypt(temp2));
		System.out.println("AES KEY:" + temp1 + temp2);
		System.out.println("AES key now shared!\n");
		
		//Return the aes key
		/* combine */
		RSATools.AESHelper aesHelper = new RSATools.AESHelper(temp1 + temp2);
		return aesHelper;

		/*************************/
		/* CREATED SYMMETRIC KEY */
		/*************************/

		/* listen */
	//	String s = aesHelper.getMessage(socket);
	}
}
