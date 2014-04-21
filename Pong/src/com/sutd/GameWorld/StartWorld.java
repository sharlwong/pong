package com.sutd.GameWorld;

import java.util.concurrent.CountDownLatch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Rectangle;
import com.sutd.Client.ClientBroadcaster;
import com.sutd.Pong.PongGame;
import com.sutd.PongHelpers.AssetLoader;
import com.sutd.PongHelpers.PasswordListener;
import com.sutd.Server.GameServer;


public class StartWorld {
	private Rectangle start_game_button;
	private Rectangle join_game_button;
	private PongGame pong_game;
	
	private Music chimp_short;
	
	public StartWorld(PongGame pong_game) {
		createButtons();
		this.pong_game = pong_game;
		this.chimp_short = AssetLoader.chimp_short;
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
			Gdx.app.log("MyTag", "Just Touched!");
			float x = (float) (Gdx.input.getX());
			float y = (float)(Gdx.input.getY());
			if(x>= Gdx.graphics.getWidth()*((float) 20/136) && x<= Gdx.graphics.getWidth()*((float)115/136) && 
					 					y>= Gdx.graphics.getHeight()*((float)124/204) && y<=Gdx.graphics.getHeight()*((float)149/204)) {
				//Probably start a server here.
				initializeServerAndClient();
				chimp_short.play();
			}
			
			else if(x>= Gdx.graphics.getWidth()*((float) 20/136) && x<= Gdx.graphics.getWidth()*((float)115/136) && 
					 					y>= Gdx.graphics.getHeight()*((float)154/204) && y<=Gdx.graphics.getHeight()*((float)179/204)) {
				intializeClientAndJoinServer();
				chimp_short.play();
			//connect to a server here.
			}
		}
	}
	
	/**
	 * Starts Server Thread
	 * And immediately connects to it as a client
	 */
	private void initializeServerAndClient() {
		// Start Server
		CountDownLatch start = new CountDownLatch(1);
		Gdx.app.log("my app","HOST");
		pong_game.server = new GameServer(start);
		pong_game.server.start();
		try {
			start.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Set OTP
		pong_game.client.setPassword("98398"); //Default server password
		//Start Broadcasting for server
		ClientBroadcaster bcaster = new ClientBroadcaster(pong_game.client);
		bcaster.start();
	}

	/**
	 * Start Client Thread
	 * **/

	private void intializeClientAndJoinServer() {
		Gdx.app.log("my app","CLIENT");
		//first set us up as player 1
		pong_game.player = 1;
		//Set OTP password
		Gdx.input.getTextInput(new PasswordListener(pong_game.client), "Enter Password", "");
		// broadcast request to join

		ClientBroadcaster bcaster = new ClientBroadcaster(pong_game.client);
		bcaster.start();
		/*pong_game.client = new GameClient();
		pong_game.client.connectToServer("localhost");
		pong_game.client.startListening();
		client2_created = true;
		pong_game.setScreen(new GameScreen(pong_game));*/
	}
}