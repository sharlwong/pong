package com.sutd.Pong;

import com.badlogic.gdx.Game;
import com.sutd.PongHelpers.AssetLoader;
import com.sutd.Screens.StartScreen;

public class PongGame extends Game {
	
	
	
	@Override
	public void create() {		
		System.out.println("Game Created!");
		AssetLoader.load();
        setScreen(new StartScreen(this));
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
}
