package com.sutd.GameWorld;

import java.util.concurrent.BlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sutd.GameObjects.GameState;
import com.sutd.PongHelpers.AssetLoader;
import com.sutd.PongHelpers.Constants;
import com.sutd.PongHelpers.Dimension;
import com.sutd.PongHelpers.InputHandler;
import java.util.Random;

/**
 * GameRenderer is responsible for rendering all the objects during the game play.
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

	int[][] balls;

	int[] player0;
	int[] player1;
	
	private TextureRegion watermelon, kiwi, orange, wait_screen, instr_screen, game_screen, paddle_top, paddle_bottom, coco;
	private GameState lastKnownState;
	private Music     chimp_long, chimp_short;

	int[] scores;
	int[] ballsType;

	double[] ballDoubles;
	int      timeLeft;
	int      countDown;
	int      tick;
	int		 ballCounter;
	
	public boolean disconnect = false;

	int 	orangeP0;
	int 	kiwiP0;
	int 	watermelonP0;

	BlockingQueue<GameState> buffer;

	public GameRenderer(BlockingQueue<GameState> buffer2, Dimension d) {
		this.d = d;
		calc = new Constants(d);
		this.buffer = buffer2;
		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, 204);
		countDown = Constants.COUNT_DOWN_SECOND;
		tick = Constants.GAME_TIME + Constants.COUNT_DOWN_SECOND;

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
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
		coco = AssetLoader.coco;
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
		else if (state == null) {
			state = lastKnownState;
			// System.out.println("Missed frame to render...");
		}
		else lastKnownState = state;

		/* Make things to render */
		balls = calc.makeBallXYs(state.getBallsData());
		player0 = calc.makePaddleXY(state.getPlayer0Data(), 0);
		player1 = calc.makePaddleXY(state.getPlayer1Data(), 1);

		scores = state.getScores();
		ballsType = state.getBallsType();
		timeLeft = state.getTimeLeft();
		
		/* Black background drawn to prevent flickering - IMPORTANT. */
		Gdx.gl.glClearColor(255, 255, 183, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		String score0 = scores[0] + "";
		String score1 = scores[1] + "";
		
		orangeP0 = state.getOrange()[0];
		kiwiP0 = state.getKiwi()[0];
		watermelonP0 = state.getWatermelon()[0];
		

		/* Keep time */
		if ((timeLeft - tick) <= -1) {
			tick--;
			System.out.println("Ticks: " + tick);
			if (countDown > -1) {
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
		
		else if (state.getStatus() == -1) {
			System.out.println("I need to disconnect");
			disconnect = true;
			batcher.end();
			return;
		}
		
		/* Game over */
		else if (timeLeft == 0) {
			
			
			
			batcher.draw(game_screen, 0, 0, 136, 204);
			batcher.draw(coco, d.width/8, d.height/10, d.width/4, d.width/4);
			
			
			/* Handle conclusion of game - whether there is a winner or not, and if the player is the winner. */
			if (scores[0] > scores[1]) font.draw(batcher, "WIN !", d.width / 2, d.height/9);
			else if (scores[0] < scores[1]) font.draw(batcher, "LOSE", d.width / 2, d.height/9);
			else font.draw(batcher, "TIE", d.width / 2, d.height/9);
			
			
			font.draw(batcher, Integer.toString(orangeP0), d.width / 2 - 48, d.height / 2 - 40);
			font.draw(batcher, Integer.toString(kiwiP0), d.width / 2 - 48, d.height / 2 - 20);
			font.draw(batcher, Integer.toString(watermelonP0), d.width / 2 - 48, d.height / 2);
			
			font.draw(batcher, "x", d.width / 2 - 25, d.height / 2 - 40);
			font.draw(batcher, "x", d.width / 2 - 25, d.height / 2 - 20);
			font.draw(batcher, "x", d.width / 2 - 25, d.height / 2);
			
			batcher.draw(orange, d.width / 2, d.height / 2 - 40, 2 * (int) calc.getBallPixelRadius()  * 7/6, 2 * (int) calc.getBallPixelRadius()  * 7/6);
			batcher.draw(kiwi, d.width / 2, d.height / 2 - 20, 2 * (int) calc.getBallPixelRadius()  * 7/6, 2 * (int) calc.getBallPixelRadius()  * 7/6);
			batcher.draw(watermelon, d.width / 2, d.height / 2 , 2 * (int) calc.getBallPixelRadius()  * 7/6, 2 * (int) calc.getBallPixelRadius()  * 7/6);
			
			/* Show final scores of both players. */
			font.draw(batcher, "=", d.width / 5, d.height / 2 + 30);
			font.draw(batcher, score0 + " : " + score1, d.width / 2 - 2 * ( score0.length() + 5), d.height / 2 + 30);
			
			/* Play again? If so, instantiate countDown and tick to original values. */
			font.draw(batcher, "AGAIN ?", 2 * d.width / 7, d.height - 40);
			countDown = Constants.AGAIN_COUNT_DOWN_SECOND;
			tick = Constants.GAME_TIME + Constants.AGAIN_COUNT_DOWN_SECOND;

			/* Play short chimp call to signal start of another new game. */
			chimp_short.play();

		}

		else {
			
			/* Game play starts */			
			if (countDown == -1) {
				batcher.draw(game_screen, 0, 0, 136, 204);
			
				/* Draw fruitballs. */
				for(int i = 0; i< balls.length; i++){
					int[] ball = balls[i];
					
					/* If type of ball = 0, draw orange as a ball. */
					if(ballsType[i] == 0){
						drawOrange(ball[0], ball[1]);
						
					/* If type of ball = 1, draw kiwi as a ball. */	
					} else if (ballsType[i] == 1){
						drawKiwi(ball[0], ball[1]);
						
					/* If type of ball = 2, draw watermelon as a ball. */
					} else if (ballsType[i] == 2){
						drawWatermelon(ball[0], ball[1]);
					}	
				}
		        
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
				if (countDown > 1) {
					batcher.draw(instr_screen, 0, 0, 136, 204);	
				}

				/* Load the words: READY? GO! */
				else if (countDown == 1) {
					batcher.draw(game_screen, 0, 0, 136, 204);
					font.draw(batcher, "READY ?", d.width / 4, d.height / 2 - 20);
					
					/* Play short chimp call to signal start of game. */
					chimp_short.play();
				}
				else if (countDown == 0) {

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