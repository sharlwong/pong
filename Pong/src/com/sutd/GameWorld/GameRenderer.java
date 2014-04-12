package com.sutd.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.sutd.GameObjects.GameState;
import com.sutd.GameObjects.Paddle;
import com.sutd.PongHelpers.AssetLoader;
import com.sutd.PongHelpers.Constants;
import com.sutd.PongHelpers.InputHandler;

import java.util.concurrent.BlockingQueue;

public class GameRenderer {

	private OrthographicCamera cam;
	private ShapeRenderer      shapeRenderer;
	private SpriteBatch        batcher;
	private Paddle             player_paddle;
	private TextureRegion      octopusSmile, fishCake, salmonSushi, riceCracker;
	private GameState lastKnownState;

	Dimension    d;
	InputHandler inputHandler;
	Constants    calc;

	int[][]  balls;
	int[]    player0;
	int[]    player1;
	int[]    scores;
	int[]    ballTypes;
	double[] ballDoubles;

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
		octopusSmile = AssetLoader.octopusSmile;
		fishCake = AssetLoader.fishCake;
		riceCracker = AssetLoader.riceCracker;
		salmonSushi = AssetLoader.salmonSushi;
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
			System.out.println("Missed frame to render...");
		}
		else lastKnownState = state;

		/* make things to render */
		balls = calc.makeBallXYs(state.getBallsData());
		player0 = calc.makePaddleXY(state.getPlayer0Data(), 0);
		player1 = calc.makePaddleXY(state.getPlayer1Data(), 1);
		scores = state.getScores();
		ballDoubles = state.getSpareVar();
		ballTypes = state.getBallsType();

		/* black background drawn to prevent flickering */
		Gdx.gl.glClearColor(255, 255, 183, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		String score0 = scores[0] + "";
		String score1 = scores[1] + "";

		batcher.begin();
		//AssetLoader.shadow.draw(batcher, "10", 100, 100);
		AssetLoader.font.draw(batcher, "" + score1, d.width - 20 - (3 * score0.length()), d.height / 2 - 20);
		AssetLoader.font.draw(batcher, "" + score0, d.width - 20 - (3 * score0.length()), d.height / 2);

        /*
         * Draw octopus as a ball.
         */

		// for (int[] ball : balls) drawOctopus(ball[0], ball[1]);

        
        /*
         * Draw fish cake as a ball.
         */

		//        for (int[] ball : balls) drawFishCake(ball[0], ball[1]);

        /*
         * Draw rice cracker as a ball.
         */

		for (int[] ball : balls) drawRiceCracker(ball[0], ball[1]);
        
        /*
         * Draw salmon sushi as a ball.
         */

		//        for (int[] ball : balls) drawSalmonSushi(ball[0], ball[1]);

		// End SpriteBatch
		batcher.end();

        /* Draw normal balls: For testing.*/
		// for (int[] ball : balls) drawBall(ball[0], ball[1]);

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
	}

	private void drawRiceCracker(int centerX, int centerY) {
		int radius = (int) calc.getBallPixelRadius();

		// The octopus needs transparency, so we enable that.
		batcher.enableBlending();
		batcher.draw(riceCracker, centerX - radius, centerY - radius, 2 * radius, 2 * radius);
	}

	private void drawSalmonSushi(int centerX, int centerY) {
		int radius = (int) calc.getBallPixelRadius();

		// The octopus needs transparency, so we enable that.
		batcher.enableBlending();
		batcher.draw(salmonSushi, centerX - radius, centerY - radius, 2 * radius, 2 * radius);
	}

	private void drawFishCake(int centerX, int centerY) {
		int radius = (int) calc.getBallPixelRadius();

		// The octopus needs transparency, so we enable that.
		batcher.enableBlending();
		batcher.draw(fishCake, centerX - radius, centerY - radius, 2 * radius, 2 * radius);
	}

	// for draw function, the x and y parameters indicates the bottom left corner
	private void drawOctopus(int centerX, int centerY) {
		int radius = (int) calc.getBallPixelRadius();

		// The octopus needs transparency, so we enable that.
		batcher.enableBlending();
		batcher.draw(octopusSmile, centerX - radius, centerY - radius, 2 * radius, 2 * radius);
	}

	private void drawTwoOctopus(int centerX, int centerY, float runTime) {
		int radius = (int) calc.getBallPixelRadius();
		//		shapeRenderer.circle(centerX - radius, centerY - radius, 2 * radius);

		// The octopus needs transparency, so we enable that.
		batcher.enableBlending();

		// Draw bird at its coordinates. Retrieve the Animation object from AssetLoader
		// Pass in the runTime variable to get the current frame.
		batcher.draw(AssetLoader.octopusAnimation.getKeyFrame(runTime), centerX - radius, centerY - radius, 2 * radius, 2 * radius);
	}

	private void drawPaddle(int centerX, int centerY) {
		int width = (int) calc.getPaddlePixelWidth();
		int height = (int) calc.getPaddlePixelDepth();
		shapeRenderer.rect(centerX - width / 2, centerY - height / 2, width, height);
	}
}