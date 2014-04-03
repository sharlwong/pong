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
        System.out.println("SERVER: Preparing to consume.");
        try {
        	// Buffer consumes message
        	String message = buffer.take();
        	System.out.println("SERVER: Remove from buffer: " +message);
        	
        	// Message is passed to handler to handle.
        	handler.handle(message);
        	
        	}
        catch (InterruptedException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        	}
        
		}
	}
}