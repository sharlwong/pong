package com.sutd.Network;

import java.util.concurrent.BlockingQueue;

/**
 * Consumes messages received in the buffer 
 */

public class MessageConsumer extends Thread{
	private BlockingQueue<String> buffer;
	private MessageHandler handler;

	/**
	 * Constructor
	 * @param buffer the buffer to take messages  from
	 * @param handler the handler to callback when a message is received.
	 */
	public MessageConsumer(BlockingQueue<String> buffer, MessageHandler handler) {
		this.buffer = buffer;
		this.handler = handler;
	}
	
	/**
	 * Takes messages from the buffer (populated by listener)
	 * Parses the message into its components and calls handle with those components.
	 */
	public void run() {
		while(!this.isInterrupted()) {
        try {
        	// Buffer consumes message
        	String message = buffer.take();
        	String[] data = message.split(";");
        	int id = Integer.parseInt(data[0]);
        	if(data[1].equals("null")) {
        		System.out.println("Got Null from:"+id);
        		break;
        	}
        	// Message is passed to handler to handle.
        	handler.handle(id,data[1],data[2]);
        	
        	}
        catch (InterruptedException e) {
        	// TODO Auto-generated catch block
        	System.out.println("Consumer Interrupted. Exiting....");
        	return;
        	}
        catch (IndexOutOfBoundsException e) {
        	System.out.println("Got null..ie closed!");
        	break;
        	}
		}
	}
}