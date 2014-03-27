package com.sutd.PongHelpers;

/**
 * Created by avery_000 on 25-Mar-14.
 */
public abstract class Assets {
	public final static double BALL_SPEED = 0.003; // distance-units per millisecond
	public final static double BALL_RADIUS = 0.02; // distance units
	public final static double PADDLE_EFFECTIVE_DEPTH = BALL_RADIUS * 0.75;
	public final static double HEIGHT = 1; // distance-units
	public final static double DISPLAY_HEIGHT = HEIGHT + 2 * BALL_RADIUS;
	public final static long MAX_ACCEPTABLE_LAG = 500; // millis
	public final static double PADDLE_WIDTH = 0.1;
	public final static double WIDTH = 1; // distance-units
	public final static double DISPLAY_WIDTH = WIDTH + 2 * BALL_RADIUS;

	public static class LagException extends RuntimeException {
	}
}
