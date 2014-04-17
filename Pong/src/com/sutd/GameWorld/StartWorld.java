package com.sutd.GameWorld;

import java.util.concurrent.CountDownLatch;

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
			float x = (float) (Gdx.input.getX())/ 2;
			float y = (float)(Gdx.input.getY()) / 2;
			System.out.println(x+" "+y);
			Gdx.app.log("xy", x+","+y);
			if(x>= Gdx.graphics.getWidth()*((float) 10/136) && x<= Gdx.graphics.getWidth()*((float)110/136) && 
					y>= Gdx.graphics.getHeight()*((float)10/204) && y<=Gdx.graphics.getHeight()*((float)40/204)) {
				System.out.println("host");
				//Probably start a server here.
				initializeServerAndClient();
			}
			
			else if(x>= Gdx.graphics.getWidth()*((float)10/136) && x<= Gdx.graphics.getWidth()*((float)110/136)&&
					y>= Gdx.graphics.getHeight()*((float)60/204) && y<=Gdx.graphics.getHeight()*((float)90/204)) {
				intializeClientAndJoinServer();
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