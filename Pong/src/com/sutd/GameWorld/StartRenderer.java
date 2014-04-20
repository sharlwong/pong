package com.sutd.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.sutd.GameObjects.Ball;
import com.sutd.GameObjects.Paddle;
import com.sutd.PongHelpers.AssetLoader;
import com.sutd.PongHelpers.Assets;

import com.sutd.PongHelpers.Vector2D;

public class StartRenderer {
	
    private StartWorld start_world;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    
    private SpriteBatch batcher;
	private TextureRegion splash_screen, wait_screen, game_screen, instr_screen;
	private Vector2D screenSize;
	private Texture texture;

	private long totalTime;
	
	private void initAssets() {
		splash_screen = AssetLoader.splash_screen;
		
		/* Screens for Sharlene to do testing on phone */
		wait_screen = AssetLoader.wait_screen;
		game_screen = AssetLoader.game_screen;
		instr_screen = AssetLoader.instr_screen;
	}

    public StartRenderer(StartWorld world) {
        start_world = world;
        cam = new OrthographicCamera();
        cam.setToOrtho(true, 136, 204);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        
//        screenSize = new Vector2D(136, 204);
        batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		texture = new Texture(Gdx.files.internal("data/texture.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		initAssets();
		
		totalTime = 0;
    }

    public void render(float runTime) {
    	
        //System.out.println("StartRenderer - render");
        
        /*
         * 1. We draw the background containing the title of the game, "Fruitball".
         */
    	
    	// Draw the splash screen.
    	batcher.begin();
    	
    	/* For Sharlene to do testing on phone */
    	batcher.draw(splash_screen, 0, 0, 136, 204);
//        batcher.draw(instr_screen, 0, 0, 136, 204);
     
//        batcher.draw(wait_screen, 0, 0, 136, 204);
        batcher.end();
        
        
        /*
         * 2. We draw a start button (filled rectangle).
         */
        
        // Enable transparency
        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        // Tells shapeRenderer to begin drawing filled shapes
        shapeRenderer.begin(ShapeType.Filled);

        // Chooses RGB Color of 87, 109, 120 at full opacity
        shapeRenderer.setColor(87 / 255.0f, 109 / 255.0f, 120 / 255.0f, 0.0f);

        // Draws the rectangle from start_world (Using ShapeType.Filled)
        shapeRenderer.rect(start_world.getStartButton().x, start_world.getStartButton().y,
                start_world.getStartButton().width, start_world.getStartButton().height);

        // Tells the shapeRenderer to finish rendering
        // We MUST do this every time.
        shapeRenderer.end();
        
        //Disable transparency
        Gdx.gl.glDisable(GL10.GL_BLEND);
        
        
//        batcher.begin();
//        AssetLoader.font.draw(batcher, "HOST", start_world.getStartButton().x + start_world.getStartButton().width/2 - 18, start_world.getStartButton().y+start_world.getStartButton().height/2 - 7);
//        batcher.end();
//        
        /*
         * 3. We draw a join button (filled rectangle).
         */

        // Enable transparency
        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        // Tells shapeRenderer to begin drawing filled shapes
        shapeRenderer.begin(ShapeType.Filled);

        // Chooses RGB Color of 87, 109, 120 at full opacity
        shapeRenderer.setColor(87 / 255.0f, 109 / 255.0f, 120 / 255.0f, 0.0f);

        // Draws the rectangle from start_world (Using ShapeType.Filled)
        shapeRenderer.rect(start_world.getJoinButton().x, start_world.getJoinButton().y,
                start_world.getJoinButton().width, start_world.getJoinButton().height);

        // Tells the shapeRenderer to finish rendering
        // We MUST do this every time.
        shapeRenderer.end();
        
        //Disable transparency
        Gdx.gl.glDisable(GL10.GL_BLEND);

//        batcher.begin();
//        AssetLoader.font.draw(batcher, "JOIN", start_world.getJoinButton().x + start_world.getJoinButton().width/2 - 18, start_world.getJoinButton().y+start_world.getJoinButton().height/2 - 7);
//        batcher.end();
       
        

    }
    

}
