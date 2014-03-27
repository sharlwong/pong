package com.sutd.GameObjects;

import com.sutd.PongHelpers.Assets;
import com.sutd.PongHelpers.Vector2D;

public class Ball {
	private long apparentStartTimeMillis;
	private long realEndTimeMillis;
	private long realStartTimeMillis;
	private Vector2D realVelocity;
	private Vector2D startPosition;
	private Vector2D startVelocity;
	private long tempCurrentMillis;
	
	private boolean isMoving;

	public Ball(Vector2D startPosition, Vector2D startVelocity,
			long realStartTime) {
		this.startPosition = startPosition;
		this.startVelocity = startVelocity.makeUnitVector().multiply(
				Assets.BALL_SPEED);
		this.realStartTimeMillis = realStartTime;
		this.isMoving = true;
	}
	
	public Vector2D getPosition(long currentTimeMillis) {
		tempCurrentMillis = currentTimeMillis;
		Vector2D realVelocityReflected = new Vector2D(realVelocity);
		realVelocityReflected.x = 0 - realVelocityReflected.x;

		long timeTravelled = currentTimeMillis - apparentStartTimeMillis;
		Vector2D youAreHere = new Vector2D(startPosition);
		youAreHere.add(realVelocity.multiply(timeTravelled));
		while (youAreHere.x < 0 || youAreHere.x > Assets.WIDTH) {
			if (youAreHere.x < 0)
				youAreHere.x = 0 - youAreHere.x;
			if (youAreHere.x > Assets.WIDTH)
				youAreHere.x = 2 * Assets.WIDTH - youAreHere.x;
		}
		return youAreHere;
	}

	public Vector2D getCurrentPosition() {
		return getPosition(tempCurrentMillis);
	}

	public void init(long fakeStartTime) throws RuntimeException {
		this.apparentStartTimeMillis = fakeStartTime;
		double distanceToTravel = Assets.HEIGHT * startVelocity.length()
				/ Math.abs(startVelocity.y);
		double realTimeTakenMillis = distanceToTravel / Assets.BALL_SPEED;
		realEndTimeMillis = realStartTimeMillis + (long) realTimeTakenMillis;
		if ((apparentStartTimeMillis - realStartTimeMillis) > Assets.MAX_ACCEPTABLE_LAG)
			throw new Assets.LagException();

		double realSpeed = distanceToTravel
				/ (realEndTimeMillis - apparentStartTimeMillis);
		realVelocity = startVelocity.makeUnitVector().multiply(realSpeed);
	}

	public Ball paddleReflect(Vector2D newVelocity) {
		return new Ball(getCurrentPosition(), newVelocity, tempCurrentMillis);
	}
	
	public void stop(){
		isMoving = false;
	}
	
	public void restart(){
		isMoving = true;
	}
	
	public boolean isAlive(){
		return isMoving;
	}
	
	/**
	 * Based on very rough calculation and estimation
	 * May need to further modify
	 * @param paddle
	 * @return
	 */
	
	public Vector2D newVelocityAfterReflection(Paddle paddle){
		double x = getCurrentPosition().x;
		double y = getCurrentPosition().y;
		double vx = realVelocity.x;
		double vy = realVelocity.y;
		Vector2D newVelocity = new Vector2D();
		double hitPoint = x - paddle.getCenter().x;
		double hitPointRatio = 2*hitPoint/Assets.DISPLAY_WIDTH;
		double innerAngle = 15 * hitPointRatio * Math.PI/180;
		if (x >= 0){
			if (Math.abs(Math.tan(x/y)) > Math.abs(innerAngle)){
				newVelocity.x = vx *(-1) * (Math.abs(Math.tan(x/y)) - Math.abs(innerAngle));
				newVelocity.y = vx *(-1) *(1+Math.abs(Math.tan(x/y)) - Math.abs(innerAngle));
			}else if (Math.abs(Math.tan(x/y)) > Math.abs(innerAngle)){
				newVelocity.x = vx *(-1) * (1+Math.abs(Math.tan(x/y)) - Math.abs(innerAngle));
				newVelocity.y = vx *(-1) *(Math.abs(Math.tan(x/y)) - Math.abs(innerAngle));
			}else{
				newVelocity.x = vx *(-1);
				newVelocity.y = vx *(-1);
			}
		}else{
			if (Math.abs(Math.tan(x/y)) > Math.abs(innerAngle)){
				newVelocity.x = vx *(-1) * (1+Math.abs(Math.tan(x/y)) - Math.abs(innerAngle));
				newVelocity.y = vx *(-1) *(Math.abs(Math.tan(x/y)) - Math.abs(innerAngle));
			}else if (Math.abs(Math.tan(x/y)) > Math.abs(innerAngle)){
				newVelocity.x = vx *(-1) * (Math.abs(Math.tan(x/y)) - Math.abs(innerAngle));
				newVelocity.y = vx *(-1) *(1+Math.abs(Math.tan(x/y)) - Math.abs(innerAngle));
			}else{
				newVelocity.x = vx *(-1);
				newVelocity.y = vx *(-1);
			}
		}
		return newVelocity;
	}
	
	
}