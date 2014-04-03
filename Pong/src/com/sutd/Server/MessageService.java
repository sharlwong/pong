package com.sutd.Server;

import java.io.PrintWriter;
import java.util.Arrays;

import com.badlogic.gdx.net.Socket;
import com.sun.security.ntlm.Client;
import com.sutd.Network.MessageHandler;

/**
 * It helps to manage all client sockets.
 * It can handle all requests from the clients, and it can send messages to all clients.
 * **/

class MessageService implements MessageHandler {
	Socket[] clients;
	PrintWriter[] out;
	String message;
	
	MessageService(Socket clients[]) {
		this.clients = clients;
		out = new PrintWriter[clients.length];
		
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
