package multipong;

public class Ball {
//	private boolean alive;
	private long apparentStartTimeMillis;
	private long realStartTimeMillis;
	private Vector2D realVelocity;
	private Vector2D startPosition;
	private Vector2D startVelocity;
	private Vector2D tempCurrentPosition;
//	private long timeMillis;

	public Ball(Vector2D startPosition, Vector2D startVelocity, long realStartTime) {
//		this.alive = startPosition.x >= 0 && startPosition.x <= Constants.WIDTH && startPosition.y >= 0 && startPosition.y <= Constants.HEIGHT;
		this.startPosition = startPosition;
		this.startVelocity = startVelocity.makeUnitVector().multiply(Constants.BALL_SPEED);
		this.realStartTimeMillis = realStartTime;
		init(realStartTime);
	}

	public Vector2D getCurrentPosition() {
		return tempCurrentPosition;
	}

//	public void updateDeltaTime(long deltaMillis){
//		timeMillis += deltaMillis;
//		updateCurrentTime(timeMillis);
//	}

	public void updateCurrentTime(long currentTimeMillis) {
//		this.timeMillis = currentTimeMillis;
		long timeTravelled = currentTimeMillis - apparentStartTimeMillis;
		Vector2D youAreHere = new Vector2D(startPosition);
		youAreHere.add(realVelocity.cpy().multiply(timeTravelled));
		while (youAreHere.x < 0 || youAreHere.x > Constants.WIDTH) {
			if (youAreHere.x < 0) youAreHere.x = 0 - youAreHere.x;
			if (youAreHere.x > Constants.WIDTH) youAreHere.x = 2 * Constants.WIDTH - youAreHere.x;
		}
		tempCurrentPosition = youAreHere;
	}

	public boolean inGame() {
//		if (!isAlive()) return false;
		if (tempCurrentPosition.y < (0 - Constants.PADDLE_EFFECTIVE_DEPTH)) return false;
		if (tempCurrentPosition.y > (Constants.HEIGHT + Constants.PADDLE_EFFECTIVE_DEPTH)) return false;
		return true;
	}

	private void init(long imaginaryStartTime) throws Constants.LagException {
		/* grab the fake starting time and check it */
		this.apparentStartTimeMillis = imaginaryStartTime;
		if ((apparentStartTimeMillis - realStartTimeMillis) > Constants.MAX_ACCEPTABLE_LAG)
			throw new Constants.LagException();

		/* error containment */
		if (startVelocity.y == 0) startVelocity = Vector2D.Y.cpy().makeUnitVector().multiply(Constants.BALL_SPEED);

		/* how far does the ball move to the paddle line */
		double distanceToTravel;
		if (startVelocity.y > 0)
			distanceToTravel = (Constants.HEIGHT - startPosition.y) * (startVelocity.length() / Math.abs(startVelocity.y));
		else distanceToTravel = startPosition.y * (startVelocity.length() / Math.abs(startVelocity.y));

		/* when is it supposed to the paddle line */
		double realTimeTakenMillis = distanceToTravel / Constants.BALL_SPEED;
		long realEndTimeMillis = realStartTimeMillis + (long) realTimeTakenMillis;

		/* how fast must it move to get there on time */
		double realSpeed = distanceToTravel / (realEndTimeMillis - apparentStartTimeMillis);
		realVelocity = startVelocity.makeUnitVector().multiply(realSpeed);


		/* setup ball at time zero */
		updateCurrentTime(imaginaryStartTime);

	}

//	public boolean isAlive() {
//		return alive;
//	}

//	public void kill() {
//		alive = false;
//	}
}