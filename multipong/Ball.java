package multipong;

public class Ball {
	private double   uselessVar;
	private long     initTime;
	private Vector2D velocity;
	private Vector2D initialPosition;
	private Vector2D currentPosition;

	public Ball(Vector2D startPosition, Vector2D startVelocity, long startTimeMillis, double unusedVariable) {
		this.initialPosition = startPosition;
		this.uselessVar = unusedVariable;
		this.initTime = startTimeMillis;
		this.initTime = startTimeMillis;

		/* error correct starting speed, leaving the direction equal */
		Vector2D initialVelocity = startVelocity.cpy().makeUnitVector().multiply(Constants.BALL_SPEED);

		/* error containment */
		if (initialVelocity.y == 0) initialVelocity = Vector2D.Y.cpy().makeUnitVector().multiply(Constants.BALL_SPEED);

		/* how far does the ball move to the paddle line */
		double distanceToTravel;
		if (initialVelocity.y > 0)
			distanceToTravel = (Constants.HEIGHT - startPosition.y) * (initialVelocity.length() / Math.abs(initialVelocity.y));
		else distanceToTravel = startPosition.y * (initialVelocity.length() / Math.abs(initialVelocity.y));

		/* when is it supposed to the paddle line */
		double realTimeTakenMillis = distanceToTravel / Constants.BALL_SPEED;
		long realEndTimeMillis = initTime + (long) realTimeTakenMillis;

		/* how fast must it move to get there on time */
		double realSpeed = distanceToTravel / (realEndTimeMillis - initTime);
		velocity = initialVelocity.makeUnitVector().multiply(realSpeed);

		/* setup ball at time zero */
		updateCurrentTime(startTimeMillis);
	}

	public double getUnusedVariable() {
		return uselessVar;
	}

	public Vector2D getCurrentPosition() {
		return currentPosition;
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
		if (currentPosition.y < (0 - Constants.PADDLE_EFFECTIVE_DEPTH - Constants.EDGE_PADDING) && velocity.y < 0) return false;
		if (currentPosition.y > (Constants.HEIGHT + Constants.PADDLE_EFFECTIVE_DEPTH + Constants.EDGE_PADDING) && velocity.y > 0) return false;
		return true;
	}

	public boolean isMovingUp() {
		return velocity.y > 0;
	}
}