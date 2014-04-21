package com.sutd.Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.badlogic.gdx.net.Socket;

/**
 * THIS IS AN OLD VERSION
 */
public class RSATools {
	public static String base64(byte[] bytes) {
		BASE64Encoder base64 = new BASE64Encoder();
		return base64.encode(bytes);
	}

	public static byte[] base64(String string) {
		try {
			return new BASE64Decoder().decodeBuffer(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String digest(String message) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(message.getBytes());
		byte[] digest = md.digest();
		return base64(digest);
	}

	public static String nonce() {
		return nonce(128);
	}

	public static String nonce(int length) {
		Random random = new SecureRandom();
		byte[] bytes = new byte[length * 2];
		random.nextBytes(bytes);
		String temp = base64(bytes);
		if (temp.length() > length) temp = temp.substring(0, length);
		return temp;
	}

	public static class AESHelper {
		SecretKeySpec keySpec;
		private Object unencryptedObject;

		public AESHelper(String keyString) {
			byte[] key = keyString.getBytes();
			MessageDigest messageDigest = null;
			try {
				messageDigest = MessageDigest.getInstance("SHA-1");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			key = messageDigest.digest(key);
			key = Arrays.copyOf(key, 16);
			keySpec = new SecretKeySpec(key, "AES");
		}

		public String decrypt(String encrypted) {
			try {
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, keySpec);
				byte[] original = cipher.doFinal(base64(encrypted));
				return new String(original);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			}
			return null;
		}

		public String encrypt(String message) {
			try {
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, keySpec);
				return base64(cipher.doFinal((message).getBytes())).replace("\r","").replace("\n","");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			}
			return null;
		}

		public String getMessage(Socket socket) {
			Object obj = null;
			try {
				ObjectInputStream obIn = new ObjectInputStream(socket.getInputStream());
				obj = obIn.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return decrypt((String) obj);
		}


		public void sendMessage(Socket socket, String message) {
			try {
				ObjectOutputStream obOut = new ObjectOutputStream(socket.getOutputStream());
				obOut.writeObject(encrypt(message));
				obOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Created by avery_000 on 07-Apr-14.
	 */
	public static class RSADecryption {
		private final int RSAKeySize = 2048;
		private KeyPair keyPair;

		public RSADecryption() {
			KeyPairGenerator RSAKeyGen = null;
			try {
				RSAKeyGen = KeyPairGenerator.getInstance("RSA");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			SecureRandom random = new SecureRandom();
			RSAKeyGen.initialize(RSAKeySize, random);
			keyPair = RSAKeyGen.generateKeyPair();
		}

		public String decrypt(String encryptedBase64) {
			try {
				byte[] decode64 = base64(encryptedBase64);
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
				cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
				byte[] newPlainText = cipher.doFinal(decode64);
				return new String(newPlainText, Charset.defaultCharset());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			}
			return null;
		}

		public String getMessage(Socket socket) {
			Object obj = null;
			try {
				ObjectInputStream obIn = new ObjectInputStream(socket.getInputStream());
				obj = obIn.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return decrypt((String) obj);
		}

		protected PrivateKey getPrivateKey() {
			return keyPair.getPrivate();
		}

		public byte[] getPublicKeyEncoded() {
			return keyPair.getPublic().getEncoded();
		}

		public void sendKey(Socket socket) {
			byte[] encodedKey = keyPair.getPublic().getEncoded();
			ByteBuffer bb = ByteBuffer.allocate(4);
			bb.putInt(encodedKey.length);
			try {
				socket.getOutputStream().write(bb.array());
				socket.getOutputStream().write(encodedKey);
				socket.getOutputStream().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Created by avery_000 on 07-Apr-14.
	 */
	public static class RSAEncryption {
		private Key publicKey;

		public String encrypt(String message) {
			byte[] cipherText = new byte[0];
			try {
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);
				cipherText = cipher.doFinal(message.getBytes(Charset.defaultCharset()));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			}
			return base64(cipherText);
		}

		public void getKey(Socket socket) {
			try {
				byte[] lenb = new byte[4];
				socket.getInputStream().read(lenb, 0, 4);
				ByteBuffer bb = ByteBuffer.wrap(lenb);
				int len = bb.getInt();
				byte[] cPubKeyBytes = new byte[len];
				socket.getInputStream().read(cPubKeyBytes);
				X509EncodedKeySpec ks = new X509EncodedKeySpec(cPubKeyBytes);
				KeyFactory kf = KeyFactory.getInstance("RSA");
				publicKey = kf.generatePublic(ks);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}

		public Key getPublicKey() {
			return publicKey;
		}

		public void setPublicKey(byte[] encodedPublicKey) {
			try {
				X509EncodedKeySpec ks = new X509EncodedKeySpec(encodedPublicKey);
				KeyFactory kf = KeyFactory.getInstance("RSA");
				publicKey = kf.generatePublic(ks);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}

		public void sendMessage(Socket socket, String message) {

			try {
				ObjectOutputStream obOut = new ObjectOutputStream(socket.getOutputStream());
				obOut.writeObject(encrypt(message));
				obOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Created by avery_000 on 08-Apr-14.
	 */
	public static class RSASign {
		PrivateKey privateKey;

		public RSASign(RSADecryption rsaDecryption) {
			this.privateKey = rsaDecryption.getPrivateKey();
		}

		public String sign(String message) {
			try {
				/* Create a Signature object and initialize it with the private key */
				Signature dsa = Signature.getInstance("SHA1withRSA");
				dsa.initSign(privateKey);

	            /* Update and sign the data */
				byte[] buffer = message.getBytes();
				dsa.update(buffer, 0, buffer.length);

	            /* Now that the data to be signed has been processed, generate a signature for it */
				byte[] generatedSignature = dsa.sign();

				/* done now return */
				return base64(generatedSignature);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (SignatureException e) {
				e.printStackTrace();
			}
			return null;
		}

		public void sendMessage(Socket socket, String message) {
			try {
				ObjectOutputStream obOut = new ObjectOutputStream(socket.getOutputStream());
				obOut.writeObject(sign(message));
				obOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Created by avery_000 on 08-Apr-14.
	 */
	public static class RSAVerify {
		PublicKey publicKey;

		public RSAVerify(RSAEncryption rsaEncryption) {
			try {
				byte[] receivedEncodedKey = rsaEncryption.getPublicKey().getEncoded();

			/* rebuild key */
				X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(receivedEncodedKey);
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				publicKey = keyFactory.generatePublic(pubKeySpec);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}

		public boolean verify(String originalData, String signature) {
			try {
			/* create a Signature object and initialize it with the public key */
				Signature sig = Signature.getInstance("SHA1withRSA");
				sig.initVerify(publicKey);

	            /* verify data */
				byte[] buffer = originalData.getBytes();
				sig.update(buffer, 0, buffer.length);
				boolean verifies = sig.verify(base64(signature));

				/* done */
				return verifies;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (SignatureException e) {
				e.printStackTrace();
			}
			return false;
		}

		public boolean getVerification(Socket socket, String originalData) {
			Object obj = null;
			try {
				ObjectInputStream obIn = new ObjectInputStream(socket.getInputStream());
				obj = obIn.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return verify(originalData, (String) obj);
		}

		public static void main(String[] args) {

			/* data */
			String originalData = "Something to be signed and sent and verified.";

			try {

				RSADecryption rsaDecryption = new RSADecryption();
				RSAEncryption rsaEncryption = new RSAEncryption();
				rsaEncryption.setPublicKey(rsaDecryption.getPublicKeyEncoded());

				RSASign rsaSign = new RSASign(rsaDecryption);
				RSAVerify rsaVerify = new RSAVerify(rsaEncryption);

				String sig = rsaSign.sign(originalData);

				boolean v = rsaVerify.verify(originalData, sig);

				System.out.println(sig);
				System.out.println(v);
			} catch (Exception e) {
				System.err.println("Caught exception " + e.toString());
			}
		}
	}
}
