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
	private int id = -1;
	
	public MessageProducer(BufferedReader reader, BlockingQueue<String> buffer,int id) {
		this(reader, buffer);
		this.id = id;
	}

	public MessageProducer(BufferedReader reader, BlockingQueue<String> buffer) {
		this.reader = reader;
		this.buffer = buffer;
	}
	public void run() {
		while(!this.isInterrupted()) {
			try {
				// Listens to message being produced.
				String message = reader.readLine();
				// Adds message to the buffer.
				boolean offered = buffer.offer(id+";"+message);
				if(offered) System.out.println("Add to buffer"+ id +";" + message);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}	
	