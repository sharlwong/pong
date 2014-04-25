package multipong;

import org.junit.Test;

import java.security.SecureRandom;

public class PaddleTest {

	private void pass(String methodName) {
		System.out.println("PASS: " + methodName);
	}

	@Test
	public void testFractionalPosition() throws Exception {
		Paddle paddle = new Paddle(0);
		assert paddle.getFractionalPosition() == 0.5;
		for (int i = -100; i < 1100; i++) {
			double temp = paddle.getFractionalPosition();
			paddle.setFractionalPosition((double) i / 1000);
			if (i < 0) assert paddle.getFractionalPosition() == temp;
			else if (i > 1000) assert paddle.getFractionalPosition() == temp;
			else assert paddle.getFractionalPosition() - (double) i / 1000 < 0.0000001;
		}
		paddle = new Paddle(1);
		assert paddle.getFractionalPosition() == 0.5;
		for (int i = -100; i < 1100; i++) {
			double temp = paddle.getFractionalPosition();
			paddle.setFractionalPosition((double) i / 1000);
			if (i < 0) assert paddle.getFractionalPosition() == temp;
			else if (i > 1000) assert paddle.getFractionalPosition() == temp;
			else assert paddle.getFractionalPosition() - (double) i / 1000 < 0.0000001;
		}
		pass("getFractionalPosition");
		pass("setFractionalPosition");
	}

	@Test
	public void testScore() throws Exception {
		SecureRandom random = new SecureRandom();
		Paddle paddle = new Paddle(0);
		int score = 0;
		assert paddle.getScore() == score;
		for (int i = 0; i < 999; i++) {
			int temp = random.nextInt(200);
			score += temp + 1;
			paddle.incrementScore(new Ball(Vector2D.X, Vector2D.Y, 0, temp, 1));
			assert paddle.getScore() == score;
		}
		pass("getScore");
		pass("incrementScore");
	}

	@Test
	public void testPositions() throws Exception {
		Paddle paddle = new Paddle(0);
		assert paddle.getCenter().x == 0.5;
		for (int i = 0; i < 999; i++) {
			double fraction;
			double max = Constants.WIDTH + Constants.BALL_RADIUS - (Constants.PADDLE_WIDTH / 2);
			double min = (Constants.PADDLE_WIDTH / 2) - Constants.BALL_RADIUS;
			fraction = min + (double) i / 1000 * (max - min);
			paddle.setFractionalPosition((double) i / 1000);
			assert paddle.getCenter().x == fraction;
			assert paddle.getXY()[0] == fraction;
			assert paddle.getXY()[1] == paddle.getCenter().y;
		}
		pass("getCenter");
		pass("getXY");
	}

	@Test
	public void testCollisionCheck() throws Exception {

	}

	@Test
	public void testBounce() throws Exception {

	}
}
