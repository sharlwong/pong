package multipong;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tools for crypto
 */
public abstract class RSATools {
	public static String base64(byte[] bytes) {
		BASE64Encoder base64 = new BASE64Encoder();
		return base64.encode(bytes);
	}

	public static byte[] base64(String string) {
		try {
			return new BASE64Decoder().decodeBuffer(string.replaceAll("[^a-zA-Z0-9+/=]", ""));
		} catch (Exception e) { }
		return new byte[0];
	}

	public static byte[] decrypt(String instance, Key key, byte[] encrypted) {
		try {
			Cipher cipher = Cipher.getInstance(instance);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(encrypted);
		} catch (Exception e) { }
		return new byte[0];
	}

	public static Object deserialize(byte[] yourBytes) {
		ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
		ObjectInput in = null;
		Object o = null;
		try {
			in = new ObjectInputStream(bis);
			o = in.readObject();
		} catch (Exception e) {
		} finally {
			try {
				bis.close();
			} catch (Exception ex) { }
			try {
				if (in != null) in.close();
			} catch (Exception ex) { }
		}
		return o;
	}

	public static String digest(String message) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (Exception e) { }
		assert md != null;
		md.update(message.getBytes());
		byte[] digest = md.digest();
		return base64(digest);
	}

	public static byte[] encrypt(String instance, Key key, byte[] plainText) {
		try {
			Cipher cipher = Cipher.getInstance(instance);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(plainText);
		} catch (Exception e) { }
		return new byte[0];
	}

	public static Object getUnencryptedObject(Socket socket) {
		Object obj = null;
		try {
			ObjectInputStream obIn = new ObjectInputStream(socket.getInputStream());
			obj = obIn.readObject();
		} catch (Exception e) { }
		return obj;
	}

	public static String nonce(int length) {
		Random random = new SecureRandom();
		byte[] bytes = new byte[length * 2];
		random.nextBytes(bytes);
		String temp = base64(bytes);
		if (temp.length() > length) temp = temp.substring(0, length);
		return temp;
	}

	public static String nonce() {
		return nonce(128);
	}

	public static void sendUnencryptedObject(Socket socket, Object message) {
		try {
			ObjectOutputStream obOut = new ObjectOutputStream(socket.getOutputStream());
			obOut.writeObject(message);
			obOut.flush();
		} catch (Exception e) { }
	}

	public static byte[] serialize(Object yourObject) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(yourObject);
		} catch (Exception e) {
		} finally {
			try {
				if (out != null) out.close();
			} catch (Exception ex) { }
			try {
				bos.close();
			} catch (Exception ex) { }
		}
		return bos.toByteArray();
	}

	public static class AESHelper {
		SecretKeySpec keySpec;

		public AESHelper(String keyString) {
			byte[] key = base64(digest(keyString));
			key = Arrays.copyOf(key, 16);
			keySpec = new SecretKeySpec(key, "AES");
		}

		public Object decryptObject(String encrypted) {
			byte[] decrypted = decrypt("AES/ECB/PKCS5Padding", keySpec, base64(encrypted));
			return deserialize(decrypted);
		}

		public String decryptString(String encrypted) {
			return new String(decrypt("AES/ECB/PKCS5Padding", keySpec, base64(encrypted)));
		}

		public String encryptObject(Object message) {
			byte[] bytes = serialize(message);
			return base64(encrypt("AES/ECB/PKCS5Padding", keySpec, bytes));
		}

		public String encryptString(String message) {
			return base64(encrypt("AES/ECB/PKCS5Padding", keySpec, message.getBytes()));
		}

		public String getMessage(Socket socket) {
			Object obj = getUnencryptedObject(socket);
			return decryptString((String) obj);
		}

		public Object getObject(Socket socket) {
			Object obj = getUnencryptedObject(socket);
			return decryptObject((String) obj);
		}

		public void sendMessage(Socket socket, String message) {
			sendUnencryptedObject(socket, encryptString(message));
		}

		public void sendObject(Socket socket, Object object) {
			sendUnencryptedObject(socket, encryptObject(object));
		}
	}

	/**
	 * a convenient exception class
	 * inherits runtime-exception
	 * so no declarations are necessary
	 */
	public static class RSAPrivate {
		private final int RSAKeySize = 2048;
		private KeyPair keyPair;

		public RSAPrivate() {
			KeyPairGenerator RSAKeyGen = null;
			try {
				RSAKeyGen = KeyPairGenerator.getInstance("RSA");
			} catch (Exception ignored) { }
			SecureRandom random = new SecureRandom();
			assert RSAKeyGen != null;
			RSAKeyGen.initialize(RSAKeySize, random);
			keyPair = RSAKeyGen.generateKeyPair();
		}

		public String decryptRSA(String encryptedBase64) {
			byte[] decode64 = base64(encryptedBase64);
			return new String(decrypt("RSA/ECB/PKCS1Padding", keyPair.getPrivate(), decode64));
		}

		public String getMessage(Socket socket) {
			Object obj = getUnencryptedObject(socket);
			return decryptRSA((String) obj);
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
			} catch (IOException e) { }
		}

		public void sendSignature(Socket socket, String message) {
			sendUnencryptedObject(socket, sign(message));
		}

		public String sign(String message) {
			try {
				/* Create a Signature object and initialize it with the private key */
				Signature rsaSignature = Signature.getInstance("SHA1withRSA");
				rsaSignature.initSign(keyPair.getPrivate());

	            /* Update and sign the data */
				byte[] buffer = message.getBytes();
				rsaSignature.update(buffer, 0, buffer.length);

	            /* Now that the data to be signed has been processed, generate a signature for it */
				byte[] generatedSignature = rsaSignature.sign();

				/* done now return */
				return base64(generatedSignature);
			} catch (Exception ignored) { }
			return "";
		}
	}

	public static class RSAPublic {
		private PublicKey publicKey;

		public RSAPublic() {}

		public RSAPublic(RSAPrivate rsaPrivate) {
			setPublicKey(rsaPrivate.getPublicKeyEncoded());
		}

		public RSAPublic(byte[] encodedPublicKey) {
			setPublicKey(encodedPublicKey);
		}

		public String encryptRSA(String message) {
			return base64(encrypt("RSA/ECB/PKCS1Padding", publicKey, message.getBytes()));
		}

		public void getKey(Socket socket) {
			byte[] encodedPublicKey = null;
			try {
				int len;
				byte[] byteLength = new byte[4];
				len = socket.getInputStream().read(byteLength, 0, 4);
				if (len == 0) return;
				ByteBuffer bb = ByteBuffer.wrap(byteLength);
				len = bb.getInt();
				encodedPublicKey = new byte[len];
				len = socket.getInputStream().read(encodedPublicKey);
				if (len == 0) return;
			} catch (Exception ignored) { }
			setPublicKey(encodedPublicKey);
		}

		public boolean getVerification(Socket socket, String originalData) {
			Object obj = getUnencryptedObject(socket);
			return verify(originalData, (String) obj);
		}

		public void sendMessage(Socket socket, String message) {
			sendUnencryptedObject(socket, encryptRSA(message));
		}

		public void setPublicKey(byte[] encodedPublicKey) {
			try {
			/* rebuild key */
				X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encodedPublicKey);
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				publicKey = keyFactory.generatePublic(pubKeySpec);
			} catch (Exception ignored) { }
		}

		public boolean verify(String originalData, String signature) {
			try {
			/* create a Signature object and initialize it with the public key */
				Signature sig = Signature.getInstance("SHA1withRSA");
				sig.initVerify(publicKey);

	            /* verify data */
				byte[] buffer = originalData.getBytes();
				sig.update(buffer, 0, buffer.length);

				/* done */
				return sig.verify(base64(signature));
			} catch (Exception ignored) { }
			return false;
		}
	}
}