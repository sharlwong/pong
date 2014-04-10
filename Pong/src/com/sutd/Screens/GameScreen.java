package com.sutd.Screens;

import java.awt.Dimension;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.sutd.GameObjects.GameState;
import com.sutd.GameWorld.GameRenderer;
import com.sutd.GameWorld.GameWorld;
import com.sutd.Pong.PongGame;
import com.sutd.PongHelpers.Constants;
import com.sutd.PongHelpers.GameUpdater;
import com.sutd.PongHelpers.InputHandler;

public class GameScreen implements Screen {
	
	private GameWorld game_world;
	private GameRenderer game_renderer;
	private PongGame pong_game;
	private GameUpdater updater;
	Constants calc;
	BlockingQueue<double[][]> buffer = new LinkedBlockingQueue<double[][]>(5);
    
	public GameScreen(PongGame pong_game) {
	    System.out.println("GameScreen Attached");
	    Dimension dim = new Dimension(136, 204);
	    game_world = new GameWorld();// initialize world
	    buffer.offer(game_world.getState());
	    game_renderer = new GameRenderer(buffer,dim); // initialize renderer
	    this.pong_game = pong_game;
	    calc = new Constants(dim);
	    Gdx.input.setInputProcessor(new InputHandler(game_world, calc));
	    updater = new GameUpdater(game_world);
	    pong_game.client.startConsuming(updater);
	}

	@Override
    public void render(float delta) {
        // Draws the RGB color 10, 15, 230, at 100% opacity
    	//System.out.println("Rendering!");
        //Gdx.gl.glClearColor(10/255.0f, 15/255.0f, 230/255.0f, 1f);
        //Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        game_world.update(delta);
        if (buffer.remainingCapacity() == 0){
        	buffer.poll();
        	buffer.offer(game_world.getState());
        }else{
        	buffer.offer(game_world.getState());
        }
        game_renderer.render();
        pong_game.client.sendMessage("player_position:"+game_world.getPaddle(0).getFractionalPosition());
        
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("GameScreen - resizing");
    }

    @Override
    public void show() {
        System.out.println("GameScreen - show called");
    }

    @Override
    public void hide() {
        System.out.println("GameScreen - hide called");     
    }

    @Override
    public void pause() {
        System.out.println("GameScreen - pause called");        
    }

    @Override
    public void resume() {
        System.out.println("GameScreen - resume called");       
    }

    @Override
    public void dispose() {
        // Leave blank
    }

}