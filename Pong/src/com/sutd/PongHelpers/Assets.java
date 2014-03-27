package com.sutd.PongHelpers;

/**
 * Created by avery_000 on 25-Mar-14.
 */
public abstract class Assets {
	/* this is a ball */
	public final static double BALL_RADIUS = 0.02; // distance units
	public final static double BALL_SPEED = 0.003; // distance-units per millisecond

	/* by default paddle will be one-tenth of the screen
	 * note though that the screen will have an extra ball-radius at the end, so a bit extra complication there
	 */
	public final static double PADDLE_WIDTH = 0.1;
	public final static double PADDLE_EFFECTIVE_DEPTH = BALL_RADIUS * 0.75;

	/* this is the size of the board on which the point-mass balls move */
	public final static double HEIGHT = 1; // distance-units
	public final static double WIDTH = 1; // distance-units

	/* this is how big the display will be
	 * because the math treats the ball as a point mass
	 * one ball-radius is added to each edge
	 * the paddles must render above and under this padding
	 */
	public final static double DISPLAY_HEIGHT = HEIGHT + 2 * BALL_RADIUS;
	public final static double DISPLAY_WIDTH = WIDTH + 2 * BALL_RADIUS;

	/* special exception for extreme lag */
	public final static long MAX_ACCEPTABLE_LAG = 500; // milliseconds
	public static class LagException extends RuntimeException {
	}
}
