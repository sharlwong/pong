package multipong;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by avery_000 on 31-Mar-14.
 */
public class GameWorld {
	public long elapsedTimeMillis;
	private List<Ball> balls;
	private Paddle player0;
	private Paddle player1;
	private SecureRandom random;
	private long injectBalls = 0;
	private boolean init = true;

	public GameWorld() {
		this.elapsedTimeMillis = 0;
		this.player0 = new Paddle(0);
		this.player1 = new Paddle(1);
		this.balls = new ArrayList<Ball>();
		random = new SecureRandom();
		random.setSeed(1234567890);
	}

	public void exit() {
		System.out.println("GAME OVER");
		System.out.println("Player 0: " + player0.getScore());
		System.out.println("Player 1: " + player1.getScore());
		System.out.println("Done!");
		System.exit(0);
	}

	public double[][] getState() {
		Vector2D temp;
		int num;
		double[][] out;
		synchronized (balls) {
			num = balls.size();
			out = new double[num+3][2];
			for (int i = 0; i < num; i++) {
				temp = balls.get(i).getCurrentPosition();
				out[i][0] = temp.x;
				out[i][1] = temp.y;
			}
		}
		temp = player0.getCenter();
		out[num] = new double[] {temp.x, temp.y};
		temp = player1.getCenter();
		out[num + 1] = new double[] {temp.x, temp.y};
		out[num+2]= new double[] {player0.getScore(), player1.getScore()};
		return out;
	}

	public void setInjectBalls() {
		injectBalls = elapsedTimeMillis + 100;
		injectRandomBall();
	}

	public void stopInjectBalls() {
		injectBalls = 0;
	}

	private void injectRandomBall() {
		Vector2D position = new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2);
		double speed1 = random.nextDouble() - 0.5;
		double speed2 = random.nextDouble() - 0.5;
		Vector2D speed;
		if (Math.abs(speed1) < Math.abs(speed2)) speed = new Vector2D(speed1, speed2);
		else speed = new Vector2D(speed2, speed1);
		synchronized (balls) {
			balls.add(new Ball(position, speed, elapsedTimeMillis, (random.nextInt())));
		}
	}

	public void updateDeltaTime(long deltaMillis) {
		elapsedTimeMillis += deltaMillis;
		if (elapsedTimeMillis > Constants.START_GAME_DELAY && init) {
			init = false;
			injectRandomBall();
		}
		if (injectBalls > 0 && injectBalls < elapsedTimeMillis) injectRandomBall();
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
					if (b.getCurrentPosition().y < 0) player1.incrementScore();
					if (b.getCurrentPosition().y > Constants.HEIGHT) player0.incrementScore();
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


