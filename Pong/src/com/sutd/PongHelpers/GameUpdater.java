package com.sutd.PongHelpers;

import com.sutd.GameObjects.Paddle;
import com.sutd.GameWorld.GameWorld;
import com.sutd.Network.MessageHandler;

/** Update the game when the message is handled. **/

public class GameUpdater implements MessageHandler {
	GameWorld game_world;

	public GameUpdater(GameWorld game_world) {
		this.game_world = game_world;
	}
	public void handle(int id, String type, String message) {
		if(type.equals("opponent_position")) {
			System.out.println("Updating opponent to:"+message);
			game_world.getPaddle(1).setFractionalPosition(Double.parseDouble(message));
		}
		else if(type.equals("game_state")) {
			if(message.equals("ready")) game_world.ready = new Boolean(true);
		}
	}
}
