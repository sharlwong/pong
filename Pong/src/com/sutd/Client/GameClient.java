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

/**
 * Encapsulates Client information and methods to talk to server.
 */
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

	/** Pre-condition: We expect a server to be up and running
	 * and the address should be accessible to the client.
	 * setServer() should have been called to setup the details. 
	 * 
	 * It opens a socket connection between this and the server.
	 * 
	 * Post-condition: It initializes the socket, the print writer and the reader
	 */
	public void connectToServer() {
		client_socket = Gdx.net.newClientSocket(Protocol.TCP,serverAddress , serverPort, null);
		writer = new PrintWriter(client_socket.getOutputStream());
		reader = new BufferedReader( new InputStreamReader(client_socket.getInputStream()));
	}

	/**
	 * Sends a message to the server
	 * @param message the message to be sent.
	 */
	public void sendMessage(String message) {
		writer.println(message);
		writer.flush();
	}

	/**
	 * Starts a listener thread that listens for information from the server.
	 */
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

	/**
	 * Initialize the server details
	 * 
	 * @param address Address of the server
	 * @param port Port of the server.
	 * 
	 * Post Condition: It sets ready status to be true.
	 */
	public synchronized void setServer(String address, int port) {
		this.serverAddress = address;
		this.serverPort = port;
		this.isReady = true;
	}
	
	/**
	 * @return whether server details have been set up or not.
	 */
	public synchronized boolean ready() {
		return this.isReady;
	}

	/**
	 * Sane handling of a disconnect event.
	 * Interrupts threads and closes sockets.
	 */
	public void tearDown() {
		consumer.interrupt();
		listener.interrupt();
		// We dont care about joining...
		client_socket.dispose();
		writer.close();
	}
}