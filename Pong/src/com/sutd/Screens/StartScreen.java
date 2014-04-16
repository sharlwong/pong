package com.sutd.Screens;

import com.badlogic.gdx.Screen;
import com.sutd.Client.GameClient;
import com.sutd.GameWorld.StartRenderer;
import com.sutd.GameWorld.StartWorld;
import com.sutd.Pong.PongGame;


public class StartScreen implements Screen {
	
	private StartWorld start_world;
	private StartRenderer start_renderer;
	private PongGame game;
    
    public StartScreen(PongGame game){
        System.out.println("StartScreen Attached");
        start_world = new StartWorld(game); 
        start_renderer = new StartRenderer(start_world);
        this.game = game;
		this.game.client = new GameClient();
    }

    @Override
    public void render(float delta) {
        // Draws the RGB color 10, 15, 230, at 100% opacity
    	//System.out.println("Rendering!");
        //Gdx.gl.glClearColor(10/255.0f, 15/255.0f, 230/255.0f, 1f);
        //Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        start_world.update();
        start_renderer.render(delta);
        if (game.client.ready()) {
        	System.out.print("Ready");
        	game.client.connectToServer();
            game.client.startListening();
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        //System.out.println("StartScreen - resizing");
    }

    @Override
    public void show() {
        //System.out.println("StartScreen - show called");
    }

    @Override
    public void hide() {
        //System.out.println("StartScreen - hide called");     
    }

    @Override
    public void pause() {
        //System.out.println("StartScreen - pause called");        
    }

    @Override
    public void resume() {
        //System.out.println("StartScreen - resume called");       
    }

    @Override
    public void dispose() {
        // Leave blank
    }

}