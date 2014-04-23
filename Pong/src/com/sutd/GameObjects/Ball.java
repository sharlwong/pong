package com.sutd.GameObjects;

import com.sutd.PongHelpers.Constants;
import com.sutd.PongHelpers.Vector2D;

/**
 * Ball is a game object. It will be rendered as fruit during the game play.
 * Its attributes includes initialTime, initialVelocity, initialPosition, currentPosition and ballType.
 * For the ease of communication, the moving track of each ball will be calculated locally within the
 * constraint of synchronization.
 * ballType determines the way it will be rendered and it's score value.
 */

public class Ball {
	private long     initTime;
	private Vector2D velocity;
	private Vector2D initialPosition;
	private Vector2D currentPosition;
	private int      type;
	private double   speedMultiplier;

	public Ball(Vector2D startPosition, Vector2D startVelocity, long startTimeMillis, int type) {
		this(startPosition, startVelocity, startTimeMillis, type, 1)
	}

	public Ball(Vector2D startPosition, Vector2D startVelocity, long startTimeMillis, int type, double speedMultiplier) {
		this.initialPosition = startPosition;
		this.initTime = startTimeMillis;
		this.initTime = startTimeMillis;
		this.type = type;
		this.speedMultiplier = speedMultiplier;
		
		/* widen angle */
		startVelocity.x *= Constants.ANGLE_WIDENER;

		/* error correct starting speed, leaving the direction equal */
		Vector2D initialVelocity = startVelocity.cpy().makeUnitVector().multiply(Constants.BALL_SPEED).multiply(speedMultiplier);

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

	/**
	 * Update ball's position and velocity after the input amount of time.
	 *
	 * @param currentTimeMillis After this amount of time, the position and velocity of the ball
	 *                          will be calculated.
	 */
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

	/**
	 * Check this ball is still in game or not.
	 * "In Game" is defined as moving within the game (screen) region.
	 *
	 * @return true if the ball is still in game, false otherwise.
	 */
	public boolean inGame() {
		if (currentPosition.y < (0 - Constants.PADDLE_EFFECTIVE_DEPTH - Constants.EDGE_PADDING) && velocity.y < 0)
			return false;
		if (currentPosition.y > (Constants.HEIGHT + Constants.PADDLE_EFFECTIVE_DEPTH + Constants.EDGE_PADDING) && velocity.y > 0)
			return false;
		return true;
	}

	/**
	 * Check if the ball is moving upwards (actually downwards as it is shown on the screen)
	 *
	 * @return true if the ball is moving upward (y value is increasing)
	 */
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