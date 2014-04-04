package multipong;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by avery_000 on 31-Mar-14.
 */
public class GameBoard {
	public long elapsedTimeMillis;
	public Constants calc;
	private List<Ball> balls;
	private int heightPixels;
	private InputHandler inputHandler;
	private Paddle player0;
	private Paddle player1;
	private SecureRandom random;
	private int widthPixels;
	private Dimension dim;
	private boolean injectBalls = false;

	public GameBoard(Dimension sizePixels) {
		this.calc = new Constants(sizePixels);
		this.heightPixels = sizePixels.height;
		this.widthPixels = sizePixels.width;
		this.elapsedTimeMillis = 0;
		this.player0 = new Paddle(0);
		this.player1 = new Paddle(1);
		this.balls = new ArrayList<Ball>();
		random = new SecureRandom();
		random.setSeed(1234567890);
		inputHandler = new InputHandler(player0, player1, this);
		dim = sizePixels;

		injectRandomBall();
	}

	public void exit() {
		System.out.println("GAME OVER");
		System.out.println("Player 0: " + player0.getScore());
		System.out.println("Player 1: " + player1.getScore());
		System.out.println("Done!");
		System.exit(0);
	}

	public int[][] getBallXYs() {
		synchronized (balls) {
			int[][] out = new int[balls.size()][2];
			for (int i = 0; i < balls.size(); i++) {
				Dimension temp = calc.translateBallReferenceFrame(balls.get(i).getCurrentPosition());
				out[i][0] = temp.width;
				out[i][1] = temp.height;
			}
			return out;
		}
	}

	public int[] getBottomPaddleXY() {
		int[] out = new int[2];
		Dimension temp = calc.translateBallReferenceFrame(player0.getCenter());
		out[0] = temp.width;
		out[1] = temp.height + (int) calc.getBallPixelRadius();
		return out;
	}

	public int[] getTopPaddleXY() {
		int[] out = new int[2];
		Dimension temp = calc.translateBallReferenceFrame(player1.getCenter());
		out[0] = temp.width;
		out[1] = temp.height - (int) calc.getBallPixelRadius();
		return out;
	}

	public void setInjectBalls() {
		injectBalls = true;
		injectRandomBall();
	}

	public void stopInjectBalls() {
		injectBalls = false;
	}

	public void injectRandomBall() {
		Vector2D position = new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2);
		double speed1 = random.nextDouble() - 0.5;
		double speed2 = random.nextDouble() - 0.5;
		Vector2D speed;
		if (Math.abs(speed1) < Math.abs(speed2)) speed = new Vector2D(speed1, speed2);
		else speed = new Vector2D(speed2, speed1);
		synchronized (balls) {
			balls.add(new Ball(position, speed, elapsedTimeMillis));
		}
	}

	public int[] getScores() {
		int[] out = new int[2];
		out[0] = player0.getScore();
		out[1] = player1.getScore();
		return out;
	}

	public synchronized void updateDeltaTime(long deltaMillis) {

		if (injectBalls) injectRandomBall();

		elapsedTimeMillis += deltaMillis;
		player0.updateDeltaTime(deltaMillis);
		player1.updateDeltaTime(deltaMillis);
		List<Ball> removeThese = new ArrayList<Ball>();
		List<Ball> addThese = new ArrayList<Ball>();
		synchronized (balls) {
			for (Ball b : balls) {
				b.updateCurrentTime(elapsedTimeMillis);
				if (player1.collisionCheck(b)) {
					addThese.add(player1.bounce(b, elapsedTimeMillis));
					//				b.kill();
					removeThese.add(b);
				}
				if (player0.collisionCheck(b)) {
					addThese.add(player0.bounce(b, elapsedTimeMillis));
					//				b.kill();
					removeThese.add(b);
				}
				if (!b.inGame()) {
					//				b.kill();
					removeThese.add(b);
					if (b.getCurrentPosition().y < 0) player1.incrementScore();
					if (b.getCurrentPosition().y > Constants.HEIGHT) player0.incrementScore();
				}
			}

			for (Ball b : removeThese) balls.remove(b);
			for (Ball b : addThese) balls.add(b);
		}
	}

	public void keyDown(KeyEvent e) {
		inputHandler.keyDown(e);
	}

	public void keyUp(KeyEvent e) {
		inputHandler.keyUp(e);
	}
}


