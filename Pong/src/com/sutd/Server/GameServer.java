package com.sutd.Server;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;
import com.sutd.Client.MutualAuthClient;
import com.sutd.GameWorld.GameWorld;
import com.sutd.Network.MessageConsumer;
import com.sutd.Network.MessageProducer;
import com.sutd.Network.RSATools;
import com.sutd.Network.RSATools.AESHelper;
import com.sutd.PongHelpers.Constants;


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
	private GameWorld game_world;
	private CountDownLatch started;

	public GameServer() {}
	/**
	 * Precondition: Started must be equal to 1
	 * @param started: a deterministic way of figuring out if the server is started or not.
	 */
	public GameServer(CountDownLatch started) {
		this.started = started;
		assert started.getCount() == 1;
	}

	/**
	 * Start the server socket
	 */
	public void run() {
		game_world = new GameWorld();
		message_service = new MessageService(this);
		// Start Server Socket
		System.out.println("Server Starting...");
		serverSocket = Gdx.net.newServerSocket(Protocol.TCP,port, null);
		System.out.println("Server started at:");
		//Start broadcasting presence
		ServerBroadcaseter broadcaster = new ServerBroadcaseter("lhst", "5000");
		broadcaster.start();
		//mark server as started
		if(started != null) started.countDown();
		// Accept Two clients to connect.
		// And start listening to messages from them.

		for(int i = 0 ; i < 2; i ++) {
			System.out.println("Waiting for client");
			player_sockets[i] = serverSocket.accept(null);
			// Authenticate the client
			AESHelper aes = null;
			try {
				aes = MutualAuthServer.authenticate(player_sockets[i]);
				if (aes == null) {
					i--;
					continue;
				}
			} catch (Exception e) {

			}
			listeners[i] = startListening(player_sockets[i],i,aes);
			message_service.addSocket(player_sockets[i],i,aes);
			message_service.sendStateToSocket(game_world.getGameState(), i);
			System.out.println("Client connected!");
		}
		startConsuming();
		game_world.ready = true; 

		ServerUpdater serverUpdater = new ServerUpdater(game_world, message_service);
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleWithFixedDelay(serverUpdater, 0, Constants.UPDATE_DELTA, TimeUnit.MILLISECONDS);
	}

	/**
	 * Listens to messages passed by client and stores them in a buffer 
	 * @param socket: The socket to start listening to.
	 * @return the actual object that handles message production.
	 */
	public MessageProducer startListening(Socket socket,int id, AESHelper aes) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		MessageProducer producer = new MessageProducer(reader, buffer, id, aes);
		producer.start();
		return producer;
	}

	/**
	 * Starts to consume the message passed by the client. 
	 * @param listeners2 
	 */
	public void startConsuming() {
		consumer = new MessageConsumer(buffer, message_service) ;
		consumer.start();
	}

	public void setPaddle(double fraction, int i) {
		if(!game_world.ready) return ;
		game_world.getPaddle(i).setFractionalPosition(fraction);
	}
}
