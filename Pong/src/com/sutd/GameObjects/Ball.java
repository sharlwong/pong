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
		this(startPosition, startVelocity, startTimeMillis, type, 1);
	}

	/**
	 * a constructor, to construct a ball
	 *
	 * @param startPosition   where the ball starts
	 * @param startVelocity   direction of travel
	 * @param startTimeMillis time of birth
	 * @param type            its color and weight
	 * @param speedMultiplier how fast it is moving
	 */
	public Ball(Vector2D startPosition, Vector2D startVelocity, long startTimeMillis, int type, double speedMultiplier) {
		this.initialPosition = startPosition;
		this.initTime = startTimeMillis;
		this.type = type;
		this.speedMultiplier = speedMultiplier <= 0 ? 0.1 : speedMultiplier;
		
		/* widen starting velocity angle */
		Vector2D initialVelocity = startVelocity.cpy().multiply(Constants.ANGLE_WIDENER);

		/* error containment */
		if (initialVelocity.y == 0) initialVelocity = Vector2D.Y.cpy();

		/* how far does the ball move to the paddle line */
		double distanceToTravel;
		if (initialVelocity.y > 0)
			distanceToTravel = (Constants.HEIGHT - startPosition.y) * (initialVelocity.length() / Math.abs(initialVelocity.y));
		else distanceToTravel = startPosition.y * (initialVelocity.length() / Math.abs(initialVelocity.y));

		/* when is it supposed to the paddle line, factoring in speed */
		double realTimeTakenMillis = distanceToTravel / (Constants.BALL_SPEED * speedMultiplier);
		long realEndTimeMillis = initTime + (long) realTimeTakenMillis;

		/* how fast must it move to get there on time */
		double realSpeed = distanceToTravel / (realEndTimeMillis - initTime);
		this.velocity = initialVelocity.makeUnitVector().multiply(realSpeed);

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
		/* imaginary position */
		long timeTravelled = currentTimeMillis - initTime;
		timeTravelled = timeTravelled < 0 ? 0 : timeTravelled;
		Vector2D youAreHere = new Vector2D(initialPosition);
		youAreHere.add(velocity.cpy().multiply(timeTravelled));

		/* reflection */
		while (youAreHere.x < 0 || youAreHere.x > Constants.WIDTH) {
			if (youAreHere.x < 0) youAreHere.x = 0 - youAreHere.x;
			if (youAreHere.x > Constants.WIDTH) youAreHere.x = 2 * Constants.WIDTH - youAreHere.x;
		}

		/* done now return */
		currentPosition = youAreHere;
	}

	/**
	 * Check this ball is still in game or not.
	 * "In Game" is defined as moving within the game (screen) region.
	 *
	 * @return true if the ball is still in game, false otherwise.
	 */
	public boolean inGame() {

		/* below bottom line */
		if (currentPosition.y < (0 - Constants.PADDLE_EFFECTIVE_DEPTH - Constants.EDGE_PADDING) && velocity.y < 0)
			return false;

		/* above top line */
		if (currentPosition.y > (Constants.HEIGHT + Constants.PADDLE_EFFECTIVE_DEPTH + Constants.EDGE_PADDING) && velocity.y > 0)
			return false;

		/* within bounds */
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
	 * what kind of ball is it
	 * 0 -> yellow
	 * 1 -> green
	 * 2 -> red
	 *
	 * @return type number
	 */
	public int getType() {
		return type;
	}

	/**
	 * whats the weight of the ball
	 * hint: it's the type plus one
	 *
	 * @return weight
	 */
	public int getScore() {
		return type + 1;
	}

	/**
	 * where is the ball
	 *
	 * @return here it is
	 */
	public Vector2D getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * how fast is the ball
	 *
	 * @return speed relative to default
	 */
	public double getSpeedMultiplier() {
		return speedMultiplier;
	}
}