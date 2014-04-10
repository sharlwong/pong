package com.sutd.PongHelpers;

import java.util.concurrent.BlockingQueue;

import com.google.gson.Gson;
import com.sutd.GameObjects.GameState;
import com.sutd.GameWorld.GameWorld;
import com.sutd.Network.MessageHandler;


public class GameUpdater implements MessageHandler {
	BlockingQueue<GameState> stateBuffer;
	private int player;

	public GameUpdater(BlockingQueue<GameState> buffer, int player) {
		this.stateBuffer = buffer;
		this.player = player;
	}
	public void handle(int id, String type, String message) {
		if(!type.equals("game_update")) return;
		Gson gson = new Gson();
		GameState state = gson.fromJson(message, GameState.class);

		if (player == 1) state = state.flip();

		try {
			stateBuffer.put(state);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
