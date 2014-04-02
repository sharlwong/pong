package com.sutd.GameObjects;

import com.sutd.PongHelpers.Vector2D;

public class ObjectTest {
	public static void main(String[] args){
		Vector2D startPosition = new Vector2D(0.5, 0);
		Vector2D startVelocity =  new Vector2D(0.1, 0.1);
		long realStartTime = 0;
		Ball b = new Ball(startPosition, startVelocity, realStartTime);
		
		Paddle p = new Paddle(0);
		p.setPosition(0.5);
		for (int i = 0; i<500; i++){
			long currentTime = ((long) i);
			System.out.println(currentTime+ ": "+ b.getPosition(currentTime).x+" "+b.getPosition(currentTime).y);
			if (p.collisionCheck(b)){
				System.out.println("collide!");
				b = b.paddleReflect(b.newVelocityAfterReflection(p));
			}
		}
	}
}
