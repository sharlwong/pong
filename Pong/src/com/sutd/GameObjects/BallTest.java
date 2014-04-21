package com.sutd.GameObjects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.SecureRandom;

import org.junit.Test;

import com.sutd.PongHelpers.Constants;
import com.sutd.PongHelpers.Vector2D;

public class BallTest {

	Ball testBall;
	int ballType;
	Vector2D position;
	
	public BallTest(){
		SecureRandom random;
		long elapsedTimeMillis;
		Vector2D speed;
		elapsedTimeMillis = 0;
		random = new SecureRandom();
		random.setSeed(1234567890);
		/* ball type and other data */
		double randomValue = random.nextDouble() * 14;
		ballType = (int) randomValue;

		/* starting position */
		position = new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2);

		/* randomize starting velocity within 45 degree */
		double speed1 = random.nextDouble() - 0.5;
		double speed2 = random.nextDouble() - 0.5;
		if (Math.abs(speed1) < Math.abs(speed2))
			speed = new Vector2D(speed1, speed2);
		else
			speed = new Vector2D(speed2, speed1);
		testBall = new Ball(position, speed, elapsedTimeMillis, randomValue,
				ballType);
	}

	@Test
	public void testGetType() {
		assertEquals(ballType, testBall.getType());
	}
	
	@Test
	public void getCurrentPosition(){
		/* start position is current position if no update happened */
		assertEquals(position, testBall.getCurrentPosition());
		
		/* start position is current position if update 0 millisecond */
		testBall.updateCurrentTime(0);
		assertEquals(position, testBall.getCurrentPosition());
	}
	
	@Test
	public void testMovement() {
		Ball testBall2 = new Ball(new Vector2D(0, 0), new Vector2D(0, 1), 0, 0, 1);
		testBall2.updateCurrentTime(10);
		/* startvelocity only represent direction, while the speed is always 0.0005 */
		Vector2D expectedPosition = new Vector2D(0, Constants.BALL_SPEED * 10);
		System.out.println(testBall2.getCurrentPosition().y);
		assertEquals(expectedPosition, testBall2.getCurrentPosition());
		testBall2 = new Ball(new Vector2D(0, 0), new Vector2D(1, 0), 0, 0, 1);
		testBall2.updateCurrentTime(10);
		/* error input handler test, if velocity in y direction is 0,  */
		assertEquals(expectedPosition, testBall2.getCurrentPosition());
	}
	
	@Test
	public void testIsMovingUp() {
		Ball testBall2 = new Ball(new Vector2D(0, 0), new Vector2D(0, 1), 0, 0, 1);
		assertTrue(testBall2.isMovingUp());
		testBall2 = new Ball(new Vector2D(0, 0), new Vector2D(0, -1), 0, 0, 1);
		assertFalse(testBall2.isMovingUp());
		testBall2 = new Ball(new Vector2D(0, 0), new Vector2D(0, 0), 0, 0, 1);
		/* input error handler */
		assertTrue(testBall2.isMovingUp());
	}


}
