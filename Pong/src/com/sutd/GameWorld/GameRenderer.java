package com.sutd.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.sutd.GameObjects.GameState;
import com.sutd.GameObjects.Paddle;
import com.sutd.PongHelpers.AssetLoader;
import com.sutd.PongHelpers.Constants;
import com.sutd.PongHelpers.InputHandler;

import java.awt.Dimension;
import java.util.Random;
import java.util.concurrent.BlockingQueue;


public class GameRenderer {

	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batcher;
	private BitmapFont font;
	
	Dimension d;
	InputHandler inputHandler;
	Constants calc;
	
	private Paddle player_paddle;
	int[][] balls;
	int[] player0;
	int[] player1;
	private TextureRegion watermelon, kiwi, orange, wait_screen, splash_screen, game_screen, paddle;
	private GameState lastKnownState;

	int[]    scores;
	int[]    ballsType;
	double[] ballDoubles;
	int      timeLeft;

	BlockingQueue<GameState> buffer;

	public GameRenderer(Paddle paddle, BlockingQueue<GameState> buffer2, Dimension d) {
		this.d = d;
		calc = new Constants(d);
		this.buffer = buffer2;
		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, 204);

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		this.player_paddle = paddle;
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		initAssets();
	}

	private void initAssets() {
		watermelon = AssetLoader.watermelon;
		kiwi = AssetLoader.kiwi;
		orange = AssetLoader.orange;
		font = AssetLoader.font;
		splash_screen = AssetLoader.splash_screen;
		wait_screen = AssetLoader.wait_screen;
		paddle = AssetLoader.paddle;
		game_screen = AssetLoader.game_screen;
	}

	public void render(float runTime) {
		// This runTime keeps accumulating, can be used by Ball class directly

        /* get state */
		GameState state = buffer.poll();

		/* check state and store */
		if (state == null && lastKnownState == null) {
			System.out.println("Nothing to render...");
			return;
		}
		if (state == null) {
			state = lastKnownState;
			//System.out.println("Missed frame to render...");
		}
		else lastKnownState = state;

		/* make things to render */
		balls = calc.makeBallXYs(state.getBallsData());
		player0 = calc.makePaddleXY(state.getPlayer0Data(), 0);
		player1 = calc.makePaddleXY(state.getPlayer1Data(), 1);

		scores = state.getScores();
		ballDoubles = state.getSpareVar();
		ballsType = state.getBallsType();
		timeLeft = state.getTimeLeft();
		
		

		/* Black background drawn to prevent flickering - IMPORTANT */
		Gdx.gl.glClearColor(255, 255, 183, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		String score0 = scores[0] + "";
		String score1 = scores[1] + "";
		
		batcher.begin();
		
		//AssetLoader.shadow.draw(batcher, "10", 100, 100);
		
		if(state.getStatus() == 0) //Waiting for client!
		{
//			font.draw(batcher, "Waiting", d.width/2 - 30 , d.height / 2-20);
//			font.draw(batcher, "Player 2", d.width/2 - 30 , d.height / 2+10);

	        batcher.draw(wait_screen, 0, 0, 136, 204);
			
		}
		else if (timeLeft == 0){
			
			batcher.draw(game_screen, 0, 0, 136, 204);
			
			font.draw(batcher, "GAME OVER", d.width/2 - 48, d.height / 2 - 40);
			
			if (scores[0] > scores[1]) font.draw(batcher, "YOU WIN", d.width/2 - 40, d.height / 2 - 20);
			else if (scores[0] < scores[1]) font.draw(batcher, "YOU LOSE", d.width/2 - 37, d.height / 2 - 20);
			else font.draw(batcher, "TIE", d.width/2 - 10, d.height / 2 - 20);
			
			font.draw(batcher, score0+":"+score1, d.width/2 - 3 * score0.length() - 10, d.height / 2);
			
			font.draw(batcher, "AGAIN", 5, d.height - 40);
			font.draw(batcher, "EXIT", d.width/2 + 30, d.height - 40);	
		
		}else{
			 batcher.draw(game_screen, 0, 0, 136, 204);
			 
			/*** Draw paddles. ****/

			// Tells shapeRenderer to begin drawing filled shapes
			shapeRenderer.begin(ShapeType.Filled);

	        /*render player 0 at the bottom */
			shapeRenderer.setColor(Color.BLUE);
			drawPaddle(player0[0], player0[1]);

	        /* render player 1 at the top */
			shapeRenderer.setColor(Color.RED);
			drawPaddle(player1[0], player1[1]);

			/* why is this here ._. it doesn't seem useful */
			shapeRenderer.setColor(Color.WHITE);

	        // Tells the shapeRenderer to finish rendering
	        // We MUST do this every time.
	        shapeRenderer.end();
			
			font.draw(batcher, "" + score1, d.width - 20 - (3 * score0.length()), d.height / 2 - 20);
			font.draw(batcher, "" + score0, d.width - 20 - (3 * score0.length()), d.height / 2);
			font.draw(batcher, "" + timeLeft, 5, d.height / 2 - 10);
			
			for(int[] ball: balls){ 
				drawOrange(ball[0], ball[1]);
			}

			 /*
	         * Draw watermelon as a ball.
	         */

			// for (int[] ball : balls) drawWatermelon(ball[0], ball[1]);

	        
	        /*
	         * Draw kiwi as a ball.
	         */

			//        for (int[] ball : balls) drawKiwi(ball[0], ball[1]);

	        /*
	         * Draw orange as a ball.
	         */

			// for (int[] ball : balls) drawOrange(ball[0], ball[1]);

			// End SpriteBatch
		}
		batcher.end();

        /* Draw normal balls: For testing.*/
		// for (int[] ball : balls) drawBall(ball[0], ball[1]);

    }
    
    
    private void drawWatermelon(int centerX, int centerY) {
		int radius = (int) calc.getBallPixelRadius();
				
		// The octopus needs transparency, so we enable that.
        batcher.enableBlending();
        batcher.draw(watermelon, centerX - radius, centerY - radius, 2*radius, 2*radius);
	}
    
    private void drawOrange(int centerX, int centerY) {
		int radius = (int) calc.getBallPixelRadius();
				
		// The octopus needs transparency, so we enable that.
        batcher.enableBlending();
        batcher.draw(orange, centerX - radius, centerY - radius, 2*radius, 2*radius);
	}
    
    private void drawKiwi(int centerX, int centerY) {
		int radius = (int) calc.getBallPixelRadius();
				
		// The octopus needs transparency, so we enable that.
        batcher.enableBlending();
        batcher.draw(kiwi, centerX - radius, centerY - radius, 2*radius, 2*radius);
	}

	private void drawPaddle(int centerX, int centerY) {
		int width = (int) calc.getPaddlePixelWidth();
		int height = (int) calc.getPaddlePixelDepth();
		
		batcher.enableBlending();
        batcher.draw(paddle, centerX - width / 2, centerY - height / 2, width, height);
		
		//shapeRenderer.rect(centerX - width / 2, centerY - height / 2, width, height);
	}


	/*
	 * Returns a pseudo-random number between min(inclusive) and max (inclusive).
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max.
	 * @see java.util.Random#nextInt(int)
	 */

	private static int randInt(int min, int max) {

	    // Usually this can be a field rather than a method variable
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

}