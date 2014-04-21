package com.sutd.Network;

import java.io.BufferedReader;
import java.util.concurrent.BlockingQueue;

import com.sutd.Network.RSATools.AESHelper;

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
	private RSATools.AESHelper aes;
	
	public MessageProducer(BufferedReader reader, BlockingQueue<String> buffer,int id, AESHelper aes) {
		this(reader, buffer,aes);
		this.id = id;
	}

	public MessageProducer(BufferedReader reader, BlockingQueue<String> buffer, AESHelper aes) {
		this.reader = reader;
		this.buffer = buffer;
		this.aes = aes;
	}
	public void run() {
		while(!this.isInterrupted()) {
			try {
				// Listens to message being produced.
				String message = reader.readLine();
				if(id != -1) System.out.println("Server got:"+message);
				else System.out.println("Client got:"+message);
				message = aes.decrypt(message);
				if(id != -1) System.out.println("Server decrpty:"+message);
				else System.out.println("Client decrypt:"+message);
				// Adds message to the buffer.
				boolean offered = buffer.offer(id+";"+message);
				//if(offered) System.out.println("Add to buffer"+ id +";" + message);

			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}
}	
	