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
	
	public static Texture japanese_texture, fruitball_texture, screen_texture;
	public static TextureRegion watermelon, orange, kiwi, salmonSushi, riceCracker, fishCake, octopusSmile, octopusGasp, splash_screen;
	public static Animation octopusAnimation;
	
//	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
	
	public static void load(){
		
//		font = TrueTypeFontFactory.createBitmapFont(Gdx.files.internal("font.ttf"), FONT_CHARACTERS, 12.5f, 7.5f, 1.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		font.setColor(1f, 0f, 0f, 1f);
		
		font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
		font.setScale(.25f, -.25f);
		shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
		shadow.setScale(.25f, -.25f);
		
		fruitball_texture = new Texture(Gdx.files.internal("data/Fruitball_Texture.png"));
		fruitball_texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		screen_texture = new Texture(Gdx.files.internal("data/Splash_screen_wo_strokes_256x512.png"));
		screen_texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
				
		japanese_texture = new Texture(Gdx.files.internal("data/Pong_texture_4.png"));
		japanese_texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
	
		octopusSmile = new TextureRegion(japanese_texture, 0, 0, 128, 128);
		octopusSmile.flip(false, true);
		
		fishCake = new TextureRegion(japanese_texture, 128, 0, 128, 128);
		fishCake.flip(false, true);
		
		salmonSushi = new TextureRegion(japanese_texture, 256, 0, 128, 128);
		salmonSushi.flip(false, true);
		
		riceCracker = new TextureRegion(japanese_texture, 384, 0, 128, 128);
		riceCracker.flip(false, true);
		
		watermelon = new TextureRegion(fruitball_texture, 0, 0, 128, 128);
		watermelon.flip(false, true);
		
		orange = new TextureRegion(fruitball_texture, 128, 0, 128, 128);
		orange.flip(false, true);
		
		kiwi = new TextureRegion(fruitball_texture, 256, 0, 128, 128);
		kiwi.flip(false, true);
		
		splash_screen = new TextureRegion(screen_texture, 0, 0, 256, 512);
		splash_screen.flip(false, true);
		
//		TextureRegion[] octopus = { octopusGasp, octopusSmile };
//		octopusAnimation = new Animation(0.06f, octopus);
//		octopusAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		
	
	}
	
	public void dispose(){
		font.dispose();
		japanese_texture.dispose();
	}

	public static class LagException extends RuntimeException {
	}
}
