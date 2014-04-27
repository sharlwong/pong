package com.sutd.Client;

import java.util.concurrent.BlockingQueue;

import com.google.gson.Gson;
import com.sutd.GameObjects.GameState;
import com.sutd.Network.MessageHandler;

/**
 * Update handler for the client.
 * Specifically, handles server updates and adds gamestates to the buffer
 * that the game render then uses to render the game state.
 */
public class ClientUpdater implements MessageHandler {
	BlockingQueue<GameState> stateBuffer;
	private int player;

	/**
	 * Constructor
	 * @param buffer the GameState buffer to add GameStates from server to.
	 * @param player Player id. Either 0 or 1. Used for player specific transforms.
	 */
	public ClientUpdater(BlockingQueue<GameState> buffer, int player) {
		this.stateBuffer = buffer;
		this.player = player;
	}

	/**
	 * Handles updates from server
	 * Converts game updates to GameStates and passes it to a buffer
	 * Flips game states for the second player.
	 */
	public void handle(int id, String type, String message) {
		if (!type.equals("game_update")) return;
		Gson gson = new Gson();
		GameState state = gson.fromJson(message, GameState.class);

		// Flip the state for the seconds player
		if (player == 1) state = state.flip();
		
		// Check if it is a disconnect event
		// These events are critical, therefore we force the buffer to accept this state.
		if (state.getStatus() == -1) { 
			// We need to force the buffer to accept this
			System.out.println("Yes Clinet exit state :)");
			try {
				stateBuffer.put(state);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//Otherwise, just try to offer the received packet to the buffer.
		else stateBuffer.offer(state);
	}
}
