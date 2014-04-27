package com.sutd.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;
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
	
	/**
	 * Constructor 
	 * @param reader The BufferedReader object to listen to
	 * @param buffer The buffer to put messages received into.
	 * @param id Helper attribute. Useful to keep track of who sent the message.
	 */
	public MessageProducer(BufferedReader reader, BlockingQueue<String> buffer,int id) {
		this(reader, buffer);
		this.id = id;
	}

	public MessageProducer(BufferedReader reader, BlockingQueue<String> buffer) {
		this.reader = reader;
		this.buffer = buffer;
	}
	
	/**
	 * Listen for messages from socket's inputstream
	 * Put received messages in the buffer.
	 */
	public void run() {
		while(!this.isInterrupted()) {
			try {
				// Listens to message being produced.
				String message = reader.readLine();
				// Adds message to the buffer.
				boolean offered = buffer.offer(id+";"+message);
				//if(offered) System.out.println("Add to buffer"+ id +";" + message);

			} catch (IOException e) {
				System.out.println("Producer Closing...");
				break;
			}
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}	
	