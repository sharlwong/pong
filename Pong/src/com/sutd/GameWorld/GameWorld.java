package com.sutd.GameWorld;

import com.badlogic.gdx.math.Rectangle;
import com.sutd.GameObjects.Paddle;

public class GameWorld {
	
	private Rectangle rect = new Rectangle(0, 0, 17, 12);
	Paddle player0 = new Paddle(0);
	Paddle player1 = new Paddle(1);
	

	
	public void update() {
        System.out.println("GameWorld - update");
        rect.x++;
        if (rect.x > 137) {
            rect.x = 0;
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
	

}
