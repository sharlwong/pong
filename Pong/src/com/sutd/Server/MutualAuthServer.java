package com.sutd.Server;

import com.badlogic.gdx.net.Socket;
import com.sutd.Network.RSATools;

import java.lang.Exception;

public class MutualAuthServer {

	public static RSATools.AESHelper authenticate(Socket socket, String password) throws Exception {
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

	public static RSATools.AESHelper authenticate(Socket socket, String password, int code) throws Exception {
		final int port = 3344;
		String serverNonce = RSATools.nonce();
		String clientNonce;
		String serverText = "Hello Client! Here the authentication from server";
		RSATools.AESHelper passwordAuthenticate = new RSATools.AESHelper(password);
		String temp0 = "", temp1, temp2;
		int temp;
		boolean verified;

		/*** assume connected to client ***/

		System.out.println("Exchanging keys...");
		/* get client public key */
		RSATools.RSAEncryption encryption = new RSATools.RSAEncryption();
		encryption.getKey(socket);
		/* send server public key */
		RSATools.RSADecryption decryption = new RSATools.RSADecryption();
		decryption.sendKey(socket);

		System.out.println("Ready!");
		/***********************************/
		/* READY FOR MUTUAL AUTHENTICATION */
		/***********************************/

		if (code < 5 && code >= 1) {
			System.out.println("Exchanging nonsense...");
			/* get client nonce */
			clientNonce = decryption.getMessage(socket);
			/* send server nonce */
			encryption.sendMessage(socket, serverNonce);

			System.out.println("Making stuff...");
			/* encrypt client nonce ++ password */
			String encryptedTemp = passwordAuthenticate.encrypt(clientNonce + password);
			/* split into halves */
			temp = encryptedTemp.length() / 2;
			String encrypted1 = encryptedTemp.substring(0, temp);
			String encrypted2 = encryptedTemp.substring(temp);

			String part0 = RSATools.digest(encryptedTemp);
			String part1 = encrypted1;
			String part2 = encrypted2;

			System.out.println("Interlock start...");
			if (code < 3 && code > 1) {
				/* get part 1 */
				temp1 = decryption.getMessage(socket);
				/* send part 1 */
				encryption.sendMessage(socket, part1);
				/* get part 2 */
				temp2 = decryption.getMessage(socket);
				/* send part 2 */
				encryption.sendMessage(socket, part2);
			}
			else {
				/* get part 0 */
				temp0 = decryption.getMessage(socket);
				/* send part 0 */
				encryption.sendMessage(socket, part0);
				/* get part 1,2 */
				temp1 = decryption.getMessage(socket);
				temp2 = decryption.getMessage(socket);
				/* send part 1,2 */
				encryption.sendMessage(socket, part1);
				encryption.sendMessage(socket, part2);
			}

			try {
				/* decode */
				String received = passwordAuthenticate.decrypt(temp1 + temp2);
				if (received == null) {
					System.out.println("Verification failed, exiting...");
					encryption.sendMessage(socket, passwordAuthenticate.encrypt(""));
					return null;
				}
				/* verification */
				temp = serverNonce.length();
				verified = received.substring(0, temp).equals(serverNonce);
				verified = verified && received.substring(temp).equals(password);
				if (code >= 4 || code == 1) verified = verified && (temp0.equals(RSATools.digest(temp1 + temp2)));
				if (!verified) {
					System.out.println("Verification failed, exiting...");
					encryption.sendMessage(socket, "");
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
			/* make key part 1 */
			temp1 = RSATools.nonce();
			/* send part 1 */
			encryption.sendMessage(socket, passwordAuthenticate.encrypt(temp1));
			/* get part 2 */
			temp2 = passwordAuthenticate.decrypt(decryption.getMessage(socket));
			/* combine */
			aesHelper = new RSATools.AESHelper(temp1 + temp2);
		}
		else if (code == 5 || code == 1) {
			RSATools.RSASign rsaSign = new RSATools.RSASign(decryption);
			RSATools.RSAVerify rsaVerify = new RSATools.RSAVerify(encryption);

			System.out.println("Generating randomness...");
			temp1 = RSATools.nonce();

			System.out.println("Exchanging bits...");
			encryption.sendMessage(socket, temp1);
			temp2 = decryption.getMessage(socket);

			System.out.println("Exchanging signatures...");
			rsaSign.sendMessage(socket, temp1);
			verified = rsaVerify.getVerification(socket, temp2);

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
