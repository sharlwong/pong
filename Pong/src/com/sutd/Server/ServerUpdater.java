package com.sutd.Server;

import com.sutd.GameWorld.GameWorld;

class ServerUpdater implements Runnable {
    private static long LastTime;
	private GameWorld game_world;
	private MessageService messageService;

	public ServerUpdater(GameWorld game_world, MessageService msg_service) {
		this.game_world = game_world;
		this.messageService = msg_service;
	}

	@Override
	public void run() {
		float delta = ((float) (System.nanoTime() - LastTime)) / 1000000;
		game_world.update(delta);
		//System.out.println(delta);
		messageService.sendState(game_world.getGameState());
		LastTime = System.nanoTime();
	}
}
