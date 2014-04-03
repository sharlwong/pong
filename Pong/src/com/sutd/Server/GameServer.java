package com.sutd.Server;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;
import com.sutd.Network.MessageConsumer;
import com.sutd.Network.MessageProducer;


/**
 * The authoritative Server.
 * Responsible for handling requests
 * updating game state and responding.
 * @author Swayam
 *
 */
public class GameServer extends Thread {

	private ServerSocket serverSocket;
	private final int port = 5000;
	private Socket[] player_sockets = new Socket[2];
	private MessageProducer[] listeners = new MessageProducer[2];
	private MessageConsumer consumer;
	private BlockingQueue<String> buffer = new ArrayBlockingQueue<String>(50); 
	private MessageService message_service;
	
	/**
	 * Start the server socket
	 */
	public void run() {

		// Start Server Socket
		System.out.println("Server Starting...");
		serverSocket =  Gdx.net.newServerSocket(Protocol.TCP,port, null);
		
		// Accept Two clients to connect.
		// And start listening to messages from them.
		for(int i = 0 ; i < 2; i ++) {
			player_sockets[i] = serverSocket.accept(null);
			System.out.println("player_sockets " + i);
			listeners[i] = startListening(player_sockets[i]);
			System.out.println("listeners " + i);
		}
		message_service = new MessageService(player_sockets);
		startConsuming();
	}
	
	/**
	 * Listens to messages passed by client and stores them in a buffer 
	 * @param socket: The socket to start listening to.
	 * @return the actual object that handles message production.
	 */
	public MessageProducer startListening(Socket socket) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		MessageProducer producer = new MessageProducer(reader, buffer);
		producer.start();
		return producer;
	}
	
	/**
	 * Starts to consume the message passed by the client. 
	 */
	public void startConsuming() {
		consumer = new MessageConsumer(buffer, message_service) ;
		consumer.start();
	}

}
