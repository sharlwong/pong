package com.sutd.Client;

import java.io.BufferedReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;

public class GameClient {
	private Socket client_socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private final int port = 5000;
	private BlockingQueue<String> buffer = new ArrayBlockingQueue<String>(10);
	
	/* Pre-condition: We expect a server to be up and running
	 * and the address should be accessible to the client.
	 */
	public void connectToServer(String address) {
		client_socket = Gdx.net.newClientSocket(Protocol.TCP, address, port, null);
		writer = new PrintWriter(client_socket.getOutputStream());
		reader = new BufferedReader( new InputStreamReader(client_socket.getInputStream()));
	}
	
	public void sendMessage(String message) {
		writer.println(message);
		writer.flush();
	}
	
	public void startListening() {
		MessageProducer listener = new MessageProducer(reader, buffer);
		listener.start();
	}
	
	public void startConsuming() {
		MessageConsumer consumer = new MessageConsumer(buffer);
		consumer.start();
	}
	
	/**
	 * Listens for incoming messages from the server
	 * Notifies the Network handler about received messages. 
	 * @author Swayam
	 *
	 */
	class MessageConsumer extends Thread {
		private BlockingQueue<String> buffer;
		MessageConsumer(BlockingQueue<String> buffer) {
			this.buffer = buffer;
		}
		
		public void run() {
//			while(!this.isInterrupted()) {
				System.out.println("CLIENT: Preparing to consume.");
				try {
					String message = buffer.take();
					System.out.println("CLIENT: Remove from buffer: " +message);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//			}
		}
		
	}

	class MessageProducer extends Thread {
		private BufferedReader reader;
		private BlockingQueue<String> buffer;
		MessageProducer(BufferedReader reader, BlockingQueue<String> buffer) {
			this.reader = reader;
			this.buffer = buffer;
		}
		public void run() {
//			while(!this.isInterrupted()) {
				try {
					System.out.println("CLIENT: Listening for message to produce");
					String message = reader.readLine();
					buffer.offer(message);
					System.out.println("CLIENT: Add to buffer: " + message);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
//			}
		}
	}
}