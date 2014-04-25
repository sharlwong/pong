package multipong;

/**
 * Paddle is a game object in this game. Paddle will be controlled by players during
 * the game play. Meanwhile, it is also responsible for collision check and score
 * updating. Paddle has two types, which are the bottom one and the top one respectively.
 * Functions related to position setting will be called in InputHandler. **
 */
public class Paddle {
	private double   max;
	private double   min;
	private int      score;
	private Vector2D paddleCenter;
	public  boolean  playerBottom;
	private int      orange;
	private int      kiwi;
	private int      watermelon;

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
		this.orange = 0;
		this.kiwi = 0;
		this.watermelon = 0;
	}

	/**
	 * This function will be called when collision happens.
	 *
	 * @param ball              Ball before collision
	 * @param currentTimeMillis Time when collision happens
	 * @return new ball with new velocity and initialized starting time after collision
	 */
	public Ball bounce(Ball ball, long currentTimeMillis) {
		double yVelocity = Constants.PADDLE_WIDTH / 2;
		if (!playerBottom) yVelocity = 0 - yVelocity;
		Vector2D outVelocity = new Vector2D(ball.getCurrentPosition().x - paddleCenter.x, yVelocity);
		return new Ball(ball.getCurrentPosition(), outVelocity, currentTimeMillis, ball.getType(), ball.getSpeedMultiplier());
	}

	/**
	 * Check whether collision happens. By checking paddle position and ball position.
	 *
	 * @param ball to check
	 * @return True if collision detected and false otherwise.
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
	 * When a ball passes paddle, score will be increased.
	 * Type: 0	Orange		Score: 1
	 * Type: 1	Kiwi		Score: 2
	 * Type: 2  Watermelon	Score: 3
	 *
	 * @param ball to be added
	 */
	public void incrementScore(Ball ball) {
		score += ball.getScore();
		if (ball.getScore() == 1) orange++;
		if (ball.getScore() == 2) kiwi++;
		if (ball.getScore() == 3) watermelon++;
	}

	/**
	 * Set x coordinate of the center point of the paddle.
	 *
	 * @param xValue center point, cannot be larger than max value (screenwidth - paddlewidth / 2),
	 *               cannot be smaller than min value (paddlewidth / 2) either.
	 */
	private void setPosition(double xValue) {
		paddleCenter.x = xValue;
		if (paddleCenter.x < min) paddleCenter.x = min;
		if (paddleCenter.x > max) paddleCenter.x = max;
	}

	/**
	 * Similar to setPosition. Input will be the fraction from 0 to 1.
	 * More convenient to use in other classes.
	 *
	 * @param fraction fraction position. From 0 to 1.
	 *                 0 represents the min value while 1 represents the max value.
	 */
	public void setFractionalPosition(double fraction) {
		if (fraction < 0 || fraction > 1) return;
		setPosition(min + fraction * (max - min));
	}

	/**
	 * where art thou (i want numbers)
	 *
	 * @return here i am (paddle center XY)
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

	/**
	 * @param player paddle on the bottom or on the top
	 * @return paddle's fractional position on the other screen
	 */
	public double getTransformedFractionalPosition(int player) {
		return Math.abs(player - getFractionalPosition());
	}

	public int getOrange(){
		return orange;
	}

	public int getKiwi(){
		return kiwi;
	}

	public int getWatermelon(){
		return watermelon;
	}
}