package com.sutd.PongHelpers;

import com.sutd.PongHelpers.Dimension;

import java.util.Collection;

import com.sutd.GameObjects.GameState;

/**
 * Created by avery_000 on 25-Mar-14.
 */
public class Constants {

	/* this is the square unit-length board on which the point-mass balls move about */
	public final static double HEIGHT = 1;
	public final static double WIDTH  = 1;

	/* some number of balls fit vertically, but not necessarily sideways, due to XY scaling */
	public final static double BALL_RADIUS = 0.06;

	/* these are the distances for the vertical buffers */
	public final static double EDGE_PADDING           = 0.01; // Previous value: 0.03 (recorded in case)
	public final static double PADDLE_EFFECTIVE_DEPTH = 0.06; // Previous value:  0.03 (recorded in case)

	/* this is how big the display will be
	 * because the math treats the ball as a point mass
	 * one ball-radius is added to each edge
	 * the paddles must render above and under this padding
	 */
	public final static double DISPLAY_HEIGHT = HEIGHT + 2 * BALL_RADIUS + 2 * PADDLE_EFFECTIVE_DEPTH + 2 * EDGE_PADDING;
	public final static double BALL_SPEED     = 0.0005;

	/* by default paddle will be one-tenth of the screen
	 * note though that the screen will have an extra ball-radius at the end, so a bit extra complication there
	 */
	public final static double PADDLE_WIDTH = 0.3;

	/* delay appearance of first ball by this much to give the user time to prepare */
	public final static double START_GAME_DELAY = 300;
	private final Dimension screen;
	private final double    verticalFractionalPadding;
	private final double    horizontalFractionalPadding;
	private final double    verticalPixelUnitLength;
	private final double    horizontalPixelUnitLength;
	private final double    ballPixelRadius;
	private final double    paddlePixelWidth;
	private final double    paddlePixelDepth;
	private final double    edgePixelPadding;

	/* Speed of rendering, gameworld updating and buffer size respectively */
	public final static int FPS                     = 50;
	public final static int UPDATE_DELTA            = 16;
	public final static int STATE_BUFFER_SIZE       = 1;
	public final static int COUNT_DOWN_SECOND       = 10;
	public final static int AGAIN_COUNT_DOWN_SECOND = 2;
	public final static int GAME_TIME               = 20;

	public Constants(Dimension d) {
		this.screen = d;

		/* find vertical distances */
		verticalFractionalPadding = EDGE_PADDING + PADDLE_EFFECTIVE_DEPTH + BALL_RADIUS;
		verticalPixelUnitLength = ((double) screen.height) / (HEIGHT + 2 * verticalFractionalPadding);
		ballPixelRadius = verticalPixelUnitLength * BALL_RADIUS;
		paddlePixelDepth = verticalPixelUnitLength * PADDLE_EFFECTIVE_DEPTH;
		edgePixelPadding = verticalPixelUnitLength * EDGE_PADDING;

		/* find horizontal distances */
		horizontalPixelUnitLength = ((double) screen.width) - 2 * ballPixelRadius;
		horizontalFractionalPadding = ballPixelRadius / horizontalPixelUnitLength;
		paddlePixelWidth = PADDLE_WIDTH * horizontalPixelUnitLength;
	}

	public Dimension getDim() {
		return screen;
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

	//	public Dimension translateBallReferenceFrame(Vector2D v) {
	//		return translateBallReferenceFrame(new double[]{v.x, v.y});
	//	}

	private Dimension translateBallReferenceFrame(double[] ball) {
		/* note that v is in small square reference frame of point-mass balls; do not modify v */
		double x = ball[0];
		x = x < 0 ? 0 : x;
		x = x > 1 ? 1 : x;
		double y = ball[1];

		/* translation */
		y += verticalFractionalPadding;
		x += horizontalFractionalPadding;

		/* scaling */
		y /= (HEIGHT + 2 * verticalFractionalPadding);
		x /= (WIDTH + 2 * horizontalFractionalPadding);

		/* flip upright */
		y = 1.0 - y;

		/* change units */
		x *= screen.width;
		y *= screen.height;

		/* convert to dimension */
		return new Dimension((int) x, (int) y);
	}

	public int[][] makeBallXYs(double[][] ballsData) {
		int[][] out = new int[ballsData.length][2];
		for (int i = 0; i < ballsData.length; i++) {
			Dimension temp = translateBallReferenceFrame(ballsData[i]);
			out[i][0] = temp.width;
			out[i][1] = temp.height;
		}
		return out;
	}

	public int[] makePaddleXY(double[] paddle, int player) {
		int[] out = new int[2];
		Dimension temp = translateBallReferenceFrame(paddle);
		out[0] = temp.width;
		out[1] = (int) (getEdgePixelPadding() + getPaddlePixelDepth() / 2);
		if (player == 0) out[1] = screen.height - out[1];

		return out;
	}

	//	public int[] makeScores(double[][] state) {
	//		double[] temp = state[state.length - 1];
	//		return new int[]{(int) temp[0], (int) temp[1]};
	//	}

	//	public static class LagException extends RuntimeException {
	//	}
}
