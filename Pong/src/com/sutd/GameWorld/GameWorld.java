package com.sutd.GameWorld;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.sutd.GameObjects.Ball;
import com.sutd.GameObjects.Paddle;

public class GameWorld {
	
	private Rectangle rect = new Rectangle(0, 0, 17, 12);
	Paddle player0 = new Paddle(0);
	Paddle player1 = new Paddle(1);
	private int ballNumber = 20;
	private Ball[] balls = new Ball[ballNumber];	// all elements in the array of Ball need to be initialized when GameWorld is created
	//how to initialize balls?
	// for odd index, balls come from player0 side
	// for even index, balls come from player1 side
													// note that only set the first five elements' isMoving to be true
	private float totalTime = 0;
	private float timeCounter = 0;
	

	/**
	 * initially there will be a certain number of balls (for example, 5 balls)
	 * every second there will be two more balls (each one comes from each side)
	 * when the total number of alive balls reached 20 (for example), there will be no more balls added in
	 * when there are balls stopped, reset those balls
	 * @param delta
	 */
	public void update(float delta) {
		System.out.println("GameWorld - update");
		timeCounter = timeCounter + delta;
		if (timeCounter/1!=0){
			timeCounter -= 1;
			if (existDeadBall()){
				getNextDeadBall().restart();
			}
			if (existDeadBall()){
				getNextDeadBall().restart();
			}
		}
        rect.x++;
        if (rect.x > 137) {
            rect.x = 0;
        }
        for (Ball b: balls){
        	if (player0.collisionCheck(b)){
        		b = b.paddleReflect(b.newVelocityAfterReflection(player0));
        	}else if(player1.collisionCheck(b)){
        		b = b.paddleReflect(b.newVelocityAfterReflection(player1));
        	}
        }
    }
	
	public Rectangle getRect() {
        return rect;
    }
	
	/**
	 * prerequisite: input integer must be either 1 or 0
	 * @param p
	 * @return
	 */
	public Paddle getPaddle(int p){
		if (p == 0){
			return player0;
		}else{
			return player1;
		}
	}
	
	public boolean existDeadBall(){
		boolean result = false;
		for (Ball b: balls){
			if (!b.isAlive()){
				result = true;
			}
		}
		return result;
	}
	
	public Ball getNextDeadBall(){
		for (Ball b: balls){
			if (!b.isAlive()){
				return b;
			}
		}
		return null;
	}
	

}
