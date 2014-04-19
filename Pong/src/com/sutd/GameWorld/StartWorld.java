package com.sutd.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntIntMap;
import com.sutd.Client.ClientBroadcaster;
import com.sutd.Client.GameClient;
import com.sutd.Pong.PongGame;
import com.sutd.Screens.GameScreen;
import com.sutd.Server.GameServer;


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
		start_game_button = new Rectangle(20,124,95,25);
		join_game_button = new Rectangle(20,154,95,25);
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
			if(start_game_button.contains(x, y)) {
				//Probably start a server here.
				initializeServerAndClient();
			}
			
			else if(join_game_button.contains(x, y)) {
				intializeClientAndJoinServer();
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
		}
	}
	
	/**
	 * Starts Server Thread
	 * And immediately connects to it as a client
	 */
	private void initializeServerAndClient() {
		// Start Server
		pong_game.server = new GameServer();
		pong_game.server.start();
		pong_game.client.setServer("localhost", 5000); //default
		/*pong_game.client.connectToServer();
		pong_game.client.startListening();
		pong_game.player = 0;
		pong_game.setScreen(new GameScreen(pong_game)); */
	}
	
	/**
	 * Start Client Thread
	 * **/
	
	private void intializeClientAndJoinServer() {
		//first set us up as player 1
		pong_game.player = 1;
		// broadcast request to join
		ClientBroadcaster bcaster = new ClientBroadcaster(pong_game.client);
		bcaster.start();
		//once discovered, we can start:
		/*pong_game.client = new GameClient();
		pong_game.client.connectToServer("localhost");
		pong_game.client.startListening();
		client2_created = true;
		pong_game.setScreen(new GameScreen(pong_game));*/
	}
}