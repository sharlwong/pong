package com.sutd.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Responds to broadcast packets to enable Server Discovery
 * @author Swayam
 *
 */
public class ServerBroadcaster extends Thread {
	String address = "";
	String port = "";

	/**
	 * Constructor
	 * @param address The address of the Fruitball server
	 * @param port The port of the Fruitball server.
	 */
	public ServerBroadcaster(String address, String port) {
		this.address = address;
		this.port = port;
	}

	/**
	 * Listens to requests and responds with port address of the successful ones.
	 */
	public void run() {
		DatagramSocket socket = null;
		try {
			//  Open a new broadcast socket.
			socket =  new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);

			// Listen for requests
			while(!this.isInterrupted()) {
				byte[] rec_buf = new byte[5000];
				DatagramPacket packet = new DatagramPacket(rec_buf, rec_buf.length);
				socket.receive(packet);

				String message = new String(packet.getData()).trim();
				System.out.println("Received:"+ message);
				//check if packet is sent by a Pong client
				if(message.equals("ARE YOU PONG SERVER"));
				//reply with the affirmative, and include port.
				byte[] send_data = ("YES I AM SERVER AT PORT;"+port).getBytes();
				DatagramPacket sendPacket = new DatagramPacket(send_data, send_data.length,packet.getAddress(), packet.getPort());
				socket.send(sendPacket);
				System.out.println("SENDING: YES I AM SERVER AT PORT;"+port);
			}
			

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Broadcaster Closing!");
		if(socket!=null) socket.close();
	}

}
