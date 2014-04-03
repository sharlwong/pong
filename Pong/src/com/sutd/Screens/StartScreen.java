package com.sutd.Screens;

import com.badlogic.gdx.Screen;
import com.sutd.GameWorld.StartRenderer;
import com.sutd.GameWorld.StartWorld;


public class StartScreen implements Screen {
	
	private StartWorld start_world;
	private StartRenderer start_renderer;
    
    public StartScreen() {
        System.out.println("StartScreen Attached");
        start_world = new StartWorld(); 
        start_renderer = new StartRenderer(start_world);
    }

    @Override
    public void render(float delta) {
        // Draws the RGB color 10, 15, 230, at 100% opacity
    	//System.out.println("Rendering!");
        //Gdx.gl.glClearColor(10/255.0f, 15/255.0f, 230/255.0f, 1f);
        //Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        start_world.update();
        start_renderer.render(delta);
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