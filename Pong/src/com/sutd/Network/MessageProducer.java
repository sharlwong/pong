package com.sutd.Network;

import java.io.BufferedReader;
import java.util.concurrent.BlockingQueue;

/**
 * Given a socket connection and a Buffer
 * It listens for messages and adds them to the buffer
 * @author Swayam
 *
 */
public class MessageProducer extends Thread {
	private BufferedReader reader;
	private BlockingQueue<String> buffer;
	
	public MessageProducer(BufferedReader reader, BlockingQueue<String> buffer) {
		this.reader = reader;
		this.buffer = buffer;
	}
	public void run() {
		while(!this.isInterrupted()) {
			try {
				System.out.println("SERVER: Listening for message to produce.");
				// Listens to message being produced.
				String message = reader.readLine();
				// Adds message to the buffer.
				buffer.offer(message);
				System.out.println("SERVER: Add to buffer: " + message);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}	
	