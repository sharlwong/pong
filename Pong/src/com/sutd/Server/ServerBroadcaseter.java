package com.sutd.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ServerBroadcaseter extends Thread {
	String address = "";
	String port = "";

	public ServerBroadcaseter(String address, String port) {
		this.address = address;
		this.port = port;
	}

	public void run() {
		try {
			DatagramSocket socket =  new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);

			while(true) {
				byte[] rec_buf = new byte[5000];
				DatagramPacket packet = new DatagramPacket(rec_buf, rec_buf.length);
				socket.receive(packet);

				String message = new String(packet.getData()).trim();
				System.out.println("Received:"+ message);
				//if packet is ours
				if(message.equals("ARE YOU PONG SERVER"));
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
	}

}