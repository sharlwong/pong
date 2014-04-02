package multipong;

public class Paddle {
	public boolean playerBottom;
	private Vector2D paddleCenter;
	private int score = 0;
	private Vector2D velocity;

	public Paddle(int playerNum) {
		//assumes binary player ID number: 0 at bottom, 1 at top
		paddleCenter = new Vector2D(Assets.WIDTH / 2, Assets.HEIGHT * playerNum);
		this.playerBottom = (playerNum == 0);
	}

	public Ball bounce(Ball b, long currentTimeMillis) {
		double yVelocity = Assets.PADDLE_WIDTH / 2;
		if (!playerBottom) yVelocity = 0 - yVelocity;
		Vector2D outVelocity = new Vector2D(b.getCurrentPosition().x - paddleCenter.x, yVelocity);
		outVelocity.makeUnitVector().multiply(Assets.BALL_SPEED);
		b.stop();
		return new Ball(b.getCurrentPosition(), outVelocity, currentTimeMillis);
	}

	public boolean collisionCheck(Ball b) {
		if (!b.inGame()) return false;
		Vector2D ballPosition = b.getCurrentPosition();
		if (Math.abs(ballPosition.x - paddleCenter.x) > Assets.PADDLE_WIDTH) return false;
		if (playerBottom) {
			if (ballPosition.y > 0) return false;
			if (ballPosition.y < (0 - Assets.PADDLE_EFFECTIVE_DEPTH)) return false;
		} else {
			if (ballPosition.y < Assets.HEIGHT) return false;
			if (ballPosition.y > (Assets.HEIGHT + Assets.PADDLE_EFFECTIVE_DEPTH)) return false;
		}
		return true;
	}

	public Vector2D getCenter() {
		return paddleCenter;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void incrementScore() {
		this.score++;
	}

	public void miss(Ball b) {
		if (!collisionCheck(b)) {
			b.stop();
		}
	}

	public void setFractionalPosition(double fraction) {
		if (fraction < 0 || fraction > 1) return;
		setPosition((Assets.DISPLAY_WIDTH - Assets.PADDLE_WIDTH) * fraction + Assets.PADDLE_WIDTH / 2);
	}

	private void setPosition(double xValue) {
		paddleCenter.x = xValue;
		if (paddleCenter.x < (Assets.PADDLE_WIDTH / 2)) paddleCenter.x = Assets.PADDLE_WIDTH / 2;
		if (paddleCenter.x > (Assets.DISPLAY_WIDTH - (Assets.PADDLE_WIDTH / 2)))
			paddleCenter.x = Assets.DISPLAY_WIDTH - (Assets.PADDLE_WIDTH / 2);
	}

	public void setVelocity(double xVelocity) {
		if (xVelocity == 0) velocity = Vector2D.Zero;
		if (xVelocity > 0) velocity = Vector2D.X.multiply(0.5);
		if (xVelocity < 0) velocity = Vector2D.X.multiply(-0.5);
	}

	public void updateDeltaTime(float delta) {
		//		if (velocity.x > 0.8) velocity.x = 0.8;
		//		if (velocity.x < -0.8) velocity.x = -0.8;
		//		if (velocity.x > 0 && velocity.x < 0.3) velocity.x = 0.3;
		//		if (velocity.x < 0 && velocity.x > -0.3) velocity.x = -0.3;
		//		if ((paddleCenter.x < (Assets.PADDLE_WIDTH / 2)) || (paddleCenter.x > (Assets.DISPLAY_WIDTH - (Assets.PADDLE_WIDTH / 2)))) velocity.x = 0;

		setPosition(paddleCenter.cpy().add(velocity.cpy().multiply(delta)).x);
	}
}