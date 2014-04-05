package com.sutd.PongHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by avery_000 on 25-Mar-14.
 */
public abstract class AssetLoader {
	public final static double BALL_SPEED = 0.003; // distance-units per millisecond
	public final static double BALL_RADIUS = 0.02; // distance units
	public final static double PADDLE_EFFECTIVE_DEPTH = BALL_RADIUS * 0.75;
	public final static double HEIGHT = 1; // distance-units
	public final static double DISPLAY_HEIGHT = HEIGHT + 2 * BALL_RADIUS;
	public final static long MAX_ACCEPTABLE_LAG = 500; // millis
	public final static double PADDLE_WIDTH = 0.1;
	public final static double WIDTH = 1; // distance-units
	public final static double DISPLAY_WIDTH = WIDTH + 2 * BALL_RADIUS;
	
	public static BitmapFont font, shadow;
	
	public static void load(){
		font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
		font.setScale(.25f, -.25f);
		shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
		shadow.setScale(.25f, -.25f);
	}
	
	public void dispose(){
		font.dispose();
	}

	public static class LagException extends RuntimeException {
	}
}
