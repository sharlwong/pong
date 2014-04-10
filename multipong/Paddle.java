package multipong;

public class Paddle {
	public boolean playerBottom;
	private double max = Constants.WIDTH + Constants.BALL_RADIUS - (Constants.PADDLE_WIDTH / 2);
	private double min = (Constants.PADDLE_WIDTH / 2) - Constants.BALL_RADIUS;
	private Vector2D paddleCenter;
	private double score = 0;

	public Paddle(int playerNum) {
		paddleCenter = new Vector2D(-1, Constants.HEIGHT * playerNum);
		setFractionalPosition(0.5);
		this.playerBottom = (playerNum == 0);
	}

	public Ball bounce(Ball b, long currentTimeMillis) {
		double yVelocity = Constants.PADDLE_WIDTH / 2;
		if (!playerBottom) yVelocity = 0 - yVelocity;
		Vector2D outVelocity = new Vector2D(b.getCurrentPosition().x - paddleCenter.x, yVelocity);
		outVelocity.makeUnitVector().multiply(Constants.BALL_SPEED);
		return new Ball(b.getCurrentPosition(), outVelocity, currentTimeMillis, b.getUnusedVariable());
	}

	public boolean collisionCheck(Ball b) {
		Vector2D ballPosition = b.getCurrentPosition();
		boolean up = b.isMovingUp();
		if (Math.abs(ballPosition.x - paddleCenter.x) > (Constants.PADDLE_WIDTH / 2)) return false;
		if (playerBottom && ballPosition.y < Constants.BALL_RADIUS && !up) return true;
		if (!playerBottom && ballPosition.y > (Constants.HEIGHT - Constants.BALL_RADIUS) && up) return true;
		return false;
	}

	public Vector2D getCenter() {
		return paddleCenter;
	}

	public int getScore() {
		return (int) Math.floor(score);
	}

	public void incrementScore() {
		incrementScore(0);
	}

	public void incrementScore(double n) {
		this.score += n;
	}

	public void setFractionalPosition(double fraction) {
		if (fraction < 0 || fraction > 1) return;
		setPosition(min + fraction * (max - min));
	}

	private void setPosition(double xValue) {
		paddleCenter.x = xValue;
		if (paddleCenter.x < min) paddleCenter.x = min;
		if (paddleCenter.x > max) paddleCenter.x = max;
	}
}