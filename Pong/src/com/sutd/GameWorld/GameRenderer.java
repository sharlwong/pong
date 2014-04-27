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

/**
 * GameRenderer is responsible for rendering all the objects during the game play.
 * It calls upon values from Constants and assets from AssetLoader to render.
 * We employ two rendering classes in LibGDX engine: ShapeRenderer and SpriteBatch.
 * ShapeRenderer is used to render in-built shapes in LibGDX, while SpriteBatch is
<<<<<<< HEAD
 * used to render images.
 * 
 * ***/


public class GameRenderer {

	/* assets */
	private OrthographicCamera cam;
	private ShapeRenderer      shapeRenderer;
	private SpriteBatch        batcher;
	private BitmapFont         font;
	private GameState          lastKnownState;
	private Music              chimp_long, chimp_short;

	private TextureRegion watermelon, kiwi, orange, wait_screen, story_screen, fruit_pt_screen, game_screen, paddle_top, paddle_bottom, coco;

	/* working items */ Dimension d;
	InputHandler             inputHandler;
	Constants                calc;
	BlockingQueue<GameState> buffer;

	/* variables */ int[][] balls;
	int[]    player0;
	int[]    player1;
	int[]    scores;
	int[]    ballsType;
	double[] ballDoubles;

	/* more variables */ int timeLeft;
	int countDown;
	int tick;
	int ballCounter;
	int orangeP0;
	int kiwiP0;
	int watermelonP0;

	/* is it dead yet */
	public boolean disconnect = false;

