package multipong;

import org.junit.Test;

public class BallTest {

	private void pass(String methodName) {
		System.out.println("PASS: " + methodName);
	}

	@Test
	public void testGetSpeedMultiplier() throws Exception {
		Ball ball;
		for (int i = -10; i < 2000; i++) {
			ball = new Ball(Vector2D.X, Vector2D.Y, 0, 0, (double) i / 100);
			if (i > 0) assert ball.getSpeedMultiplier() == (double) i / 100;
			assert ball.getSpeedMultiplier() > 0;
		}
		pass("getSpeedMultiplier");
	}

	@Test
	public void testMovement() {
		for (int t = -100; t < 200; t++) {
			Ball testBall2 = new Ball(Vector2D.ZERO, Vector2D.Y.cpy(), t, 0, 1);
			Ball testBall3 = new Ball(Vector2D.ZERO, Vector2D.X.cpy(), t, 0, 1);
			Vector2D expectedPosition = null;
			for (int i = -100; i < 2000; i++) {
				testBall2.updateCurrentTime(i);
				testBall3.updateCurrentTime(i);
				expectedPosition = new Vector2D(0, Constants.BALL_SPEED * (i - t));
				if (t <= i) {
					assert expectedPosition.equals(testBall2.getCurrentPosition());
					assert expectedPosition.equals(testBall3.getCurrentPosition());
				}
				else {

					assert !expectedPosition.equals(testBall2.getCurrentPosition());
					assert !expectedPosition.equals(testBall3.getCurrentPosition());
				}
			}
		}

		pass("movement");
	}

	@Test
	public void testGetCurrentPosition() throws Exception {
		Ball ball;
		for (int i = 0; i < 99; i++) {
			for (int j = 0; j < 99; j++) {
				Vector2D vector2D = Vector2D.X.cpy().multiply((double) i / 100).add(Vector2D.Y.cpy().multiply((double) j / 100));
				ball = new Ball(vector2D, Vector2D.Y, 0, 0, 1);
				ball.updateCurrentTime(0);
				assert ball.getCurrentPosition().equals(vector2D);
			}
		}
		pass("getCurrentPosition");
	}

	@Test
	public void testGetScore() throws Exception {
		Ball ball;
		for (int i = 0; i < 99; i++) {
			ball = new Ball(Vector2D.X, Vector2D.Y, 0, i, 1);
			assert ball.getScore() == i + 1;
		}
		pass("getScore");
	}

	@Test
	public void testGetType() throws Exception {
		Ball ball;
		for (int i = 0; i < 99; i++) {
			ball = new Ball(Vector2D.X, Vector2D.Y, 0, i, 1);
			assert ball.getType() == i;
		}
		pass("getType");
	}

	@Test
	public void testIsMovingUp() throws Exception {
		Ball ball;
		for (int i = 0; i < 99; i++) {
			for (int j = 0; j < 99; j++) {
				Vector2D vector2D = Vector2D.X.cpy().multiply((double) i / 100).add(Vector2D.Y.cpy().add(0, -0.5).multiply((double) j / 100));
				ball = new Ball(Vector2D.X, vector2D, 0, 0, 1);
				ball.updateCurrentTime(0);
				if (vector2D.y >= 0) assert ball.isMovingUp();
				else assert !ball.isMovingUp();
			}
		}
		pass("isMovingUp");
	}

	@Test
	public void testUpdating() throws Exception {
		Ball ball;
		for (int i = 0; i < 99; i++) {
			for (int j = 0; j < 99; j++) {
				Vector2D vector2D = Vector2D.X.cpy().multiply((double) i / 100).add(Vector2D.Y.cpy().add(0, -0.5).multiply((double) j / 100));
				ball = new Ball(vector2D, vector2D, 0, 0, 1);
				for (int k = -10; k < 2000; k++) {
					ball.updateCurrentTime(k * 7);
					if (ball.getCurrentPosition().y <= Constants.HEIGHT + Constants.PADDLE_EFFECTIVE_DEPTH + Constants.EDGE_PADDING && ball.getCurrentPosition().y >= 0 - Constants.PADDLE_EFFECTIVE_DEPTH - Constants.EDGE_PADDING)
						assert ball.inGame();
					else assert !ball.inGame();
					assert ball.getCurrentPosition().x >= 0;
					assert ball.getCurrentPosition().x <= 1;
				}
			}
		}
		pass("inGame");
		pass("updateCurrentTime");
		pass("getCurrentPosition");
	}
}
