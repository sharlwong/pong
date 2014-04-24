package multipong;

public class Ball {
	private long     initTime;
	private Vector2D velocity;
	private Vector2D initialPosition;
	private Vector2D currentPosition;
	private int      type;
	private double   speedMultiplier;

	public Ball(Vector2D startPosition, Vector2D startVelocity, long startTimeMillis, int type, double speedMultiplier) {
		this.initialPosition = startPosition;
		this.initTime = startTimeMillis;
		this.type = type;
		this.speedMultiplier = speedMultiplier <= 0 ? 0.1 : speedMultiplier;

		/* widen starting velocity angle */
		Vector2D initialVelocity = startVelocity.cpy().multiply(Constants.ANGLE_WIDENER);

		/* error containment */
		if (initialVelocity.y == 0)
			initialVelocity = Vector2D.Y.cpy();

		/* how far does the ball move to the paddle line */
		double distanceToTravel;
		if (initialVelocity.y > 0)
			distanceToTravel = (Constants.HEIGHT - startPosition.y) * (initialVelocity.length() / Math.abs(initialVelocity.y));
		else distanceToTravel = startPosition.y * (initialVelocity.length() / Math.abs(initialVelocity.y));

		/* when is it supposed to the paddle line, rounded to the nearest millisecond */
		double realTimeTakenMillis = distanceToTravel / Constants.BALL_SPEED;
		long realEndTimeMillis = initTime + (long) realTimeTakenMillis;

		/* how fast must it move to get there on time, times the speed constant */
		double realSpeed = distanceToTravel / (realEndTimeMillis - initTime);
		this.velocity = initialVelocity.makeUnitVector().multiply(realSpeed).multiply(speedMultiplier);

		/* setup ball at time zero */
		updateCurrentTime(startTimeMillis);
	}

	public void updateCurrentTime(long currentTimeMillis) {
		long timeTravelled = currentTimeMillis - initTime;
		timeTravelled = timeTravelled < 0 ? 0 : timeTravelled;
		Vector2D youAreHere = new Vector2D(initialPosition);
		youAreHere.add(velocity.cpy().multiply(timeTravelled));
		while (youAreHere.x < 0 || youAreHere.x > Constants.WIDTH) {
			if (youAreHere.x < 0) youAreHere.x = 0 - youAreHere.x;
			if (youAreHere.x > Constants.WIDTH) youAreHere.x = 2 * Constants.WIDTH - youAreHere.x;
		}
		currentPosition = youAreHere;
	}

	public boolean inGame() {
		if (currentPosition.y < (0 - Constants.PADDLE_EFFECTIVE_DEPTH - Constants.EDGE_PADDING) && velocity.y < 0)
			return false;
		if (currentPosition.y > (Constants.HEIGHT + Constants.PADDLE_EFFECTIVE_DEPTH + Constants.EDGE_PADDING) && velocity.y > 0)
			return false;
		return true;
	}

	public boolean isMovingUp() {
		return velocity.y > 0;
	}

	/**
	 * @return Ball Type (Integer, 0, 1, or 2)
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return Score of the ball (Integer, 1, 2, or 3)
	 */
	public int getScore() {
		return type + 1;
	}

	public Vector2D getCurrentPosition() {
		return currentPosition;
	}

	public double getSpeedMultiplier() {
		return speedMultiplier;
	}
}