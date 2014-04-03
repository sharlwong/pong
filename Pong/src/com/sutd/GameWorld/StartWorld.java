package com.sutd.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.input.*;
import com.sutd.Client.GameClient;
import com.sutd.Network.*;
import com.sutd.Pong.PongGame;
import com.sutd.Screens.GameScreen;
import com.sutd.Server.*;


public class StartWorld {
	private Rectangle start_game_button;
	private Rectangle join_game_button;
	private Boolean server_created = false;
	private Boolean client1_created = false;
	private Boolean client2_created = false;
	private PongGame pong_game;
	
	public StartWorld(PongGame pong_game) {
		createButtons();
		this.pong_game = pong_game;
	}
	
	/**
	 * Creates the buttons that is shown on the start screen
	 */
	private void createButtons() {
		start_game_button = new Rectangle(10,10,100,30);
		join_game_button = new Rectangle(10,60,100,30);
	}
	
	public Rectangle getStartButton() {
		return new Rectangle(start_game_button);
	}
	public Rectangle getJoinButton() {
		return new Rectangle(join_game_button);
	}
	
	public void update() {
		check_if_touched();
		check_if_server_clients_created();
	}
	
	/** Checks if start game button and join game button starts.
	 * 
	 * If start game button is touched, a new server and a new client is created. 
	 * The client connects to the server.
	 * 
	 * If join game button is touched, a new client is created and connected to the server too.*
	 * */
	
	private void check_if_touched() {
		if(Gdx.input.justTouched()) {
			float x = (float) (Gdx.input.getX())/ 2;
			float y = (float)(Gdx.input.getY()) / 2;
			System.out.println("X: "+x+", y:"+y);
			if(start_game_button.contains(x, y)) {
				System.out.println("Start Touched!");
				//Probably start a server here.
				createServerAndClient();
				System.out.println("Finish starting game.\n");
			}
			
			else if(join_game_button.contains(x, y)) {
				System.out.println("Join Touched!");
				createClientAndJoinServer();
				System.out.println("Finish joining game.\n");
			//connect to a server here.
			}
		}
	}
	
	/**
	 * Checks if server and clients are created successfully.
	 * If they are, the screen is changed to GameScreen. 
	 * */
	
	private void check_if_server_clients_created(){
		if(server_created == true && client1_created == true && client2_created == true){
			pong_game.setScreen(new GameScreen());
		}
	}
	
	/**
	 * Starts Server Thread
	 * And immediately connects to it as a client
	 */
	private void createServerAndClient() {
		// Start Server
		GameServer server = new GameServer();
		server.start();
		server_created = true;
		
		GameClient client1 = new GameClient();
		client1.connectToServer("localhost");
		client1.startListening();
		System.out.println("Client 1 finishes listening.");
		client1.startConsuming();
		System.out.println("Client 1 finishes consuming.");
		client1.sendMessage("Player 1: Hi!");
		client1_created = true;
	}
	
	/**
	 * Start Client Thread
	 * **/
	
	private void createClientAndJoinServer() {
		GameClient client2 = new GameClient();
		client2.connectToServer("localhost");
		client2.startListening();
		System.out.println("Client 2 finishes listening.");		
		client2.startConsuming();
		System.out.println("Client 2 finishes consuming.");
//		client2.sendMessage("Player 2: Hi!");
		client2_created = true;
		
	}
}