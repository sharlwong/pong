package multipong;

public class Paddle {
	private double   max;
	private double   min;
	private int      score;
	private Vector2D paddleCenter;
	public  boolean  playerBottom;

	/**
	 * give you a paddle
	 *
	 * @param playerNum top == 1 OR bottom == 0
	 */
	public Paddle(int playerNum) {
		this.max = Constants.WIDTH + Constants.BALL_RADIUS - (Constants.PADDLE_WIDTH / 2);
		this.min = (Constants.PADDLE_WIDTH / 2) - Constants.BALL_RADIUS;
		this.score = 0;
		this.paddleCenter = new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT * playerNum);
		this.playerBottom = (playerNum == 0);
	}

	/**
	 * bounce a ball off this paddle
	 *
	 * @param ball              to bounce
	 * @param currentTimeMillis give the ball a birth date
	 * @return new ball to relace the old one
	 */
	public Ball bounce(Ball ball, long currentTimeMillis) {
		double yVelocity = Constants.PADDLE_WIDTH / 2;
		if (!playerBottom) yVelocity = 0 - yVelocity;
		Vector2D outVelocity = new Vector2D(ball.getCurrentPosition().x - paddleCenter.x, yVelocity);
		return new Ball(ball.getCurrentPosition(), outVelocity, currentTimeMillis, ball.getType(), ball.getSpeedMultiplier());
	}

	/**
	 * is this ball hitting this paddle
	 *
	 * @param ball to check
	 * @return yes or no
	 */
	public boolean collisionCheck(Ball ball) {
		Vector2D ballPosition = ball.getCurrentPosition();
		boolean up = ball.isMovingUp();
		if (Math.abs(ballPosition.x - paddleCenter.x) > (Constants.PADDLE_WIDTH / 2)) return false;
		if (playerBottom && ballPosition.y < (0 - Constants.PADDLE_EFFECTIVE_DEPTH) && !up) return false;
		if (playerBottom && ballPosition.y <= 0 && !up) return true;
		if (!playerBottom && ballPosition.y > (Constants.HEIGHT + Constants.PADDLE_EFFECTIVE_DEPTH) && up) return false;
		if (!playerBottom && ballPosition.y >= (Constants.HEIGHT) && up) return true;
		return false;
	}

	/**
	 * add a balls weight to your score
	 *
	 * @param ball to add
	 */
	public void incrementScore(Ball ball) {
		score += ball.getScore();
	}

	/**
	 * move to this place
	 *
	 * @param xValue move here
	 */
	private void setPosition(double xValue) {
		paddleCenter.x = xValue;
		if (paddleCenter.x < min) paddleCenter.x = min;
		if (paddleCenter.x > max) paddleCenter.x = max;
	}

	/**
	 * move relative to the traversal line
	 *
	 * @param fraction of the line the paddle should be at
	 */
	public void setFractionalPosition(double fraction) {
		if (fraction < 0 || fraction > 1) return;
		setPosition(min + fraction * (max - min));
	}

	/**
	 * where art thou (i want numbers)
	 *
	 * @return here i am (paddle center
	 */
	public double[] getXY() {
		return new double[]{paddleCenter.x, paddleCenter.y};
	}

	/**
	 * request vector position
	 *
	 * @return paddle center coordinates in the form of Vector2D
	 */
	public Vector2D getCenter() {
		return paddleCenter;
	}

	/**
	 * whats your score
	 *
	 * @return current score of the paddle controller
	 */
	public int getScore() {
		return score;
	}

	/**
	 * where are you
	 *
	 * @return this is fraction of where i could be
	 */
	public double getFractionalPosition() {
		return (paddleCenter.x - min) / (max - min);
	}
}