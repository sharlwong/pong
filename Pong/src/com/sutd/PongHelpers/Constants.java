package com.sutd.PongHelpers;



import java.awt.*;

/**
 * Created by avery_000 on 25-Mar-14.
 */
public class Constants {

	/* this is the square unit-length board on which the point-mass balls move about */
	public final static double HEIGHT = 1;
	public final static double WIDTH = 1;

	/* 25 balls (26 including the buffer) fit vertically, but not necessarily sideways, due to XY scaling */
	public final static double BALL_RADIUS = 0.02;

	/* these are the distances for the paddles */
	public final static double EDGE_PADDING = BALL_RADIUS / 2;
	public final static double PADDLE_EFFECTIVE_DEPTH = BALL_RADIUS * 1.5;

	/* this is how big the display will be
	 * because the math treats the ball as a point mass
	 * one ball-radius is added to each edge
	 * the paddles must render above and under this padding
	 */
	public final static double DISPLAY_HEIGHT = HEIGHT + 2 * BALL_RADIUS + 2 * PADDLE_EFFECTIVE_DEPTH + 2 * EDGE_PADDING;
	public final static double BALL_SPEED = 0.001; // distance-units per millisecond
	/* special exception for extreme lag */
	public final static long MAX_ACCEPTABLE_LAG = 500; // milliseconds
	/* by default paddle will be one-tenth of the screen
	 * note though that the screen will have an extra ball-radius at the end, so a bit extra complication there
	 */
	public final static double PADDLE_WIDTH = 0.3;

	private final Dimension dim;

	private final double verticalFractionalPadding;
	private final double horizontalFractionalPadding;
	private final double verticalPixelUnitLength;
	private final double horizontalPixelUnitLength;
	private final double ballPixelRadius;
	private final double paddlePixelWidth;
	private final double paddlePixelDepth;
	private final double edgePixelPadding;

	public Constants(Dimension dimension) {
		this.dim = dimension;

		/* find vertical distances */
		verticalFractionalPadding = EDGE_PADDING + PADDLE_EFFECTIVE_DEPTH + BALL_RADIUS;
		verticalPixelUnitLength = ((double) dim.height) / (HEIGHT + 2 * verticalFractionalPadding);
		ballPixelRadius = verticalPixelUnitLength * (BALL_RADIUS);
		paddlePixelDepth = verticalPixelUnitLength * PADDLE_EFFECTIVE_DEPTH;
		edgePixelPadding = verticalPixelUnitLength * EDGE_PADDING;

		/* find horizontal distances */
		horizontalPixelUnitLength = ((double) dim.width) - 2 * ballPixelRadius;
		horizontalFractionalPadding = ballPixelRadius / horizontalPixelUnitLength;
		paddlePixelWidth = PADDLE_WIDTH * horizontalPixelUnitLength;
	}

	public double getVerticalFractionalPadding() {
		return verticalFractionalPadding;
	}

	public double getHorizontalFractionalPadding() {
		return horizontalFractionalPadding;
	}

	public double getVerticalPixelUnitLength() {
		return verticalPixelUnitLength;
	}

	public double getHorizontalPixelUnitLength() {
		return horizontalPixelUnitLength;
	}

	public double getBallPixelRadius() {
		return ballPixelRadius;
	}

	public double getPaddlePixelWidth() {
		return paddlePixelWidth;
	}

	public double getPaddlePixelDepth() {
		return paddlePixelDepth;
	}

	public double getEdgePixelPadding() {
		return edgePixelPadding;
	}

	public Dimension translateBallReferenceFrame(Vector2D v) {
		/* note that v is in small square reference frame of point-mass balls; do not modify v */
		double x = v.x;
		double y = v.y;

		/* translation */
		x += verticalFractionalPadding;
		y += horizontalFractionalPadding;

		/* scaling */
		x /= (HEIGHT + 2 * verticalFractionalPadding);
		y /= (HEIGHT + 2 * horizontalFractionalPadding);

		/* flip upright */
		y = 1.0 - y;

		/* change units */
		x *= dim.width;
		y *= dim.height;

		/* convert to dimension */
		return new Dimension((int) x, (int) y);
	}

	public static class LagException extends RuntimeException {
	}
}
