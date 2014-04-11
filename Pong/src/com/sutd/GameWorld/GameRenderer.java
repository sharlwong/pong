package com.sutd.GameWorld;

import java.awt.Dimension;
import java.util.concurrent.BlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.sutd.PongHelpers.AssetLoader;
import com.sutd.PongHelpers.Constants;
import com.sutd.PongHelpers.InputHandler;

public class GameRenderer {
    
	private GameWorld game_world;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;

	private SpriteBatch batcher;
	
	Dimension d;
	InputHandler inputHandler;
	Constants calc;
	
	int[][] balls;
	int[] player0;
	int[] player1;
	BlockingQueue<double[][]> buffer;
	
	private TextureRegion octopusSmile;
	

	public GameRenderer(BlockingQueue<double[][]> buffer, Dimension d) {
		this.d = d;
		calc = new Constants(d);
		//game_world = new GameWorld();
		this.buffer = buffer;
		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, 204);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);

		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		
		inputHandler = new InputHandler(game_world, calc);
		initAssets();
	}
	
	private void initAssets() {
		octopusSmile = AssetLoader.octopusSmile;
	}

    public void render(float runTime) {
    	// This runTime keeps accumulating, can be used by Ball class directly
    	
        //System.out.println("GameRenderer - render");
    	double[][] state = buffer.peek();
    	
		balls = calc.makeBallXYs(state);
		player0 = calc.makePaddleXY(state, 0);
		player1 = calc.makePaddleXY(state, 1);
		int[] scores = calc.makeScores(state);
        
        /*
         * We draw a black background. This prevents flickering.
         */

        Gdx.gl.glClearColor(153, 153, 255, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        String score0 = scores[0] + "";
        String score1 = scores[1] + "";
        
        
        batcher.begin();
        //AssetLoader.shadow.draw(batcher, "10", 100, 100);
        AssetLoader.font.draw(batcher, ""+score1, d.width-20 - (3*score0.length()), d.height/2 - 20);
        AssetLoader.font.draw(batcher, ""+score0, d.width-20 - (3*score0.length()), d.height/2);
        
        /*
         * Draw octopus as a ball and animate it!
         */
        
        for (int[] ball : balls) drawOctopus(ball[0], ball[1]);
        		
        
        // End SpriteBatch
        batcher.end();

        /*
         * Draw paddles.
         */
        

        // Tells shapeRenderer to begin drawing filled shapes
        shapeRenderer.begin(ShapeType.Filled);
        
        /* Draw normal balls: For testing.*/
         for (int[] ball : balls) drawBall(ball[0], ball[1]); 

        /*render player 0 at the bottom */
        shapeRenderer.setColor(Color.BLUE);
        drawPaddle(player0[0], player0[1]);
		//shapeRenderer.("Player 0: "+ scores[0], dim.width/10, (int) (dim.height*0.99));
        

        /* render player 1 at the top */
		shapeRenderer.setColor(Color.RED);
		drawPaddle(player1[0], player1[1]);
		//g.drawString("Player 1: "+ scores[1], dim.width/10, (int) (dim.height*0.02));

		shapeRenderer.setColor(Color.WHITE);
		
        // Tells the shapeRenderer to finish rendering
        // We MUST do this every time.
        shapeRenderer.end();
        
        
    }
    
    private void drawOctopus(int centerX, int centerY) {
		int radius = (int) calc.getBallPixelRadius();
				
		// The octopus needs transparency, so we enable that.
        batcher.enableBlending();
        batcher.draw(octopusSmile, centerX, centerY, 2*radius, 2*radius);
	}
    
    
    private void drawTwoOctopus(int centerX, int centerY, float runTime) {
		int radius = (int) calc.getBallPixelRadius();
//		shapeRenderer.circle(centerX - radius, centerY - radius, 2 * radius);
				
		// The octopus needs transparency, so we enable that.
        batcher.enableBlending();
        
        // Draw bird at its coordinates. Retrieve the Animation object from AssetLoader
        // Pass in the runTime variable to get the current frame.
        batcher.draw(AssetLoader.octopusAnimation.getKeyFrame(runTime),
        		centerX, centerY, 2*radius, 2*radius);

	}
    
    private void drawBall(int centerX, int centerY) {
		int radius = (int) calc.getBallPixelRadius();
		shapeRenderer.circle(centerX - radius, centerY - radius, 2 * radius);
	}

	private void drawPaddle(int centerX, int centerY) {
		int width = (int) calc.getPaddlePixelWidth();
		int height = (int) calc.getPaddlePixelDepth();
		shapeRenderer.rect(centerX - width / 2, centerY - height / 2, width, height);
	}
}