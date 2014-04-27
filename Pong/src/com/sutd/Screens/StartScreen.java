package com.sutd.Screens;

import com.badlogic.gdx.Screen;
import com.sutd.Client.GameClient;
import com.sutd.GameWorld.StartRenderer;
import com.sutd.GameWorld.StartWorld;
import com.sutd.Pong.PongGame;

/**
 * StartScreen implements a special Screen class from libGDX.
 *
 * It creates a new StartWorld and a new StartRenderer.
 * 
 * If the client is ready, it will prompt it to connect to the server and to
 * start listening. Then, it will set a new Game Screen.
 * 
 */

/**
 * Coordinates StartWorld and Start Renderer
 * Responsible for starting communication of the client with the server.
 */
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
    /**
     * Renders the StartScreen
     * Checks if client has been initialized. 
     * Connects to server and starts listening to updates if so and 
     * switches to GameScreen.
     */
    public void render(float delta) {
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