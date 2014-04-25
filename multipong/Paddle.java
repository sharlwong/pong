package multipong;

public class Paddle {
	private double   max;
	private double   min;
	private int      score;
	private Vector2D paddleCenter;
	public  boolean  playerBottom;

	public Paddle(int playerNum) {
		this.max = Constants.WIDTH + Constants.BALL_RADIUS - (Constants.PADDLE_WIDTH / 2);
		this.min = (Constants.PADDLE_WIDTH / 2) - Constants.BALL_RADIUS;
		this.score = 0;
		this.paddleCenter = new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT * playerNum);
		this.playerBottom = (playerNum == 0);
	}

	public Ball bounce(Ball b, long currentTimeMillis) {
		double yVelocity = Constants.PADDLE_WIDTH / 2;
		if (!playerBottom) yVelocity = 0 - yVelocity;
		Vector2D outVelocity = new Vector2D(b.getCurrentPosition().x - paddleCenter.x, yVelocity);
		outVelocity.makeUnitVector().multiply(Constants.BALL_SPEED);
		return new Ball(b.getCurrentPosition(), outVelocity, currentTimeMillis, b.getType(), b.getSpeedMultiplier());
	}

	public boolean collisionCheck(Ball b) {
		Vector2D ballPosition = b.getCurrentPosition();
		boolean up = b.isMovingUp();
		if (Math.abs(ballPosition.x - paddleCenter.x) > (Constants.PADDLE_WIDTH / 2)) return false;
		if (playerBottom && ballPosition.y < (0 - Constants.PADDLE_EFFECTIVE_DEPTH) && !up) return false;
		if (playerBottom && ballPosition.y < 0 && !up) return true;
		if (!playerBottom && ballPosition.y > (Constants.HEIGHT + Constants.PADDLE_EFFECTIVE_DEPTH) && up) return false;
		if (!playerBottom && ballPosition.y > (Constants.HEIGHT) && up) return true;
		return false;
	}

	public void incrementScore(Ball b) {
		score += b.getScore();
	}

	private void setPosition(double xValue) {
		paddleCenter.x = xValue;
		if (paddleCenter.x < min) paddleCenter.x = min;
		if (paddleCenter.x > max) paddleCenter.x = max;
	}

	public void setFractionalPosition(double fraction) {
		if (fraction < 0 || fraction > 1) return;
		setPosition(min + fraction * (max - min));
	}

	/**
	 * @return paddle center coordinates.
	 */
	public double[] getXY() {
		return new double[]{paddleCenter.x, paddleCenter.y};
	}

	/**
	 * @return paddle center coordinates in the form of Vector2D
	 */
	public Vector2D getCenter() {
		return paddleCenter;
	}

	/**
	 * @return current score of the paddle controller
	 */
	public int getScore() {
		return score;
	}

	public double getFractionalPosition() {
		return (paddleCenter.x - min) / (max - min);
	}
}