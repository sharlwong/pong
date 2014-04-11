package com.sutd.PongHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
	
	public static Texture texture;
	public static TextureRegion salmonSushi, riceCracker, fishCake, octopusSmile, octopusGasp;
	public static Animation octopusAnimation;
	
	public static void load(){
		font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
		font.setScale(.25f, -.25f);
		shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
		shadow.setScale(.25f, -.25f);
		
		texture = new Texture(Gdx.files.internal("data/Pong_texture_4.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
	
		octopusSmile = new TextureRegion(texture, 0, 0, 128, 128);
		octopusSmile.flip(false, true);
		
		fishCake = new TextureRegion(texture, 128, 0, 128, 128);
		fishCake.flip(false, true);
		
		salmonSushi = new TextureRegion(texture, 256, 0, 128, 128);
		salmonSushi.flip(false, true);
		
		riceCracker = new TextureRegion(texture, 384, 0, 128, 128);
		riceCracker.flip(false, true);
		
//		TextureRegion[] octopus = { octopusGasp, octopusSmile };
//		octopusAnimation = new Animation(0.06f, octopus);
//		octopusAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		
	
	}
	
	public void dispose(){
		font.dispose();
		texture.dispose();
	}

	public static class LagException extends RuntimeException {
	}
}
