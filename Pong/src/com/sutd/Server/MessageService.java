package com.sutd.Server;

import java.io.PrintWriter;

import com.badlogic.gdx.net.Socket;
import com.google.gson.Gson;
import com.sutd.GameObjects.GameState;
import com.sutd.Network.MessageHandler;

/**
 * It helps to manage all client sockets.
 * It can handle all requests from the clients, and it can send messages to all clients.
 * **/

class MessageService implements MessageHandler {
	GameServer server;
	Socket[] clients = new Socket[2];
	PrintWriter[] out = new PrintWriter[2];

	public MessageService(GameServer gameServer) {
		// TODO Auto-generated constructor stub
		this.server = gameServer;
	}

	public synchronized void addSocket(Socket client, int index) {
		clients[index] = client;
		out[index]  = new PrintWriter(client.getOutputStream());
	}

	//Handle requests.
	public synchronized void handle(int i, String type, String message) {
		if(type.equals("player_position")) {
			server.setPaddle(Double.parseDouble(message), i);
		}
		else if (type.equals("disconnect_event")) {
			System.out.println("Disconnect EVENTTT!!");
			server.handleDisconnect(i);
		}
	}

	// Broadcast Messages to all clients.
	// ignore is the client to not send to

	public synchronized void send(String message, int ignore) {
		for(int i = 0 ; i < out.length; i++) {
			if(i == ignore) continue; // don't send message to the sender!
			out[i].println(message);
			out[i].flush();
		}
	}
	public synchronized void send(String message) {
		send(message,-1);
	}

	public synchronized void sendState(GameState state, int ignore) {
		String type = "game_update;";
		send(type+getJSONfromState(state), ignore);
	}
	
	public synchronized void sendState(GameState state) {
		sendState(state,-1);
	}

	public synchronized void sendStateToSocket(GameState gameState, int i) {
		out[i].println("game_update;"+getJSONfromState(gameState));
		out[i].flush();
	}

	private synchronized String getJSONfromState(GameState state) {
		Gson gson = new Gson();
		String message = gson.toJson(state);
		return message;
	}
}