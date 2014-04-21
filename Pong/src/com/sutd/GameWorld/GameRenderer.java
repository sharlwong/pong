package com.sutd.GameWorld;

import java.util.concurrent.BlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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
import com.sutd.PongHelpers.Dimension;
import com.sutd.PongHelpers.InputHandler;
import com.sutd.PongHelpers.Dimension;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * GameRenderer is responsible for rendering all the objects onscreen.
 * It calls upon values from Constants and assets from AssetLoader to render.
 * We employ two rendering classes in LibGDX engine: ShapeRenderer and SpriteBatch.
 * ShapeRenderer is used to render in-built shapes in LibGDX, while SpriteBatch is
 * used to render images.***/

public class GameRenderer {

	private OrthographicCamera cam;
	private ShapeRenderer      shapeRenderer;
	private SpriteBatch        batcher;
	private BitmapFont         font;

	Dimension    d;
	InputHandler inputHandler;
	Constants    calc;

	private Paddle player_paddle;
	int[][] balls;

	int[] player0;
	int[] player1;
	
	private TextureRegion watermelon, kiwi, orange, wait_screen, instr_screen, game_screen, paddle_top, paddle_bottom;
	private GameState lastKnownState;
	private Music     chimp_long, chimp_short;

	int[] scores;
	int[] ballsType;

	double[] ballDoubles;
	int      timeLeft;
	int      countDown;
	int      tick;
	int		 ballCounter;

	BlockingQueue<GameState> buffer;

