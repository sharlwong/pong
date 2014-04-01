package com.sutd.GameWorld;

import com.badlogic.gdx.math.Rectangle;
import com.sutd.GameObjects.Ball;
import com.sutd.GameObjects.Paddle;
import com.sutd.PongHelpers.Vector2D;

public class GameWorld {

	Paddle player0 = new Paddle(0);
	Paddle player1 = new Paddle(1);
	private int ballNumber = 20;
	private Ball[] balls = new Ball[ballNumber];
	private Rectangle rect = new Rectangle(0, 0, 17, 12);
	private Vector2D screenSize;
	
	// all elements in the array of Ball need to be initialized when GameWorld is created
	// how to initialize balls?
	/* WILL BE INITED BY SERVER */
	// for odd index, balls come from player0 side
	// for even index, balls come from player1 side
	/* OKAY CAN */
	// note that only set the first five elements' isMoving to be true
	private float totalTime = 0;
	private float timeCounter = 0;
	
	public GameWorld(Vector2D screenSize){
		this.screenSize = screenSize;
	}
	
	public Ball[] getBallsArray(){
		return balls;
	}
	
	public Vector2D getScreenSize(){
		return screenSize;
	}

	
	public boolean existDeadBall() {
		for (Ball b : balls)
			if (!b.isAlive()) return true;
		return false;
	}

	public Ball getNextDeadBall() {
		for (Ball b : balls)
			if (!b.isAlive()) return b;
		return null;
	}

	/**
	 * prerequisite: input integer must be either 1 or 0
	 *
	 * @param p
	 * @return
	 */
	public Paddle getPaddle(int p) {
		return (p == 0) ? player0 : player1;
	}

	public Rectangle getRect() {
		return rect;
	}

	/**
	 * initially there will be a certain number of balls (for example, 5 balls)
	 * every second there will be two more balls (each one comes from each side)
	 * when the total number of alive balls reached 20 (for example), there will be no more balls added in
	 * when there are balls stopped, reset those balls
	 *
	 * @param delta
	 */
	public void update(float delta) {
		System.out.println("GameWorld - update");
		timeCounter = timeCounter + delta;
		if (timeCounter / 1 != 0) {
			timeCounter -= 1;
			if (existDeadBall()) getNextDeadBall().restart();
			if (existDeadBall()) getNextDeadBall().restart();
		}
		rect.x++;
		if (rect.x > 137) rect.x = 0;
		for (Ball b : balls) {
			if (player0.collisionCheck(b)) {
				b = b.paddleReflect(b.newVelocityAfterReflection(player0));
			} else if (player1.collisionCheck(b)) {
				b = b.paddleReflect(b.newVelocityAfterReflection(player1));
			}
		}
	}
}
