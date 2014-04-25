package com.sutd.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.sutd.GameObjects.Ball;
import com.sutd.GameObjects.GameState;
import com.sutd.GameObjects.Paddle;
import com.sutd.PongHelpers.AssetLoader;
import com.sutd.PongHelpers.Constants;
import com.sutd.PongHelpers.Vector2D;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * GameWorld is responsible to handle and update balls, paddles and other objects
 * during game play. Mainly, it generates random balls, counts game time and keeps
 * updating to check collision and so on. It also checks whether players choose to
 * restart the game after one round ends.
 */
public class GameWorld {
	public        long         elapsedTimeMillis;
	private final List<Ball>   balls;
	private       Paddle       player0;
	private       Paddle       player1;
	private       SecureRandom random;
	private       long         injectBalls;
	private       int          init;
	public        boolean      ready;
	public        boolean      disconnect;
	public        boolean      gameover;
	public        int          ticktock;
	public        int          timeLimit; //maximum time for each round
	private       Music        bounce;

	/* simulation variable */
	private final static double frameDrop = 0;

	public GameWorld() {
		init = Integer.MAX_VALUE;
		elapsedTimeMillis = 0;
		player0 = new Paddle(0);
		player1 = new Paddle(1);
		balls = new ArrayList<Ball>();
		random = new SecureRandom();
		random.setSeed(1234567890);
		injectBalls = 0;
		ticktock = 0;
		timeLimit = Constants.GAME_TIME + Constants.COUNT_DOWN_SECOND;
		ready = false;
		disconnect = false;
		gameover = false;
		bounce = AssetLoader.bounce;
		System.out.println("Game initialized, please wait for start...");
	}

	/**
	 * @return current GameState
	 */
	public GameState getGameState() {

		/* simulate dropped frames */
		if (random.nextDouble() < frameDrop) return null;

		/* temporary vars */
		GameState out = new GameState();
		Vector2D temp;
		int[] ballsType = new int[balls.size()];
		double[][] ballsData = new double[balls.size()][2];

		/* get ball data */
		synchronized (balls) {
			for (int i = 0; i < balls.size(); i++) {
				temp = balls.get(i).getCurrentPosition();
				ballsData[i][0] = temp.x;
				ballsData[i][1] = temp.y;
				ballsType[i] = balls.get(i).getType();
			}
		}

		/* set ball data */
		out.setBallsData(ballsData);
		out.setBallsType(ballsType);

		/* set game status */
		if(disconnect) out.setStatus(-1);
		else out.setStatus(ready ? 1 : 0);

		/* set player data */
		temp = player0.getCenter();
		out.setPlayer0Data(new double[]{temp.x, temp.y});
		temp = player1.getCenter();
		out.setPlayer1Data(new double[]{temp.x, temp.y});

		/* set scores */
		out.setScores(new int[]{player0.getScore(), player1.getScore()});
		out.setTimeLeft(getSecondLeft());
		
		out.setOrange(player0.getOrange(), player1.getOrange());
		out.setKiwi(player0.getKiwi(), player1.getKiwi());
		out.setWatermelon(player0.getWatermelon(), player1.getWatermelon());

		/* done */
		return out;
	}

	/**
	 * must not be synchronized with balls when used elsewhere because balls are synchronized inside
	 */
	private void injectRandomBall() {

		/* first run stuff */
		if (init == 0) System.out.println("Start!");
		else System.out.println("inject!!!");
		init = -1;

		/* ball type and other data */
		double randomValue = random.nextDouble() * 3;
		int ballType = (int) randomValue;

		/* starting position */
		Vector2D position = new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2);

		/* randomize starting velocity within 45 degrees */
		double speed1 = random.nextDouble() - 0.5;
		double speed2 = random.nextDouble() - 0.5;
		Vector2D speed;
		if (Math.abs(speed1) < Math.abs(speed2)) speed = new Vector2D(speed1, speed2);
		else speed = new Vector2D(speed2, speed1);
		double speedMultiplier = random.nextDouble() * (Constants.BALL_MAX_SPEED - Constants.BALL_MIN_SPEED) + (Constants.BALL_MIN_SPEED);

