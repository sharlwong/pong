package com.sutd.GameObjects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sutd.PongHelpers.Constants;
import com.sutd.PongHelpers.Vector2D;

public class PaddleTest {

	Paddle testPaddle0;
	Paddle testPaddle1;
	
	public PaddleTest() {
		testPaddle0 = new Paddle(0);
		testPaddle1 = new Paddle(1);
	}

	@Test
	public void testBounce() {
		Vector2D startPosition = new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2);
		Ball testBall = new Ball(startPosition, new Vector2D(0, -1), 0, 1, 1);
		/* after bounce, the y direction of testBall will be changed into opposite direction */
		assertFalse(testBall.isMovingUp());
		assertTrue(testPaddle0.bounce(testBall, 0).isMovingUp());
		//fail("Not yet implemented");
	}

	@Test
	public void testCollisionCheck() {
		Vector2D startPosition = new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2);
		Ball testBall = new Ball(startPosition, new Vector2D(0, -1), 0, 1, 1);
		assertFalse(testPaddle0.collisionCheck(testBall));
	}

	@Test
	public void testGetCenter() {
		Vector2D topCenter = new Vector2D(Constants.WIDTH / 2, 0);
		Vector2D bottomCenter = new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT);
		assertEquals(testPaddle0.getCenter(), topCenter);
		assertEquals(testPaddle1.getCenter(), bottomCenter);
	}

	@Test
	public void testGetScore() {
		/* initially score will be zero */
		assertEquals(testPaddle0.getScore(), 0);
		assertEquals(testPaddle1.getScore(), 0);
	}

	@Test
	public void testIncrementScore() {
		Vector2D startPosition = new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2);
		int sum = 0;
		for (int i = 0; i< 10; i++){
			Ball testBall = new Ball(startPosition, new Vector2D(0, -1), 0, (int) (Math.random() * 10), 1);
			sum += testBall.getScore();
			testPaddle0.incrementScore(testBall);
		}
		assertEquals(testPaddle0.getScore(), sum);
	}

	@Test
	public void testSetFractionalPosition() {
		double max = Constants.WIDTH + Constants.BALL_RADIUS - (Constants.PADDLE_WIDTH / 2);
		double min = (Constants.PADDLE_WIDTH / 2) - Constants.BALL_RADIUS;
		double position = Math.random();
		testPaddle0.setFractionalPosition(position);
		assertEquals(testPaddle0.getCenter(), new Vector2D(min + (max - min) * position, 0));
		position = 0;
		testPaddle0.setFractionalPosition(position);
		assertEquals(testPaddle0.getCenter(), new Vector2D(min + (max - min) * position, 0));
		position = 1;
		testPaddle0.setFractionalPosition(position);
		assertEquals(testPaddle0.getCenter(), new Vector2D(min + (max - min) * position, 0));
	}

	@Test
	public void testGetFractionalPosition() {
		double max = Constants.WIDTH + Constants.BALL_RADIUS - (Constants.PADDLE_WIDTH / 2);
		double min = (Constants.PADDLE_WIDTH / 2) - Constants.BALL_RADIUS;
		double position = Math.random();
		testPaddle0.setFractionalPosition(position);
		assertEquals(testPaddle0.getCenter(), new Vector2D(min + (max - min) * testPaddle0.getFractionalPosition(), 0));
		position = 0;
		testPaddle0.setFractionalPosition(position);
		assertEquals(testPaddle0.getCenter(), new Vector2D(min + (max - min) * testPaddle0.getFractionalPosition(), 0));
		position = 1;
		testPaddle0.setFractionalPosition(position);
		assertEquals(testPaddle0.getCenter(), new Vector2D(min + (max - min) * testPaddle0.getFractionalPosition(), 0));
	}

	@Test
	public void testGetTransformedFractionalPosition() {
		double max = Constants.WIDTH + Constants.BALL_RADIUS - (Constants.PADDLE_WIDTH / 2);
		double min = (Constants.PADDLE_WIDTH / 2) - Constants.BALL_RADIUS;
		double position = 0;
		testPaddle0.setFractionalPosition(position);
		assertEquals(new Vector2D(max, 0), new Vector2D(min + (max - min) * testPaddle0.getTransformedFractionalPosition(1), 0));
		position = 1;
		testPaddle0.setFractionalPosition(position);
		assertEquals(new Vector2D(min, 0), new Vector2D(min + (max - min) * testPaddle0.getTransformedFractionalPosition(1), 0));
	}


}
