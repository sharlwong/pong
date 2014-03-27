package MultiPong;

public class Ball {
	private long apparentStartTimeMillis;
	private long realEndTimeMillis;
	private long realStartTimeMillis;
	private Vector2D realVelocity;
	private Vector2D startPosition;
	private Vector2D startVelocity;
	private long tempCurrentMillis;

	public Ball(Vector2D startPosition, Vector2D startVelocity, long realStartTime) {
		this.startPosition = startPosition;
		this.startVelocity = startVelocity.makeUnitVector().multiply(Assets.BALL_SPEED);
		this.realStartTimeMillis = realStartTime;
	}

	public Vector2D getPosition(long currentTimeMillis) {
		tempCurrentMillis = currentTimeMillis;
		Vector2D realVelocityReflected = new Vector2D(realVelocity);
		realVelocityReflected.x = 0 - realVelocityReflected.x;

		long timeTravelled = currentTimeMillis - apparentStartTimeMillis;
		Vector2D youAreHere = new Vector2D(startPosition);
		youAreHere.add(realVelocity.multiply(timeTravelled));
		while (youAreHere.x < 0 || youAreHere.x > Assets.WIDTH) {
			if (youAreHere.x < 0) youAreHere.x = 0 - youAreHere.x;
			if (youAreHere.x > Assets.WIDTH) youAreHere.x = 2 * Assets.WIDTH - youAreHere.x;
		}
		return youAreHere;
	}

	public Vector2D getCurrentPosition(){
		return getPosition(tempCurrentMillis);
	}

	public void init(long fakeStartTime) throws RuntimeException {
		this.apparentStartTimeMillis = fakeStartTime;
		double distanceToTravel = Assets.HEIGHT * startVelocity.length() / Math.abs(startVelocity.y);
		double realTimeTakenMillis = distanceToTravel / Assets.BALL_SPEED;
		realEndTimeMillis = realStartTimeMillis + (long) realTimeTakenMillis;
		if ((apparentStartTimeMillis - realStartTimeMillis) > Assets.MAX_ACCEPTABLE_LAG) throw new Assets.LagException();

		double realSpeed = distanceToTravel / (realEndTimeMillis - apparentStartTimeMillis);
		realVelocity = startVelocity.makeUnitVector().multiply(realSpeed);
	}

	public Ball paddleReflect(Vector2D newVelocity){
		return new Ball(getCurrentPosition(),newVelocity, tempCurrentMillis);
	}
	
	public boolean checkWallReflect (){
		if (getCurrentPosition().x<=0 || getCurrentPosition().x>=1){
			return true;
		}else{
			return false;
		}
	}
	
	public Ball wallReflect (){
		private Vector2D newVelocity = new Vector2D(realVelocity.x * (-1), realVelocity.y);
		return new Ball(getCurrentPosition(),newVelocity, tempCurrentMillis);
	}
}
