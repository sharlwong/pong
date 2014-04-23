package archived.security_lab.mutualAuth;

import java.net.InetAddress;
import java.net.Socket;

public class TestClient2 {

	public static void main(String[] args) throws Exception {
		String password = "1234567";
		final int port = 3344;
		Socket socket;
		String clientNonce = RSATools.nonce();
		String serverNonce;
		RSATools.AESHelper passwordAuthenticate = new RSATools.AESHelper(password);
		String temp1, temp2;
		int temp;
		boolean verified;

		System.out.println("Searching for connection...");
		/* connect to server */
		socket = new Socket(InetAddress.getLocalHost(), port);

		System.out.println("Exchanging keys...");
		/* send client public key */
		RSATools.RSAPrivate decryption = new RSATools.RSAPrivate();
		decryption.sendKey(socket);

		/* get server public key */
		RSATools.RSAPublic encryption = new RSATools.RSAPublic();
		encryption.getKey(socket);

		System.out.println("Exchanging nonsense...");
		/* send client nonce */
		encryption.sendMessage(socket, clientNonce);

		/* get server nonce */
		serverNonce = decryption.getMessage(socket);

		/* encrypt server nonce ++ password */
		String encryptedTemp = passwordAuthenticate.encryptString(serverNonce + password);

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
		String received = passwordAuthenticate.decryptString(temp1 + temp2);

		/* verification */
		temp = clientNonce.length();
		try {
			verified = received.substring(0, temp).equals(clientNonce);
			verified = verified && received.substring(temp).equals(password);
			if (!verified) {
				System.out.println("Verification failed, exiting...");
				System.exit(1);
			}
		} catch (Exception e) {
			System.out.println("Verification failed, exiting...");
			System.exit(1);
		}

		System.out.println("Verified!");

		/***********************************/
		/* DONE WITH MUTUAL AUTHENTICATION */
		/***********************************/

		/* listen */
		String s = decryption.getMessage(socket);
		System.out.println("Server: " + s);

		socket.close();
	}
}
