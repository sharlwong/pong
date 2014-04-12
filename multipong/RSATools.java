package multipong;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Random;

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
			return new BASE64Decoder().decodeBuffer(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] decrypt(String instance, Key key, byte[] encrypted) {
		try {
			Cipher cipher = Cipher.getInstance(instance);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(encrypted);
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

	public static byte[] encrypt(String instance, Key key, byte[] plainText) {
		try {
			Cipher cipher = Cipher.getInstance(instance);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(plainText);
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

	public static Object deserialize(byte[] yourBytes) {
		ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
		ObjectInput in = null;
		Object o = null;
		try {
			in = new ObjectInputStream(bis);
			o = in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException ex) {
				// ignore close exception
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				// ignore close exception
			}
		}
		return o;
	}

	public static String digest(String message) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		assert md != null;
		md.update(message.getBytes());
		byte[] digest = md.digest();
		return base64(digest);
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

	public static byte[] serialize(Object yourObject) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(yourObject);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				// ignore close exception
			}
			try {
				bos.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}
		return bos.toByteArray();
	}

	public static Object getUnencryptedObject(Socket socket) {
		Object obj = null;
		try {
			ObjectInputStream obIn = new ObjectInputStream(socket.getInputStream());
			obj = obIn.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static void sendUnencryptedObject(Socket socket, Object message) {
		try {
			ObjectOutputStream obOut = new ObjectOutputStream(socket.getOutputStream());
			obOut.writeObject(message);
			obOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	 * Created by avery_000 on 07-Apr-14.
	 */
	public static class RSAPrivate {
		private final int RSAKeySize = 2048;
		private KeyPair keyPair;

		public RSAPrivate() {
			KeyPairGenerator RSAKeyGen = null;
			try {
				RSAKeyGen = KeyPairGenerator.getInstance("RSA");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
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
			} catch (IOException e) {
				e.printStackTrace();
			}
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
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (SignatureException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * Created by avery_000 on 07-Apr-14.
	 */
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
			} catch (IOException e) {
				e.printStackTrace();
			}
			setPublicKey(encodedPublicKey);
		}

		public void setPublicKey(byte[] encodedPublicKey) {
			try {
			/* rebuild key */
				X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encodedPublicKey);
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				publicKey = keyFactory.generatePublic(pubKeySpec);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}

		public boolean getVerification(Socket socket, String originalData) {
			Object obj = getUnencryptedObject(socket);
			return verify(originalData, (String) obj);
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
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (SignatureException e) {
				e.printStackTrace();
			}
			return false;
		}

		public void sendMessage(Socket socket, String message) {
			sendUnencryptedObject(socket, encryptRSA(message));
		}
	}
}
