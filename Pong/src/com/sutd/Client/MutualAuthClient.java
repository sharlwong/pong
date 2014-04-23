package com.sutd.Client;

import com.badlogic.gdx.net.Socket;
import com.sutd.Network.RSATools;
import com.sutd.Network.RSATools.AESHelper;

import java.lang.Exception;

/*
 * Mutual Authenticate with T3 protocol.
 */
public class MutualAuthClient {

	public static AESHelper authenticate(Socket socket, String password) {
		int code = 1;
		/*
		1 = everything
		2 = interlock
		3 = interlock + aes
		4 = digest-interlock + aes
		5 = signatures + aes
		6 = nothing
		 */
		return authenticate(socket,password,code);
	}

	/**
	 * Authenticate server
	 *
	 * @param socket The socket between the client and the server
	 * @return null if authentication failed, the AES key otherwise.
	 */
	public static AESHelper authenticate(Socket socket, String password, int code) {
		final int port = 3344;
		String clientNonce = RSATools.nonce();
		String serverNonce;
		String clientText = "Hello Server! Here the authentication from client";
		RSATools.AESHelper passwordAuthenticate = new RSATools.AESHelper(password);
		String temp0 = "", temp1, temp2;
		int temp;
		boolean verified;

		/*** assume connected to server ***/

		System.out.println("Exchanging keys...");
		/* send client public key */
		RSATools.RSADecryption decryption = new RSATools.RSADecryption();
		decryption.sendKey(socket);
		/* get server public key */
		RSATools.RSAEncryption encryption = new RSATools.RSAEncryption();
		encryption.getKey(socket);

		System.out.println("Ready!");
		/***********************************/
		/* READY FOR MUTUAL AUTHENTICATION */
		/***********************************/

		if (code < 5 && code >= 1) {
			System.out.println("Exchanging nonsense...");
			/* send client nonce */
			encryption.sendMessage(socket, clientNonce);
			/* get server nonce */
			serverNonce = decryption.getMessage(socket);

			System.out.println("Making stuff...");
			/* encrypt server nonce ++ password */
			String encryptedTemp = passwordAuthenticate.encrypt(serverNonce + password);
			/* split into halves */
			temp = encryptedTemp.length() / 2;
			String encrypted1 = encryptedTemp.substring(0, temp);
			String encrypted2 = encryptedTemp.substring(temp);

			String part0 = RSATools.digest(encryptedTemp);
			String part1 = encrypted1;
			String part2 = encrypted2;

			System.out.println("Interlock start...");
			if (code < 3 && code > 1) {
				/* send part 1 */
				encryption.sendMessage(socket, part1);
				/* get part 1 */
				temp1 = decryption.getMessage(socket);
				/* send part 2 */
				encryption.sendMessage(socket, part2);
				/* get part 2 */
				temp2 = decryption.getMessage(socket);
			}
			else {
				/* send part 0 */
				encryption.sendMessage(socket, part0);
				/* get part 0 */
				temp0 = decryption.getMessage(socket);
				/* send part 1,2 */
				encryption.sendMessage(socket, part1);
				encryption.sendMessage(socket, part2);
				/* get part 1,2 */
				temp1 = decryption.getMessage(socket);
				temp2 = decryption.getMessage(socket);
			}

			try {
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
				if (code >= 4 || code == 1) verified = verified && (temp0.equals(RSATools.digest(temp1 + temp2)));
				if (!verified) {
					System.out.println("Verification failed, exiting...");
					return null;
				}
			} catch (Exception e) {
				System.out.println("Verification failed, returning...");
				return null;
			}

			System.out.println("Verified!");
			/***********************************/
		/* DONE WITH MUTUAL AUTHENTICATION */
			/***********************************/}

		RSATools.AESHelper aesHelper = null;
		if (code > 2 && code < 5) {
			System.out.println("Exchanging AES key parts...");
			/* make key part 2 */
			temp2 = RSATools.nonce();
			/* get part 1 */
			temp1 = decryption.getMessage(socket);
			temp1 = passwordAuthenticate.decrypt(temp1);
			/* send part 2 */
			encryption.sendMessage(socket, passwordAuthenticate.encrypt(temp2));
			/* combine */
			aesHelper = new RSATools.AESHelper(temp1 + temp2);
		}
		else if (code == 5 || code == 1) {
			RSATools.RSASign rsaSign = new RSATools.RSASign(decryption);
			RSATools.RSAVerify rsaVerify = new RSATools.RSAVerify(encryption);

			System.out.println("Generating randomness...");
			temp2 = RSATools.nonce();

			System.out.println("Exchanging bits...");
			temp1 = decryption.getMessage(socket);
			encryption.sendMessage(socket, temp2);

			System.out.println("Exchanging signatures...");
			verified = rsaVerify.getVerification(socket, temp1);
			rsaSign.sendMessage(socket, temp2);

			if (!verified) {
				System.out.println("Verification failed, exiting...");
				return null;
			}

			aesHelper = new RSATools.AESHelper(temp1 + temp2);
		}
		else {
			aesHelper = new RSATools.AESHelper("");
		}

		System.out.println("AES key now shared!\n");
		/*************************/
		/* CREATED SYMMETRIC KEY */
		/*************************/

		return aesHelper;
	}
}
