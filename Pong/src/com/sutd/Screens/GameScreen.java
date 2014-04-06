package com.sutd.Screens;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.sutd.GameWorld.GameRenderer;
import com.sutd.GameWorld.GameWorld;
import com.sutd.PongHelpers.InputHandler;

public class GameScreen implements Screen {
	
	private GameWorld game_world;
	private GameRenderer game_renderer;

    
	public GameScreen() {
	    System.out.println("GameScreen Attached");
	    Dimension dim = new Dimension(136, 204);
	    game_world = new GameWorld(dim);// initialize world
	    game_renderer = new GameRenderer(game_world); // initialize renderer
	    
	    Gdx.input.setInputProcessor(new InputHandler(game_world));
	}

	@Override
    public void render(float delta) {
        // Draws the RGB color 10, 15, 230, at 100% opacity
    	//System.out.println("Rendering!");
        //Gdx.gl.glClearColor(10/255.0f, 15/255.0f, 230/255.0f, 1f);
        //Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        game_world.update(delta);

        game_renderer.render();
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