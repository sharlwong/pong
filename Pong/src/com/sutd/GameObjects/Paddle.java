package com.sutd.GameObjects;

import com.sutd.PongHelpers.Constants;
import com.sutd.PongHelpers.Vector2D;

/**
 * Paddle is a game object in this game. Paddle will be controlled by players during
 * the game play. Meanwhile, it is also responsible for collision check and score
 * updating. Paddle has two types, which are the bottom one and the top one respectively.
 * Functions related to position setting will be called in InputHandler. **
 */

public class Paddle {
	private double max   = Constants.WIDTH + Constants.BALL_RADIUS - (Constants.PADDLE_WIDTH / 2);
	private double min   = (Constants.PADDLE_WIDTH / 2) - Constants.BALL_RADIUS;
	private int    score = 0;
	private Vector2D paddleCenter;
	public  boolean  playerBottom;

	public Paddle(int playerNum) {
		paddleCenter = new Vector2D(-1, Constants.HEIGHT * playerNum);
		setFractionalPosition(0.5);
		this.playerBottom = (playerNum == 0);
	}

	/**
	 * This function will be called when collision happens.
	 *
	 * @param b                 Ball before collision
	 * @param currentTimeMillis Time when collision happens
	 * @return The new ball with new velocity and initialized starting time after collision
	 */
	public Ball bounce(Ball b, long currentTimeMillis) {
		double yVelocity = Constants.PADDLE_WIDTH / 2;
		if (!playerBottom) yVelocity = 0 - yVelocity;
		Vector2D outVelocity = new Vector2D(b.getCurrentPosition().x - paddleCenter.x, yVelocity);
		outVelocity.makeUnitVector().multiply(Constants.BALL_SPEED);
		return new Ball(b.getCurrentPosition(), outVelocity, currentTimeMillis, b.getType(), b.getSpeedMultiplier());
	}

	/**
	 * Check whether collision happens. By checking paddle position and ball position.
	 *
	 * @param b Ball b
	 * @return True if collision detected and false otherwise.
	 */
	public boolean collisionCheck(Ball b) {
		Vector2D ballPosition = b.getCurrentPosition();
		boolean up = b.isMovingUp();
		if (Math.abs(ballPosition.x - paddleCenter.x) > (Constants.PADDLE_WIDTH / 2) + Constants.BALL_RADIUS)
			return false;
		if (playerBottom && ballPosition.y < (0 - Constants.PADDLE_EFFECTIVE_DEPTH) && !up) return false;
		if (playerBottom && ballPosition.y < 0 && !up) return true;
		if (!playerBottom && ballPosition.y > (Constants.HEIGHT + Constants.PADDLE_EFFECTIVE_DEPTH) && up) return false;
		if (!playerBottom && ballPosition.y > (Constants.HEIGHT) && up) return true;
		return false;
	}

	/**
	 * When a ball passes paddle, score will be increased.
	 * Type: 0	Orange		Score: 1
	 * Type: 1	Kiwi		Score: 2
	 * Type: 2  Watermelon	Score: 3
	 *
	 * @param b Ball
	 */
	public void incrementScore(Ball b) {
		score += b.getScore();
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
	 * @return paddle center coordinates.
	 */
	public double[] getXY() {
		return new double[]{paddleCenter.x, paddleCenter.y};
	}

	/**
	 * @return paddle center coordinates in the form of Vector2D
	 */
	public Vector2D getCenter() {
		return paddleCenter;
	}

	/**
	 * @return current score of the paddle controller
	 */
	public int getScore() {
		return score;
	}

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
}
