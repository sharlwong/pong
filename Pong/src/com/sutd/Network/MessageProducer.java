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
	private BlockingQueue buffer;
	
	public MessageProducer(BufferedReader reader, BlockingQueue buffer) {
		this.reader = reader;
		this.buffer = buffer;
	}
	public void run() {
		while(!this.isInterrupted()) {
			try {
				System.out.println("Listening for message to produce");
				String message = reader.readLine();
				buffer.offer(message);
				System.out.println("Listener listened: " + message);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}	
	