package com.sutd.pong;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sutd.Pong.PongGame;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Pong";
		cfg.useGL20 = false;
		cfg.width = 272;
		cfg.height = 408;
		
		PongGame pong_game = new PongGame();
		
		new LwjglApplication(pong_game, cfg);
	}
}