		/* make new ball */
		synchronized (balls) { balls.add(new Ball(position, speed, elapsedTimeMillis, ballType, speedMultiplier));}
	}

	/**
	 * prerequisite: input integer must be either 1 or 0
	 *
	 * @param p
	 * @return
	 */
	public Paddle getPaddle(int p) {
		return (p == 0) ? player0 : player1;
	}

	/**
	 * 1. check the other player is connected or not
	 * 2. check the game is counting down or not
	 * 3. check the game started or not, inject balls at appropriate time
	 * 4. check ball collision
	 * 5. check time up
	 *
	 * @param delta time interval
	 */
	public void update(float delta) {
		updateDeltaTime((long) delta);
	}

	public void updateDeltaTime(long deltaMillis) {
		// set time for each round
		if (ticktock >= timeLimit) {
			checkrestart();
			return;
		}

		/* checks whether game is ready to start */
		if (!ready) return;

		/* increment time */
		long temp = elapsedTimeMillis;
		elapsedTimeMillis += deltaMillis;
		if ((int) elapsedTimeMillis / 1000 > (int) temp / 1000) ticktock++;

		/* countdown */
		if (ticktock <= timeLimit - Constants.GAME_TIME) {
			if (init != timeLimit - ticktock - Constants.GAME_TIME) {
				init = timeLimit - ticktock - Constants.GAME_TIME;
				System.out.println(init);
			}
			return;
		}

		/* conditions under which a ball should be injected */
		boolean firstBallIn = init > 0 && (elapsedTimeMillis > Constants.START_GAME_DELAY) && (balls.size() == 0);
		boolean ballInjectionIsOn = (injectBalls > 0) && (injectBalls < elapsedTimeMillis);
		boolean tickInject = (int) elapsedTimeMillis / Constants.BALL_FREQUENCY > (int) temp / Constants.BALL_FREQUENCY;
		tickInject = tickInject && (balls.size() < 7);
		/* inject ball */
		if (firstBallIn || ballInjectionIsOn || tickInject) injectRandomBall();

		/* work with balls */
		List<Ball> removeThese = new ArrayList<Ball>();
		List<Ball> addThese = new ArrayList<Ball>();
		synchronized (balls) {
			for (Ball b : balls) {
				b.updateCurrentTime(elapsedTimeMillis);
				if (player1.collisionCheck(b)) {
					addThese.add(player1.bounce(b, elapsedTimeMillis));
					removeThese.add(b);
					bounce.play();
				}
				if (player0.collisionCheck(b)) {
					addThese.add(player0.bounce(b, elapsedTimeMillis));
					removeThese.add(b);
					bounce.play();
				}
				if (!b.inGame()) {
					removeThese.add(b);
					if (b.getCurrentPosition().y < 0) player1.incrementScore(b);
					if (b.getCurrentPosition().y > Constants.HEIGHT) player0.incrementScore(b);
				}
			}

			for (Ball b : removeThese) balls.remove(b);
			for (Ball b : addThese) balls.add(b);
			if (balls.size() == 0) injectRandomBall();
		}
	}

	/**
	 * @return how much time the game remains
	 */
	public int getSecondLeft() {
		return timeLimit - ticktock;
	}

	/**
	 * check whether user pressed the "Again ?" button
	 */
	public void checkrestart() {
		if (Gdx.input.justTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			//System.out.println(x+" "+y+" "+Gdx.graphics.getWidth()/2+" "+Gdx.graphics.getHeight());

			if (x > Gdx.graphics.getWidth() / 7 && x < 6 * Gdx.graphics.getWidth() / 7 && y > Gdx.graphics.getHeight() * ((float) 164 / 204) && y < Gdx.graphics.getHeight() * ((float) 184 / 204)) {
				// restart
				// initialize objects inside game world
				ticktock = 0;
				timeLimit = Constants.GAME_TIME + Constants.AGAIN_COUNT_DOWN_SECOND;
				elapsedTimeMillis = 0;
				ready = true;
				player0 = new Paddle(0);
				player1 = new Paddle(1);
				gameover = false;
				balls.clear();
			}
		}
	}
}
