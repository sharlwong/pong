package com.sutd.Client;

import java.util.concurrent.BlockingQueue;

import com.google.gson.Gson;
import com.sutd.GameObjects.GameState;
import com.sutd.Network.MessageHandler;

/**
 * Handles updates from the server.
 * @author Swayam
 *
 */
public class ClientUpdater implements MessageHandler {
	BlockingQueue<GameState> stateBuffer;
	private int player;

	/**
	 * 
	 * @param buffer: The buffer to add GameStates to.
	 * @param player: 0 or 1. We need it to flip GameState
	 */
	public ClientUpdater(BlockingQueue<GameState> buffer, int player) {
		this.stateBuffer = buffer;
		this.player = player;
	}

	/**
	 * @param id
	 * @param type: The type of update from the server i.e. the header of the message
	 * @param message the actual message from the server.
	 */
	public void handle(int id, String type, String message) {
		if (!type.equals("game_update")) return; 
		Gson gson = new Gson();
		GameState state = gson.fromJson(message, GameState.class);
		if (player == 1) state = state.flip(); // we need to flip the gamestate for second player
		
		// Check if it is a server exit event
		if (state.getStatus() == -1) { 
			// We need to force the buffer to accept this
			try {
				stateBuffer.put(state);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else stateBuffer.offer(state);
	}
}
