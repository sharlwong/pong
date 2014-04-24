package com.sutd.Network;

import java.util.concurrent.BlockingQueue;

public class MessageConsumer extends Thread{
	private BlockingQueue<String> buffer;
	private MessageHandler handler;

	public MessageConsumer(BlockingQueue<String> buffer, MessageHandler handler) {
		this.buffer = buffer;
		this.handler = handler;
	}
	public void run() {
		while(!this.isInterrupted()) {
        try {
        	// Buffer consumes message
        	String message = buffer.take();
        	String[] data = message.split(";");
        	int id = Integer.parseInt(data[0]);
        	
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