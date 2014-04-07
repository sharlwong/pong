package com.sutd.GameWorld;

import java.awt.Dimension;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.sutd.PongHelpers.AssetLoader;

public class GameRenderer {
    
	private GameWorld game_world;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private Texture texture;

	private SpriteBatch batcher;
	
	private Dimension screenSize;
	
	int[][] balls;
	int[] player0;
	int[] player1;

	public GameRenderer(GameWorld world) {
		game_world = world;
		screenSize = world.getDim();
		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, 204);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);

		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		
		
		
	}
	
	public void keyPressed(int keycode) {
		game_world.keyDown(keycode);
	}

	public void keyReleased(int keycode) {
		game_world.keyUp(keycode);
	}
	

    public void render() {
    	// This runTime keeps accumulating, can be used by Ball class directly
    	
        //System.out.println("GameRenderer - render");
        
        balls = game_world.getBallXYs();
		player0 = game_world.getBottomPaddleXY();
		player1 = game_world.getTopPaddleXY();
		int[] scores = game_world.getScores();
        
        /*
         * 1. We draw a black background. This prevents flickering.
         */

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        String score0 = scores[0] + "";
        String score1 = scores[1] + "";
        
        batcher.begin();
        //AssetLoader.shadow.draw(batcher, "10", 100, 100);
        AssetLoader.font.draw(batcher, ""+score1, screenSize.width-20 - (3*score0.length()), screenSize.height/2 - 10);
        AssetLoader.font.draw(batcher, ""+score0, screenSize.width-20 - (3*score1.length()), screenSize.height/2 + 10);
        batcher.end();
        

        /*
         * 2. We draw the Filled rectangle
         */
        

        // Tells shapeRenderer to begin drawing filled shapes
        shapeRenderer.begin(ShapeType.Filled);

        /*render player 0 at the bottom */
        shapeRenderer.setColor(Color.BLUE);
        drawPaddle(player0[0], player0[1]);
		//shapeRenderer.("Player 0: "+ scores[0], dim.width/10, (int) (dim.height*0.99));
        

        /* render player 1 at the top */
		shapeRenderer.setColor(Color.RED);
		drawPaddle(player1[0], player1[1]);
		//g.drawString("Player 1: "+ scores[1], dim.width/10, (int) (dim.height*0.02));

		
		shapeRenderer.setColor(Color.WHITE);
		for (int[] ball : balls) drawBall(ball[0], ball[1]);
		
        // Tells the shapeRenderer to finish rendering
        // We MUST do this every time.
        shapeRenderer.end();
        
        
    }
    
    private void drawBall(int centerX, int centerY) {
		int radius = (int) game_world.calc.getBallPixelRadius();
		shapeRenderer.circle(centerX - radius, centerY - radius, 2 * radius);
	}

	private void drawPaddle(int centerX, int centerY) {
		int width = (int) game_world.calc.getPaddlePixelWidth();
		int height = (int) game_world.calc.getPaddlePixelDepth();
		shapeRenderer.rect(centerX - width / 2, centerY - height / 2, width, height);
	}


}