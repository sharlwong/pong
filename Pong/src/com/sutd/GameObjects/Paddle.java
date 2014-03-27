package com.sutd.GameObjects;

import com.badlogic.gdx.Gdx;
import com.sutd.PongHelpers.Assets;
import com.sutd.PongHelpers.Vector2D;

/**
 * Created by avery_000 on 3/23/14.
 */
public class Paddle {
	private Vector2D centerPoint;
	private Vector2D velocity;
	private boolean playerBottom;
	private int score = 0;

	public Paddle(int playerNum) {
		//assumes binary player ID number: 0 at bottom, 1 at top
		centerPoint = new Vector2D(Assets.WIDTH / 2, Assets.HEIGHT * playerNum);
		this.playerBottom = (playerNum == 0);
	}

	public Ball bounce(Ball b) {
		double yDist = Assets.PADDLE_WIDTH / 2;
		if (!playerBottom) yDist = 0 - yDist;
		Vector2D outVelocity = new Vector2D(b.getCurrentPosition().x - centerPoint.x, yDist);
		outVelocity.makeUnitVector().multiply(Assets.BALL_SPEED);
		return b.paddleReflect(outVelocity);
	}

	public boolean collisionCheck(Ball b) {
		Vector2D ballPosition = b.getCurrentPosition();
		if (Math.abs(ballPosition.x - centerPoint.x) > Assets.PADDLE_WIDTH) return false;
		if (playerBottom) {
			if (ballPosition.y > 0) return false;
			if (ballPosition.y < (0 - Assets.PADDLE_EFFECTIVE_DEPTH)) return false;
		} else {
			if (ballPosition.y < Assets.HEIGHT) return false;
			if (ballPosition.y > (Assets.HEIGHT + Assets.PADDLE_EFFECTIVE_DEPTH)) return false;
		}
		return true;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void incrementScore() {
		this.score++;
	}

	public void setFractionalPosition(double fraction) {
		if (fraction < 0 || fraction > 1) return;
		setPosition((Assets.WIDTH - Assets.PADDLE_WIDTH) * fraction + Assets.PADDLE_WIDTH / 2);
	}

	public void setPosition(double xValue) {
		centerPoint.x = xValue;
		if (centerPoint.x < (Assets.PADDLE_WIDTH / 2)) centerPoint.x = Assets.PADDLE_WIDTH / 2;
		if (centerPoint.x > (Assets.WIDTH - (Assets.PADDLE_WIDTH / 2)))
			centerPoint.x = Assets.WIDTH - (Assets.PADDLE_WIDTH / 2);
	}
	
	/**
	 * This onClick function will be called by inputHandler
	 * When user click, inputHandler will parse the x coordinate to this function.
	 * Intuitively, longer the distance from x to paddle's current position, faster the paddle
	 * will move to the position user clicked.
	 * 
	 */

	public void onClick(int x){
		float screenWidth = Gdx.graphics.getWidth();
		velocity.x = x/screenWidth - centerPoint.x;
	}

	public void update(float delta){

		// process velocity (avoid too large or too slow cases)
		// parameters may need further modification
		if (velocity.x > 0.8) {
			velocity.x = 0.8;
		}
		if (velocity.x < -0.8) {
			velocity.x = -0.8;
		}
		if (velocity.x>0&&velocity.x<0.3){
			velocity.x = 0.3;
		}
		if (velocity.x<0&&velocity.x>-0.3){
			velocity.x = -0.3;
		}

		//side checking
		if ((centerPoint.x < (Assets.PADDLE_WIDTH / 2))|| (centerPoint.x > (Assets.WIDTH - (Assets.PADDLE_WIDTH / 2)))){
			velocity.x = 0;
		}

		centerPoint.add(velocity.cpy().multiply(delta));
	}
	
}