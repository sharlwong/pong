package com.sutd.Server;

import com.sutd.GameWorld.GameWorld;

/**
 * Sends out periodic updates from server to client
 * @author Swayam
 *
 */
class ServerUpdater implements Runnable {
    private static long LastTime;
	private GameWorld game_world;
	private MessageService messageService;

	/**
	 * Constructor
	 * @param game_world: The game_world to get state information from
	 * @param msg_service: The service to use to send the state information.
	 */
	public ServerUpdater(GameWorld game_world, MessageService msg_service) {
		this.game_world = game_world;
		this.messageService = msg_service;
	}

	@Override
	/**
	 * Calculates time elapsed since the last time it was called
	 * and updates the game_world with that amount of time
	 * It then gets gamestate information from the server
	 * And then sends it to the clients thorugh the message service.
	 */
	public void run() {
		float delta = ((float) (System.nanoTime() - LastTime)) / 1000000;
		game_world.update(delta);
		//System.out.println(delta);
		messageService.sendState(game_world.getGameState());
		LastTime = System.nanoTime();
	}
}