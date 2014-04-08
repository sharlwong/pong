package multipong;

public class Paddle {
	public boolean playerBottom;
	private double max = Constants.WIDTH + Constants.BALL_RADIUS - (Constants.PADDLE_WIDTH / 2);
	private double min = (Constants.PADDLE_WIDTH / 2) - Constants.BALL_RADIUS;
	private Vector2D paddleCenter;
	private int score = 0;
	private boolean movingLeft = false;
	private boolean movingRight = false;

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
		if (!playerBottom && ballPosition.y > (Constants.HEIGHT - Constants.BALL_RADIUS) && up)
			return true;
		return false;
	}

	public Vector2D getCenter() {
		return paddleCenter;
	}

	public int getScore() {
		return score;
	}

	public void incrementScore() {
		this.score++;
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

	public void updateDeltaTime(float delta) {
		Vector2D displacement = Vector2D.ZERO.cpy();
		if (movingRight) displacement.add(Vector2D.X.cpy().multiply(Constants.PADDLE_DEFAULT_VELOCITY));
		if (movingLeft) displacement.add(Vector2D.X.cpy().multiply(-1 * Constants.PADDLE_DEFAULT_VELOCITY));
		setPosition(paddleCenter.cpy().add(displacement.cpy().multiply(delta)).x);
	}

	public void startMoveRight() {
		movingRight = true;
	}

	public void stopMoveRight() {
		movingRight = false;
	}

	public void startMoveLeft() {
		movingLeft = true;
	}

	public void stopMoveLeft() {
		movingLeft = false;
	}
}