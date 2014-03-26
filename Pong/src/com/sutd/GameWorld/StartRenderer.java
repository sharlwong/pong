package com.sutd.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class StartRenderer {
	
    private StartWorld start_world;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;

    public StartRenderer(StartWorld world) {
        start_world = world;
        cam = new OrthographicCamera();
        cam.setToOrtho(true, 136, 204);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
    }

    public void render() {
        //System.out.println("StartRenderer - render");
        
        /*
         * 1. We draw a black background. This prevents flickering.
         */

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        /*
         * 2. We draw a start button (filled rectangle).
         */
        

        // Tells shapeRenderer to begin drawing filled shapes
        shapeRenderer.begin(ShapeType.Filled);

        // Chooses RGB Color of 87, 109, 120 at full opacity
        shapeRenderer.setColor(87 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);

        // Draws the rectangle from start_world (Using ShapeType.Filled)
        shapeRenderer.rect(start_world.getStartButton().x, start_world.getStartButton().y,
                start_world.getStartButton().width, start_world.getStartButton().height);

        // Tells the shapeRenderer to finish rendering
        // We MUST do this every time.
        shapeRenderer.end();
        
        /*
         * 3. We draw a join button (filled rectangle).
         */
        

        // Tells shapeRenderer to begin drawing filled shapes
        shapeRenderer.begin(ShapeType.Filled);

        // Chooses RGB Color of 87, 109, 120 at full opacity
        shapeRenderer.setColor(87 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);

        // Draws the rectangle from start_world (Using ShapeType.Filled)
        shapeRenderer.rect(start_world.getJoinButton().x, start_world.getJoinButton().y,
                start_world.getJoinButton().width, start_world.getJoinButton().height);

        // Tells the shapeRenderer to finish rendering
        // We MUST do this every time.
        shapeRenderer.end();
        
        

        /*
         * 4. We draw the rectangle's outline
         */

//        // Tells shapeRenderer to draw an outline of the following shapes
//        shapeRenderer.begin(ShapeType.Line);
//
//        // Chooses RGB Color of 255, 109, 120 at full opacity
//        shapeRenderer.setColor(255 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);
//
//        // Draws the rectangle from start_world (Using ShapeType.Line)
//        shapeRenderer.rect(start_world.getStartButton().x, start_world.getStartButton().y,
//                start_world.getStartButton().width, start_world.getStartButton().height);
//
//        shapeRenderer.end();
//        
        
        
    }

}
