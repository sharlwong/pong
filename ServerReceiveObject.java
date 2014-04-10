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
		RSATools.RSAEncryption encryption = new RSATools.RSAEncryption();
		encryption.getKey(socket);
		RSATools.RSADecryption decryption = new RSATools.RSADecryption();
		decryption.sendKey(socket);

		System.out.println("Generating randomness...");
		temp1 = RSATools.nonce();

		System.out.println("Exchanging bits...");
		encryption.sendMessage(socket, temp1);
		temp2 = decryption.getMessage(socket);

		System.out.println("Making signature...");
		RSATools.RSASign rsaSign = new RSATools.RSASign(decryption);
		RSATools.RSAVerify rsaVerify = new RSATools.RSAVerify(encryption);
		rsaSign.sendSignature(socket, temp1);
		verified = rsaVerify.getVerification(socket,temp2);

		System.out.println("Verified: " + verified);

		System.out.println("AES key now shared!\n");
		RSATools.AESHelper aesHelper = new RSATools.AESHelper(temp1 + temp2);

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
