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

/**
 * StartRenderer is responsible for rendering all the objects on the start screen (blue).
 * It calls upon objects from StartWorld to render.
 * We employ two rendering classes in LibGDX engine: ShapeRenderer and SpriteBatch.
 * ShapeRenderer is used to render in-built shapes in LibGDX, while SpriteBatch is
 * used to render images.***/

public class StartRenderer {
	
    private StartWorld start_world;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    
    private SpriteBatch batcher;
	private TextureRegion splash_screen, sound_icon;
	private Texture texture;
	
	private void initAssets() {
		splash_screen = AssetLoader.splash_screen;
		sound_icon = AssetLoader.sound_icon;
	}

    public StartRenderer(StartWorld world) {
        start_world = world;
        cam = new OrthographicCamera();
        cam.setToOrtho(true, 136, 204);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        
        batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		texture = new Texture(Gdx.files.internal("data/texture.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		initAssets();
    }

    public void render(float runTime) {
    	
        //System.out.println("StartRenderer - render");
    	
    	/* Black background drawn to prevent flickering - IMPORTANT */
		Gdx.gl.glClearColor(255, 255, 183, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        /* Draw the background containing the title of the game, "Fruitball" */
    	batcher.begin();
    	batcher.draw(splash_screen, 0, 0, 136, 204);
        batcher.end();
        
        
        /* Draw a start button (filled rectangle). */
        
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
        
        
        /* Draw a join button (filled rectangle). */

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
        
        
        /* Draw a mute button. */
        batcher.begin(); 
        batcher.enableBlending();
        batcher.draw(sound_icon, start_world.getMusicButton().x, start_world.getMusicButton().y,
                start_world.getMusicButton().width, start_world.getMusicButton().height);
        batcher.end();        

    }
    

}
