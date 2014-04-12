package com.sutd.GameObjects;

/** Tian Chi's version **/

import com.badlogic.gdx.Gdx;
import com.sutd.PongHelpers.Assets;
import com.sutd.PongHelpers.Constants;
import com.sutd.PongHelpers.Vector2D;

/**
 * Created by avery_000 on 3/23/14.
 */
public class Paddle {
	public boolean playerBottom;
	private double max = Constants.WIDTH + Constants.BALL_RADIUS - (Constants.PADDLE_WIDTH / 2);
	private double min = (Constants.PADDLE_WIDTH / 2) - Constants.BALL_RADIUS;
	private Vector2D paddleCenter;
	private int score = 0;

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
		return new Ball(b.getCurrentPosition(), outVelocity, currentTimeMillis, b.getUnusedVariable(), b.getType());
	}

	public boolean collisionCheck(Ball b) {
		Vector2D ballPosition = b.getCurrentPosition();
		boolean up = b.isMovingUp();
		if (Math.abs(ballPosition.x - paddleCenter.x) > (Constants.PADDLE_WIDTH / 2)) return false;
		if (playerBottom && ballPosition.y < (0 - Constants.PADDLE_EFFECTIVE_DEPTH) && !up) return false;
		if (playerBottom && ballPosition.y < 0 && !up) return true;
		if (!playerBottom && ballPosition.y > (Constants.HEIGHT + Constants.PADDLE_EFFECTIVE_DEPTH) && up) return false;
		if (!playerBottom && ballPosition.y > (Constants.HEIGHT) && up) return true;
		return false;
	}

	public Vector2D getCenter() {
		return paddleCenter;
	}

	public int getScore() {
		return score;
	}

	public void incrementScore(Ball b) {
		score += b.getType();
	}

	public void setFractionalPosition(double fraction) {
		if (fraction < 0 || fraction > 1) return;
		setPosition(min + fraction * (max - min));
	}

	public double[] getXY() {
		return new double[]{paddleCenter.x, paddleCenter.y};
	}

	private void setPosition(double xValue) {
		paddleCenter.x = xValue;
		if (paddleCenter.x < min) paddleCenter.x = min;
		if (paddleCenter.x > max) paddleCenter.x = max;
	}

	//	public double getFractionalPosition() {
	//		return (paddleCenter.x - min)/(max-min);
	//	}
	//	public double getTransformedFractionalPosition(int player) {
	//		return Math.abs(player - getFractionalPosition());
	//	}
}
