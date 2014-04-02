package multipong;

public class Paddle {
	public boolean playerBottom;
	private double max = Constants.WIDTH + Constants.BALL_RADIUS - (Constants.PADDLE_WIDTH / 2);
	private double min = (Constants.PADDLE_WIDTH / 2) - Constants.BALL_RADIUS;
	private Vector2D paddleCenter;
	private int score = 0;
	private Vector2D velocity;

	public Paddle(int playerNum) {
		//assumes binary player ID number: 0 at bottom, 1 at top
		paddleCenter = new Vector2D(-1, Constants.HEIGHT * playerNum);
		setFractionalPosition(0.5);
		this.playerBottom = (playerNum == 0);
	}

	public Ball bounce(Ball b, long currentTimeMillis) {
		double yVelocity = Constants.PADDLE_WIDTH / 2;
		if (!playerBottom) yVelocity = 0 - yVelocity;
		Vector2D outVelocity = new Vector2D(b.getCurrentPosition().x - paddleCenter.x, yVelocity);
		outVelocity.makeUnitVector().multiply(Constants.BALL_SPEED);
		b.kill();
		return new Ball(b.getCurrentPosition(), outVelocity, currentTimeMillis);
	}

	public boolean collisionCheck(Ball b) {
		if (!b.inGame()) return false;
		Vector2D ballPosition = b.getCurrentPosition();
		if (Math.abs(ballPosition.x - paddleCenter.x) > Constants.PADDLE_WIDTH) return false;
		if (playerBottom) {
			if (ballPosition.y > 0) return false;
			if (ballPosition.y < (0 - Constants.PADDLE_EFFECTIVE_DEPTH)) return false;
		} else {
			if (ballPosition.y < Constants.HEIGHT) return false;
			if (ballPosition.y > (Constants.HEIGHT + Constants.PADDLE_EFFECTIVE_DEPTH)) return false;
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
			b.kill();
		}
	}

	public void setFractionalPosition(double fraction) {
		if (fraction < 0 || fraction > 1) return;
		setPosition(min + fraction*(max-min));
	}

	private void setPosition(double xValue) {
		paddleCenter.x = xValue;
		if (paddleCenter.x < min) paddleCenter.x = min;
		if (paddleCenter.x > max) paddleCenter.x = max;
	}

	public void setVelocity(double xVelocity) {
		if (xVelocity == 0) velocity = Vector2D.Zero.cpy();
		if (xVelocity > 0) velocity = Vector2D.X.cpy().multiply(0.5);
		if (xVelocity < 0) velocity = Vector2D.X.cpy().multiply(-0.5);
	}

	public void updateDeltaTime(float delta) {
		//		if (velocity.x > 0.8) velocity.x = 0.8;
		//		if (velocity.x < -0.8) velocity.x = -0.8;
		//		if (velocity.x > 0 && velocity.x < 0.3) velocity.x = 0.3;
		//		if (velocity.x < 0 && velocity.x > -0.3) velocity.x = -0.3;
		//		if ((paddleCenter.x < (Constants.PADDLE_WIDTH / 2)) || (paddleCenter.x > (Constants.DISPLAY_WIDTH - (Constants.PADDLE_WIDTH / 2)))) velocity.x = 0;

		setPosition(paddleCenter.cpy().add(velocity.cpy().multiply(delta)).x);
	}
}