	public GameRenderer(Paddle paddle, BlockingQueue<GameState> buffer2, Dimension d) {
		this.d = d;
		calc = new Constants(d);
		this.buffer = buffer2;
		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, 204);
		countDown = Constants.COUNT_DOWN_SECOND;
		tick = Constants.GAME_TIME + Constants.COUNT_DOWN_SECOND;
		ballCounter = 1;

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		this.player_paddle = paddle;
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		initAssets();
	}

	/*** Initialize assets needed for this class. ***/
	
	private void initAssets() {
		watermelon = AssetLoader.watermelon;
		kiwi = AssetLoader.kiwi;
		orange = AssetLoader.orange;
		font = AssetLoader.font;
		instr_screen = AssetLoader.instr_screen;
		wait_screen = AssetLoader.wait_screen;
		paddle_top = AssetLoader.paddle_top;
		paddle_bottom = AssetLoader.paddle_bottom;
		game_screen = AssetLoader.game_screen;
		chimp_long = AssetLoader.chimp_long;
		chimp_short = AssetLoader.chimp_short;
	}

	public void render(float runTime) {
		// This runTime keeps accumulating, can be used by Ball class directly

		/* Get state */
		GameState state = buffer.poll();

		/* Check state and store it */
		if (state == null && lastKnownState == null) {
			System.out.println("Nothing to render...");
			return;
		}
		if (state == null) {
			state = lastKnownState;
			// System.out.println("Missed frame to render...");
		}
		else lastKnownState = state;

		/* Make things to render */
		balls = calc.makeBallXYs(state.getBallsData());
		player0 = calc.makePaddleXY(state.getPlayer0Data(), 0);
		player1 = calc.makePaddleXY(state.getPlayer1Data(), 1);

		scores = state.getScores();
		ballDoubles = state.getSpareVar();
		ballsType = state.getBallsType();
		timeLeft = state.getTimeLeft();
		
		/* Black background drawn to prevent flickering - IMPORTANT. */
		Gdx.gl.glClearColor(255, 255, 183, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		String score0 = scores[0] + "";
		String score1 = scores[1] + "";

		/* Keep time */
		if ((timeLeft - tick) == -1) {
			tick--;
			System.out.println("Ticks: " + tick);
			if (countDown > 0) {
				countDown--;
				System.out.println("Countdown: " + countDown);
			}
		}

		batcher.begin();

		/* Wait for second client to join server. */
		if (state.getStatus() == 0) {
			//			font.draw(batcher, "Waiting", d.width / 2 - 30, d.height / 2 - 20);
			//			font.draw(batcher, "Player 2", d.width / 2 - 30, d.height / 2 + 10);

			batcher.draw(wait_screen, 0, 0, 136, 204);
		}
		
		/* Game over */
		else if (timeLeft == 0) {
			
			batcher.draw(game_screen, 0, 0, 136, 204);
			font.draw(batcher, "GAME OVER", d.width / 2 - 48, d.height / 2 - 40);
			
			/* Handle conclusion of game - whether there is a winner or not, and if the player is the winner. */
			if (scores[0] > scores[1]) font.draw(batcher, "YOU WIN", d.width / 2 - 40, d.height / 2 - 20);
			else if (scores[0] < scores[1]) font.draw(batcher, "YOU LOSE", d.width / 2 - 37, d.height / 2 - 20);
			else font.draw(batcher, "TIE", d.width / 2 - 10, d.height / 2 - 20);
			
			/* Show final scores of both players. */
			font.draw(batcher, score0 + ":" + score1, d.width / 2 - 3 * score0.length() - 10, d.height / 2);
			
			/* Play again? If so, instantiate countDown and tick to original values. */
			font.draw(batcher, "AGAIN ?", 1 * d.width / 3, d.height - 40);
			countDown = Constants.AGAIN_COUNT_DOWN_SECOND;
			tick = Constants.GAME_TIME + Constants.AGAIN_COUNT_DOWN_SECOND;

			chimp_short.play();

		}

		else {
			
			/* Game play starts */			
			if (countDown == 0) {
				batcher.draw(game_screen, 0, 0, 136, 204);

				
				for(int[] ball: balls){ 
//					if (ballsType[ballCounter] == 1){
//						drawOrange(ball[0], ball[1]);
//					} else if (ballsType[ballCounter] == 0){
//						drawKiwi(ball[0], ball[1]);
//					} else if (ballsType[ballCounter] == 0){
//						drawWatermelon(ball[0], ball[1]);
//					}
//					
					ballCounter++;
					
					System.out.println("Ball counter: "+ ballCounter);
					
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

				/* Draw paddles. Render player 0 at the bottom, render player 1 at the top. */
				drawBottomPaddle(player0[0], player0[1]);
				drawTopPaddle(player1[0], player1[1]);

				/* Draw scores on the right and time left in the game on the left. */
				font.draw(batcher, "" + score1, d.width - 20 - (3 * score0.length()), d.height / 2 - 20);
				font.draw(batcher, "" + score0, d.width - 20 - (3 * score0.length()), d.height / 2);
				font.draw(batcher, "" + timeLeft, 5, d.height / 2 - 10);

				/* Play long chimp call sound to signal end of game. */
				chimp_long.play();
			}
			
			/* Game is loading. */
			else {

				/* Load instructions screen. */
				if (countDown > 2) {
					batcher.draw(instr_screen, 0, 0, 136, 204);
					
				}

				/* Load the words: READY? GO! */
				else if (countDown == 2) {
					batcher.draw(game_screen, 0, 0, 136, 204);
					font.draw(batcher, "READY ?", d.width / 3, d.height / 2 - 20);
					
					/* Play short chimp call to signal start of game. */
					chimp_short.play();
				}
				else if (countDown == 1) {

					batcher.draw(game_screen, 0, 0, 136, 204);
					font.draw(batcher, "GO !", 2 * d.width / 5, d.height / 2 - 20);
				}
			}
		}
		batcher.end();

		/* Draw normal balls: For testing. */
		// for (int[] ball : balls) drawBall(ball[0], ball[1]);
	}

	/** Use SpriteBatch to draw a watermelon as a ball.
	 * @param int centerX, x-coordinate of center point of ball 
	 * @param int centerY, y-coordinate of center point of ball
	 * */
	private void drawWatermelon(int centerX, int centerY) {
		int radius = (int) calc.getBallPixelRadius();

		// The watermelon needs transparency, so we enable that.
		batcher.enableBlending();
		batcher.draw(watermelon, centerX - radius, centerY - radius,
				2 * radius * 7/6, 2 * radius * 7/6);
	}
	
	/** Use SpriteBatch to draw an orange as a ball.
	 * @param int centerX, x-coordinate of center point of ball 
	 * @param int centerY, y-coordinate of center point of ball
	 * */
	private void drawOrange(int centerX, int centerY) {
		int radius = (int) calc.getBallPixelRadius();

		// The orange needs transparency, so we enable that.
		batcher.enableBlending();
		batcher.draw(orange, centerX - radius, centerY - radius, 2 * radius  * 7/6,
				2 * radius  * 7/6);
	}

	/** Use SpriteBatch to draw a kiwi as a ball.
	 * @param int centerX, x-coordinate of center point of ball 
	 * @param int centerY, y-coordinate of center point of ball
	 * */
	private void drawKiwi(int centerX, int centerY) {
		int radius = (int) calc.getBallPixelRadius();

		// The kiwi needs transparency, so we enable that.
		batcher.enableBlending();

		batcher.draw(kiwi, centerX - radius, centerY - radius, 2 * radius * 7/6,
				2 * radius * 7/6);
	}
	
	/** Use SpriteBatch to draw an inverted banana as a top paddle.
	 * @param int centerX, x-coordinate of center point of paddle 
	 * @param int centerY, y-coordinate of center point of paddle
	 * */

	private void drawTopPaddle(int centerX, int centerY) {
		int width = (int) calc.getPaddlePixelWidth();
		int height = (int) calc.getPaddlePixelDepth();

		batcher.enableBlending();
        batcher.draw(paddle_top, centerX - width / 2, centerY - height / 2, width, height);
		
		//shapeRenderer.rect(centerX - width / 2, centerY - height / 2, width, height);
	}
	
	/** Use SpriteBatch to draw a banana as a bottom paddle.
	 * @param int centerX, x-coordinate of center point of paddle 
	 * @param int centerY, y-coordinate of center point of paddle
	 * */

	private void drawBottomPaddle(int centerX, int centerY) {
		int width = (int) calc.getPaddlePixelWidth();
		int height = (int) calc.getPaddlePixelDepth();
		
		batcher.enableBlending();
        batcher.draw(paddle_bottom, centerX - width / 2, centerY - height / 2, width, height);
		
		//shapeRenderer.rect(centerX - width / 2, centerY - height / 2, width, height);
	}


	/*
	 * This method is used only for testing purposes.
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