package multipong;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class GameWorld {
	public        long         elapsedTimeMillis;
	private final List<Ball>   balls;
	private       Paddle       player0;
	private       Paddle       player1;
	private       SecureRandom random;
	private       long         injectBalls;
	private       boolean      init;
	public        boolean      ready;

	/* simulation variable */
	private final static double frameDrop = 0.0;

	public GameWorld() {
		elapsedTimeMillis = 0;
		player0 = new Paddle(0);
		player1 = new Paddle(1);
		balls = new ArrayList<Ball>();
		random = new SecureRandom();
		random.setSeed(1234567890);
		injectBalls = 0;
		init = true;
		ready = true;
		System.out.println("Game initialized, please wait for start...");
	}

	public void exit() {
		System.out.println("GAME OVER");
		System.out.println("Player 0: " + player0.getScore());
		System.out.println("Player 1: " + player1.getScore());
		System.out.println("Done!");
		System.exit(0);
	}

	public GameState getGameState() {

		/* simulate dropped frames */
		if (random.nextDouble() < frameDrop) return null;

		/* temporary vars */
		GameState out = new GameState();
		Vector2D temp;
		double[] other = new double[balls.size()];
		int[] ballsType = new int[balls.size()];
		double[][] ballsData = new double[balls.size()][2];

		/* get ball data */
		synchronized (balls) {
			for (int i = 0; i < balls.size(); i++) {
				temp = balls.get(i).getCurrentPosition();
				ballsData[i][0] = temp.x;
				ballsData[i][1] = temp.y;
				ballsType[i] = balls.get(i).getType();
				other[i] = balls.get(i).getUnusedVariable();
			}
		}

		/* set ball data */
		out.setBallsData(ballsData);
		out.setBallsType(ballsType);
		out.setSpareVar(other);

		/* set game status */
		out.setStatus(ready ? 1 : 0);

		/* set player data */
		temp = player0.getCenter();
		out.setPlayer0Data(new double[]{temp.x, temp.y});
		temp = player1.getCenter();
		out.setPlayer1Data(new double[]{temp.x, temp.y});

		/* set scores */
		out.setScores(new int[]{player0.getScore(), player1.getScore()});

		/* done */
		return out;
	}

	public void setInjectBalls() {
		injectBalls = elapsedTimeMillis + 100;
		injectRandomBall();
	}

	public void stopInjectBalls() {
		injectBalls = 0;
	}

	/**
	 * must not be synced with balls when used elsewhere because synced inside
	 */
	private void injectRandomBall() {

		/* first run stuff */
		if (init) System.out.println("Start!");
		init = false;

		/* ball type and other data */
		double randomValue = random.nextDouble() * 14;
		int ballType = (int) randomValue;

		/* starting position */
		Vector2D position = new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2);

		/* randomize starting velocity within 45° */
		double speed1 = random.nextDouble() - 0.5;
		double speed2 = random.nextDouble() - 0.5;
		Vector2D speed;
		if (Math.abs(speed1) < Math.abs(speed2)) speed = new Vector2D(speed1, speed2);
		else speed = new Vector2D(speed2, speed1);

		/* make new ball */
		synchronized (balls) { balls.add(new Ball(position, speed, elapsedTimeMillis, randomValue, ballType));}
	}

	public void updateDeltaTime(long deltaMillis) {
		/* checks whether game is ready to start */
		if (!ready) return;

		/* increment time */
		elapsedTimeMillis += deltaMillis;

		/* conditions under which a ball should be injected */
		boolean firstBallIn = init && (elapsedTimeMillis > Constants.START_GAME_DELAY) && (balls.size() == 0);
		boolean ballInjectionIsOn = (injectBalls > 0) && (injectBalls < elapsedTimeMillis);

		/* inject ball */
		if (firstBallIn || ballInjectionIsOn) injectRandomBall();

		/* work with balls */
		List<Ball> removeThese = new ArrayList<Ball>();
		List<Ball> addThese = new ArrayList<Ball>();
		synchronized (balls) {
			for (Ball b : balls) {
				b.updateCurrentTime(elapsedTimeMillis);
				if (player1.collisionCheck(b)) {
					addThese.add(player1.bounce(b, elapsedTimeMillis));
					removeThese.add(b);
				}
				if (player0.collisionCheck(b)) {
					addThese.add(player0.bounce(b, elapsedTimeMillis));
					removeThese.add(b);
				}
				if (!b.inGame()) {
					removeThese.add(b);
					if (b.getCurrentPosition().y < 0) player1.incrementScore(b);
					if (b.getCurrentPosition().y > Constants.HEIGHT) player0.incrementScore(b);
				}
			}

			for (Ball b : removeThese) balls.remove(b);
			for (Ball b : addThese) balls.add(b);
		}
	}

	public void setP0fractional(double pos) {
		player0.setFractionalPosition(pos);
	}

	public void setP1fractional(double pos) {
		player1.setFractionalPosition(pos);
	}
}
