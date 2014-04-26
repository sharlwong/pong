package com.sutd.Pong;

import com.badlogic.gdx.Game;
import com.sutd.PongHelpers.AssetLoader;
import com.sutd.Screens.StartScreen;
import com.sutd.Server.*;
import com.sutd.Client.*;


/**
 * PongGame is the game object itself; it extends a special Game class from the libGDX library.
 */

public class PongGame extends Game {
	public GameServer server;
	public GameClient client;
	public int        player;

	@Override
	public void create() {
		System.out.println("Game Created!");
		initializeNetwork();
		AssetLoader.load();
		setScreen(new StartScreen(this));
		AssetLoader.music.play();
		AssetLoader.music.setLooping(true);
	}

	@Override
	public void dispose() {
		getScreen().dispose();
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
