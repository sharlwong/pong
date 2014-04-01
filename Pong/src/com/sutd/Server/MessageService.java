package com.sutd.Server;

import java.io.PrintWriter;

import com.badlogic.gdx.net.Socket;
import com.sutd.Network.MessageHandler;

class MessageService implements MessageHandler {
	Socket[] clients;
	PrintWriter[] out;
	
	MessageService(Socket clients[]) {
		this.clients = clients;
		for(int i = 0 ; i< clients.length; i++) {
			out[i] = new PrintWriter(clients[i].getOutputStream());
		}
	}

	//Handle requests.
	public synchronized void handle(String message) {
		System.out.println("Server handled: "+message);
		send(message);
	}

	// Broadcast Messages to all clients.
	public synchronized void send(String message) {
		for(int i = 0 ; i < out.length; i++) {
			out[i].println(message);
			out[i].flush();
		}
		
	}
}
