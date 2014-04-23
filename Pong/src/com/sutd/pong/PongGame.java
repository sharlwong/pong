package com.sutd.Pong;

import com.badlogic.gdx.Game;
import com.sutd.Client.GameClient;
import com.sutd.PongHelpers.AssetLoader;
import com.sutd.Screens.AuthScreen;
import com.sutd.Server.GameServer;

public class PongGame extends Game {
	public GameServer server;
	public GameClient client;
	public int        player;
	public String     t;

	@Override
	public void create() {
		System.out.println("Game Created!");
		initializeNetwork();
		AssetLoader.load();
		AssetLoader.music.play();
		AssetLoader.music.setLooping(true);
		setScreen(new AuthScreen(this));
		//setScreen(new StartScreen(this));
		//        setScreen(new GameScreen());
	}

	@Override
	public void dispose() {

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	private void initializeNetwork() {

	}
}
