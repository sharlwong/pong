package com.sutd.GameWorld;

import com.badlogic.gdx.math.Rectangle;

public class GameWorld {
	
	private Rectangle rect = new Rectangle(0, 0, 17, 12);
	
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

}
