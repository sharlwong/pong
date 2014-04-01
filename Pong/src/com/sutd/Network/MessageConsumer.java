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
        System.out.println("Trying to consume ... Im really hungry :(");
        try {
        	String message = buffer.take();
        	handler.handle(message);
        	System.out.println("Consumer consumed" +message);
        	}
        catch (InterruptedException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        	}
        
		}
	}
}