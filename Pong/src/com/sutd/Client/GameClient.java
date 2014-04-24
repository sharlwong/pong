package com.sutd.Client;

import java.io.BufferedReader;
import java.io.IOException;
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

// TODO: Make this  thread safe
// It is accessed by both ClientBroadcaster and StartWorld

public class GameClient {
	private Socket client_socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private BlockingQueue<String> buffer = new ArrayBlockingQueue<String>(10);
	private String serverAddress;
	private int serverPort;
	private boolean isReady = false;
	private MessageConsumer consumer;
	private MessageProducer listener;

	/* Pre-condition: We expect a server to be up and running
	 * and the address should be accessible to the client.
	 */
	public void connectToServer() {
		client_socket = Gdx.net.newClientSocket(Protocol.TCP,serverAddress , serverPort, null);
		writer = new PrintWriter(client_socket.getOutputStream());
		reader = new BufferedReader( new InputStreamReader(client_socket.getInputStream()));
	}

	public void sendMessage(String message) {
		writer.println(message);
		writer.flush();
	}

	public void startListening() {
		listener = new MessageProducer(reader, buffer);
		listener.start();
	}

	/**
	 * Start Dealing with the messages
	 * @param handler the handler that is callbacked after a message is received.
	 */
	public void startConsuming(MessageHandler handler) {
		consumer = new MessageConsumer(buffer, handler);
		consumer.start();
	}

	/*
	 * Initialize server details.
	 */
	public void setServer(String address, int port) {
		this.serverAddress = address;
		this.serverPort = port;
		this.isReady = true;
	}
	public boolean ready() {
		return this.isReady;
	}

	public void tearDown() {
		consumer.interrupt();
		listener.interrupt();
		// We dont care about joining...
		client_socket.dispose();
		writer.close();
	}
}