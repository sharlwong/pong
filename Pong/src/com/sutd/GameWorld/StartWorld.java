package com.sutd.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.input.*;
import com.sutd.Client.GameClient;
import com.sutd.Network.*;
import com.sutd.Server.*;

public class StartWorld {
	private Rectangle start_game_button;
	private Rectangle join_game_button;
	public StartWorld() {
		createButtons();
	}
	
	/**
	 * Creates the buttons that is shown on the startscreen
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
	
	private void check_if_touched() {
		if(Gdx.input.justTouched()) {
			float x = (float) (Gdx.input.getX())/ 2;
			float y = (float)(Gdx.input.getY()) / 2;
			System.out.println("X: "+x+", y:"+y);
			if(start_game_button.contains(x, y)) {
				System.out.println("Start Touched!");
				createServerAndClient();
			}
			//Probably start a server here.
			else if(join_game_button.contains(x, y)) {
				System.out.println("Join Touched!");
			//connect to a server here.
			}
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
		GameClient client = new GameClient();
		client.connectToServer("localhost");
		client.startListening();
		client.startConsuming();
	}
}