package com.sutd.Screens;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.sutd.Client.ClientUpdater;
import com.sutd.GameObjects.GameState;
import com.sutd.GameObjects.Paddle;
import com.sutd.GameWorld.GameRenderer;
import com.sutd.Pong.PongGame;
import com.sutd.PongHelpers.Constants;
import com.sutd.PongHelpers.Dimension;
import com.sutd.PongHelpers.InputHandler;

/**
 * GameScreen concerns itself with activities that happen on the screen when the game is ongoing.
 * Its render method calls upon GameRenderer.
 * When a disconnect happens, it sets a new screen to StartScreen.
 * 
 * It implements a special libGDX class called Screen.
 */

public class GameScreen implements Screen {
	
	private Paddle player_paddle;
	private GameRenderer game_renderer;
	private PongGame pong_game;
	private ClientUpdater updater;
	private float runTime;
	Constants calc;
	BlockingQueue<GameState> buffer = new LinkedBlockingQueue<GameState>(Constants.STATE_BUFFER_SIZE);
    
	public GameScreen(PongGame pong_game) {
	    player_paddle = new Paddle(0);
	    player_paddle.setFractionalPosition(0.5);
	    Dimension dim = new Dimension(136, 204);
	    game_renderer = new GameRenderer(buffer,dim); 	// initialize renderer
	    this.pong_game = pong_game;
	    calc = new Constants(dim);
	    Gdx.input.setInputProcessor(new InputHandler(player_paddle, calc));
	    updater = new ClientUpdater(buffer, pong_game.player);
	    pong_game.client.startConsuming(updater);
	}

	@Override
    public void render(float delta) {
        runTime += delta;
        game_renderer.render(runTime);
        if(game_renderer.disconnect) {
        	handleDisconnect();
        	return;
        }
        pong_game.client.sendMessage("player_position;"+player_paddle.getTransformedFractionalPosition(pong_game.player));
    }

	private void handleDisconnect() {
		pong_game.client.tearDown();
		pong_game.client = null;
		pong_game.server = null;
		pong_game.setScreen(new StartScreen(pong_game));	
	}
	
    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {  
    }

    @Override
    public void pause() {      
    }

    @Override
    public void resume() {      
    }

    @Override
    public void dispose() {
        pong_game.client.sendMessage("disconnect_event;"+"I am disconnecting");
        pong_game.client.tearDown();
        //pong_game.server.tearDown();
    }

}