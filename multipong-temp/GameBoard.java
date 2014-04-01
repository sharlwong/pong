package multipong;

import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by avery_000 on 31-Mar-14.
 */
public class GameBoard {
	private List<Ball> balls;
	private long elapsedTimeMillis;
	private int heightPixels;
	private InputHandler inputHandler;
	private Paddle player0;
	private Paddle player1;
	private SecureRandom random;
	private int widthPixels;

	public GameBoard(Dimension sizePixels){
		this.heightPixels = sizePixels.height;
		this.widthPixels = sizePixels.width;
		this.elapsedTimeMillis = 0;
		this.player0 = new Paddle(0);
		this.player1 = new Paddle(1);
		this.balls = new ArrayList<Ball>();
		random = new SecureRandom();
		random.setSeed(1234567890);
		inputHandler = new InputHandler(player0, player1, this);
	}

	public void exit() {
		System.out.println("GAME OVER");
		System.out.println("Player 0: " + player0.getScore());
		System.out.println("Player 1: " + player1.getScore());
		System.out.println("Done!");
		System.exit(0);
	}

	public int[][] getBallXYs() {
		int[][] out = new int[balls.size()][2];
		for (int i = 0; i < balls.size(); i++) {
			out[i][0] = (int) ((balls.get(i).getCurrentPosition().x + Assets.BALL_RADIUS) / Assets.DISPLAY_WIDTH) * widthPixels;
			out[i][1] = (int) ((balls.get(i).getCurrentPosition().y + Assets.BALL_RADIUS + Assets.PADDLE_EFFECTIVE_DEPTH) / (Assets.DISPLAY_HEIGHT + Assets.PADDLE_EFFECTIVE_DEPTH)) * heightPixels;
		}
		return out;
	}

	public int[] getTopPaddleXY(){
		int x = (int) player1.getCenter().x * widthPixels;
		int y = (int) (Assets.DISPLAY_HEIGHT + Assets.PADDLE_EFFECTIVE_DEPTH/2) * widthPixels;
		return new int[]{x,y};
	}

	public int[] getBottomPaddleXY(){
		int x = (int) player0.getCenter().x * widthPixels;
		int y = (int) (0- Assets.PADDLE_EFFECTIVE_DEPTH/2) * widthPixels;
		return new int[]{x,y};
	}

	public void injectRandomBall() {
		Vector2D position = new Vector2D(Assets.WIDTH / 2, Assets.HEIGHT / 2);
		Vector2D speed = new Vector2D(random.nextDouble() - 0.5, random.nextDouble() - 0.5);
		balls.add(new Ball(position, speed, elapsedTimeMillis));
	}

	public void updateDeltaTime(long deltaMillis) {
		elapsedTimeMillis += deltaMillis;
		player0.updateDeltaTime(deltaMillis);
		player1.updateDeltaTime(deltaMillis);
		for (Ball b : balls) {
			b.updateCurrentTime(elapsedTimeMillis);
			if (player1.collisionCheck(b)) {
				balls.add(player1.bounce(b, elapsedTimeMillis));
				b.stop();
				balls.remove(b);
			}
			if (player0.collisionCheck(b)) {
				balls.add(player0.bounce(b, elapsedTimeMillis));
				b.stop();
				balls.remove(b);
			}
			if (!b.inGame()) {
				b.stop();
				balls.remove(b);
				if (b.getCurrentPosition().y < 0) player1.incrementScore();
				if (b.getCurrentPosition().y > Assets.HEIGHT) player0.incrementScore();
			}
		}

	}
}