	public GameRenderer(BlockingQueue<GameState> buffer2, Dimension screenRes) {
		this.d = screenRes;
		calc = new Constants(screenRes);
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

	/** Initialize assets needed for this class. ** */

	private void initAssets() {
		/* imagery */
		chimp_long = AssetLoader.chimp_long;
		chimp_short = AssetLoader.chimp_short;
		coco = AssetLoader.coco;
		font = AssetLoader.font;
		/* screens */
		fruit_pt_screen = AssetLoader.fruit_pt_screen;
		game_screen = AssetLoader.game_screen;
		story_screen = AssetLoader.story_screen;
		wait_screen = AssetLoader.wait_screen;
		/* fruits */
		kiwi = AssetLoader.kiwi;
		orange = AssetLoader.orange;
		watermelon = AssetLoader.watermelon;
		/* paddles */
		paddle_bottom = AssetLoader.paddle_bottom;
		paddle_top = AssetLoader.paddle_top;
	}

	public void render(float runTime) {
		// This runTime keeps accumulating, can be used by Ball class directly

		/* Get state */
		GameState state = buffer.poll();

		/* Check state and store it */
		if (state == null && lastKnownState == null) return;
		else if (state == null) state = lastKnownState;
		else lastKnownState = state;

		/* Make things to render */
		balls = calc.makeBallXYs(state.getBallsData());
		player0 = calc.makePaddleXY(state.getPlayer0Data(), 0);
		player1 = calc.makePaddleXY(state.getPlayer1Data(), 1);

		ballsType = state.getBallsType();
		timeLeft = state.getTimeLeft();

		scores = state.getScores();
		String score0 = scores[0] + "";
		String score1 = scores[1] + "";

		orangeP0 = state.getOrange()[0];
		kiwiP0 = state.getKiwi()[0];
		watermelonP0 = state.getWatermelon()[0];

		/* Black background drawn to prevent flickering - IMPORTANT. */
		Gdx.gl.glClearColor(255, 255, 183, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

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

		if (state.getStatus() == 0) batcher.draw(wait_screen, 0, 0, 136, 204);

		else if (state.getStatus() == -1) {
			System.out.println("I need to disconnect");
			disconnect = true;
			batcher.end();
			return;
		}
		
		/* Game over */
		else if (timeLeft == 0) {

			batcher.draw(game_screen, 0, 0, 136, 204);
			batcher.draw(coco, d.width / 8, d.height / 10, d.width / 4, d.width / 4);
			
			
			/* Handle conclusion of game - whether there is a winner or not, and if the player is the winner. */
			if (scores[0] > scores[1]) font.draw(batcher, "WIN !", d.width / 2, d.height / 9);
			else if (scores[0] < scores[1]) font.draw(batcher, "LOSE", d.width / 2, d.height / 9);
			else font.draw(batcher, "TIE", d.width / 2, d.height / 9);

			font.draw(batcher, Integer.toString(orangeP0), d.width / 2 - 48, d.height / 2 - 40);
			font.draw(batcher, Integer.toString(kiwiP0), d.width / 2 - 48, d.height / 2 - 20);
			font.draw(batcher, Integer.toString(watermelonP0), d.width / 2 - 48, d.height / 2);

			font.draw(batcher, "x", d.width / 2 - 25, d.height / 2 - 40);
			font.draw(batcher, "x", d.width / 2 - 25, d.height / 2 - 20);
			font.draw(batcher, "x", d.width / 2 - 25, d.height / 2);

			batcher.draw(orange, d.width / 2, d.height / 2 - 40, 2 * (int) calc.getBallPixelRadius() * 7 / 6, 2 * (int) calc.getBallPixelRadius() * 7 / 6);
			batcher.draw(kiwi, d.width / 2, d.height / 2 - 20, 2 * (int) calc.getBallPixelRadius() * 7 / 6, 2 * (int) calc.getBallPixelRadius() * 7 / 6);
			batcher.draw(watermelon, d.width / 2, d.height / 2, 2 * (int) calc.getBallPixelRadius() * 7 / 6, 2 * (int) calc.getBallPixelRadius() * 7 / 6);
			
			/* Show final scores of both players. */
			font.draw(batcher, "=", d.width / 5, d.height / 2 + 30);
			font.draw(batcher, score0 + " : " + score1, d.width / 2 - 2 * (score0.length() + 5), d.height / 2 + 30);
			
			/* Play again? If so, instantiate countDown and tick to original values. */
			font.draw(batcher, "AGAIN ?", 2 * d.width / 7, d.height - 40);
			countDown = Constants.AGAIN_COUNT_DOWN_SECOND - 1;
			tick = Constants.GAME_TIME + Constants.AGAIN_COUNT_DOWN_SECOND - 1;

			/* Play short chimp call to signal start of another new game. */
			chimp_short.play();
		}

		else {
			
			/* Game play starts */
			if (countDown == -1) {
				batcher.draw(game_screen, 0, 0, 136, 204);
			
				/* Draw fruitballs. */
				for (int i = 0; i < balls.length; i++) {
					int[] ball = balls[i];
					if (ballsType[i] == 0) drawOrange(ball[0], ball[1]);
					if (ballsType[i] == 1) drawKiwi(ball[0], ball[1]);
					if (ballsType[i] == 2) drawWatermelon(ball[0], ball[1]);
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
				
				/* Load story screen. */
				if (countDown >= 8) {
					batcher.draw(story_screen, 0, 0, 136, 204);
					System.out.println("Story screen");
				}

				/* Load fruit points screen. */
				if (countDown >= 2 && countDown < 8) {
					batcher.draw(fruit_pt_screen, 0, 0, 136, 204);
					System.out.println("Fruit point screen");
					
					/* Draw bottom paddle. */
					drawBottomPaddle(player0[0], player0[1]);
				}

				/* Load the words: READY? GO! */
				else if (countDown == 1) {
					batcher.draw(game_screen, 0, 0, 136, 204);
					font.draw(batcher, "READY ?", d.width / 4, d.height / 2 - 20);
					
					/* Play short chimp call to signal start of game. */
					chimp_short.play();
					
					/* Draw bottom paddle. */
					drawBottomPaddle(player0[0], player0[1]);
				}
				else if (countDown == 0) {

					batcher.draw(game_screen, 0, 0, 136, 204);
					font.draw(batcher, "GO !", 2 * d.width / 5, d.height / 2 - 20);
					
					/* Draw bottom paddle. */
					drawBottomPaddle(player0[0], player0[1]);
				}
			}
		}
		batcher.end();
	}

	/**
	 * Use SpriteBatch to draw a watermelon as a ball.
	 *
	 * @param int centerX, x-coordinate of center point of ball
	 * @param int centerY, y-coordinate of center point of ball
	 */
	private void drawWatermelon(int centerX, int centerY) {
		int radius = (int) calc.getBallPixelRadius();

		// The watermelon needs transparency, so we enable that.
		batcher.enableBlending();
		batcher.draw(watermelon, centerX - radius, centerY - radius, 2 * radius * 7 / 6, 2 * radius * 7 / 6);
	}

	/**
	 * Use SpriteBatch to draw an orange as a ball.
	 *
	 * @param int centerX, x-coordinate of center point of ball
	 * @param int centerY, y-coordinate of center point of ball
	 */
	private void drawOrange(int centerX, int centerY) {
		int radius = (int) calc.getBallPixelRadius();

		// The orange needs transparency, so we enable that.
		batcher.enableBlending();
		batcher.draw(orange, centerX - radius, centerY - radius, 2 * radius * 7 / 6, 2 * radius * 7 / 6);
	}

	/**
	 * Use SpriteBatch to draw a kiwi as a ball.
	 *
	 * @param int centerX, x-coordinate of center point of ball
	 * @param int centerY, y-coordinate of center point of ball
	 */
	private void drawKiwi(int centerX, int centerY) {
		int radius = (int) calc.getBallPixelRadius();

		// The kiwi needs transparency, so we enable that.
		batcher.enableBlending();

		batcher.draw(kiwi, centerX - radius, centerY - radius, 2 * radius * 7 / 6, 2 * radius * 7 / 6);
	}

	/**
	 * Use SpriteBatch to draw an inverted banana as a top paddle.
	 *
	 * @param int centerX, x-coordinate of center point of paddle
	 * @param int centerY, y-coordinate of center point of paddle
	 */

	private void drawTopPaddle(int centerX, int centerY) {
		int width = (int) calc.getPaddlePixelWidth();
		int height = (int) calc.getPaddlePixelDepth();

		batcher.enableBlending();
        batcher.draw(paddle_top, centerX - width / 2, centerY - height / 2, width, height);
	
	}

	/**
	 * Use SpriteBatch to draw a banana as a bottom paddle.
	 *
	 * @param int centerX, x-coordinate of center point of paddle
	 * @param int centerY, y-coordinate of center point of paddle
	 */

	private void drawBottomPaddle(int centerX, int centerY) {
		int width = (int) calc.getPaddlePixelWidth();
		int height = (int) calc.getPaddlePixelDepth();

		batcher.enableBlending();

        batcher.draw(paddle_bottom, centerX - width / 2, centerY - height / 2, width, height);

	}

}