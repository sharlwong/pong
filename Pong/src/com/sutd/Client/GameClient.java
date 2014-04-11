package com.sutd.Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.sutd.Network.MessageConsumer;
import com.sutd.Network.MessageHandler;
import com.sutd.Network.MessageProducer;

public class GameClient {
	private Socket client_socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private final int port = 5000;
	private BlockingQueue<String> buffer = new ArrayBlockingQueue<String>(10);
	
	/* Pre-condition: We expect a server to be up and running
	 * and the address should be accessible to the client.
	 */
	public void connectToServer(String address) {
		client_socket = Gdx.net.newClientSocket(Protocol.TCP, address, port, null);
		writer = new PrintWriter(client_socket.getOutputStream());
		reader = new BufferedReader( new InputStreamReader(client_socket.getInputStream()));
	}
	
	public void sendMessage(String message) {
		writer.println(message);
		writer.flush();
	}
	
	public void startListening() {
		MessageProducer listener = new MessageProducer(reader, buffer);
		listener.start();
	}
	
	/**
	 * Start Dealing with the messages
	 * @param handler the handler that is callbacked after a message is received.
	 */
	public void startConsuming(MessageHandler handler) {
		MessageConsumer consumer = new MessageConsumer(buffer, handler);
		consumer.start();
	}
}