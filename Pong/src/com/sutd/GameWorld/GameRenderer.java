package com.sutd.GameWorld;

import java.awt.Dimension;
import java.util.concurrent.BlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.sutd.GameObjects.GameState;
import com.sutd.GameObjects.Paddle;
import com.sutd.PongHelpers.AssetLoader;
import com.sutd.PongHelpers.Constants;
import com.sutd.PongHelpers.InputHandler;

public class GameRenderer {
    
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batcher;
	
	Dimension d;
	InputHandler inputHandler;
	Constants calc;
	
	private Paddle player_paddle;
	int[][] balls;
	int[] player0;
	int[] player1;
	private GameState lastKnownState;
	BlockingQueue<GameState> buffer;

	public GameRenderer(Paddle paddle, BlockingQueue<GameState> buffer2, Dimension d) {
		this.d = d;
		calc = new Constants(d);
		//game_world = new GameWorld();
		this.buffer = buffer2;
		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, 204);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		this.player_paddle = paddle;
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
	}

    public void render() {
    	// This runTime keeps accumulating, can be used by Ball class directly
    	
        //System.out.println("GameRenderer - render");
    	GameState state = buffer.poll();
    	if(state == null && lastKnownState == null) return;
    	if(state == null) state = lastKnownState;
    	else lastKnownState = state;

		balls = calc.makeBallXYs(state.getBallsData());
		//player0 = calc.makePaddleXY(state.getPlayer0Data(), 0);
		player0 = calc.makePaddleXY(player_paddle.getXY(),0);
		player1 = calc.makePaddleXY(state.getPlayer1Data(), 1);
		int[] scores = state.getScores(); 
        
        /*
         * 1. We draw a black background. This prevents flickering.
         */

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        String score0 = scores[0] + "";
        String score1 = scores[1] + "";
        
        batcher.begin();
        //AssetLoader.shadow.draw(batcher, "10", 100, 100);
        AssetLoader.font.draw(batcher, ""+score1, d.width-20 - (3*score0.length()), d.height/2 - 20);
        AssetLoader.font.draw(batcher, ""+score0, d.width-20 - (3*score0.length()), d.height/2);
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
		int radius = (int) calc.getBallPixelRadius();
		shapeRenderer.circle(centerX - radius, centerY - radius, 2 * radius);
	}

	private void drawPaddle(int centerX, int centerY) {
		int width = (int) calc.getPaddlePixelWidth();
		int height = (int) calc.getPaddlePixelDepth();
		shapeRenderer.rect(centerX - width / 2, centerY - height / 2, width, height);
	}
}