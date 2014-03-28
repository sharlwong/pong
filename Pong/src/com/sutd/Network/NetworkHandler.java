package com.sutd.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;

public class NetworkHandler {
	private Socket client;
	private PrintWriter writer;
	private BufferedReader reader;
	private final int port = 5000;

	public void connectToServer(String address) {
		client = Gdx.net.newClientSocket(Protocol.TCP, address, port, null);
		writer = new PrintWriter(client.getOutputStream());
		reader = new BufferedReader( new InputStreamReader(client.getInputStream()));
	}
	
	public void sendMessage(String message) {
		writer.println(message);
		writer.flush();
	}
	
	public void startListening() {
		InputListener listener = new InputListener(reader);
		listener.start();
	}
	
	/**
	 * Listens for incoming messages from the server
	 * Notifies the Network handler about received messages. 
	 * @author Swayam
	 *
	 */
	class InputListener extends Thread {
		private BufferedReader reader;
		InputListener(BufferedReader reader) {
			this.reader = reader;
		}
		public void run() {
			while(!this.isInterrupted()) {
				try {
					String message = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}