package com.sutd.PongHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * AssetLoader is a special LibGDX class that is responsible for loading all assets needed in this
 * game. There are several types of assets that our game uses, which are Music, Texture, BitmapFont and 
 * Animation.  
 * ***/
public abstract class AssetLoader {
	public final static double BALL_SPEED             = 0.003; // distance-units per millisecond
	public final static double BALL_RADIUS            = 0.02; // distance units
	public final static double PADDLE_EFFECTIVE_DEPTH = BALL_RADIUS * 0.75;
	public final static double HEIGHT                 = 1; // distance-units
	public final static double DISPLAY_HEIGHT         = HEIGHT + 2 * BALL_RADIUS;
	public final static long   MAX_ACCEPTABLE_LAG     = 500; // millis
	public final static double PADDLE_WIDTH           = 0.1;
	public final static double WIDTH                  = 1; // distance-units
	public final static double DISPLAY_WIDTH          = WIDTH + 2 * BALL_RADIUS;

	public static BitmapFont font, shadow;

	public static Texture fruitball_texture, screen_texture, wait_texture, paddle_texture, game_texture, instr_texture, sound_texture, coco_texture;
	public static TextureRegion watermelon, orange, kiwi, splash_screen, wait_screen, game_screen, story_screen, fruit_pt_screen, paddle_top, paddle_bottom, sound_icon, coco;
	public static Animation octopusAnimation;


	public static Music music, chimp_long, chimp_short, bounce;
	
	/**
	 * Load assets needed for the game.
	 * **/

	public static void load() {

		/** Fonts **/

		font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
		font.setScale(.25f, -.25f);
		shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
		shadow.setScale(.25f, -.25f);

		/** Music and sounds **/

		music = Gdx.audio.newMusic(Gdx.files.internal("data/Jungle Music.mp3"));
		chimp_long = Gdx.audio.newMusic(Gdx.files.internal("data/Excited Chimp.mp3"));
		chimp_short = Gdx.audio.newMusic(Gdx.files.internal("data/Chimp.mp3"));
		bounce = Gdx.audio.newMusic(Gdx.files.internal("data/Spring Bounce.mp3"));

		/** Graphics **/

		fruitball_texture = new Texture(Gdx.files.internal("data/Fruitball_Texture.png"));
		fruitball_texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		sound_texture = new Texture(Gdx.files.internal("data/Mute_icon.png"));
		sound_texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		screen_texture = new Texture(Gdx.files.internal("data/Splash_screen_strokes_2.png"));
		screen_texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		wait_texture = new Texture(Gdx.files.internal("data/Wait_screen.png"));
		wait_texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		paddle_texture = new Texture(Gdx.files.internal("data/Banana.png"));
		paddle_texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		game_texture = new Texture(Gdx.files.internal("data/Game_screen.png"));
		game_texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		instr_texture = new Texture(Gdx.files.internal("data/Combined_instr.png"));
		instr_texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		coco_texture = new Texture(Gdx.files.internal("data/monkey2.png"));
		coco_texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		watermelon = new TextureRegion(fruitball_texture, 0, 0, 128, 128);
		watermelon.flip(false, true);

		orange = new TextureRegion(fruitball_texture, 128, 0, 128, 128);
		orange.flip(false, true);

		kiwi = new TextureRegion(fruitball_texture, 256, 0, 128, 128);
		kiwi.flip(false, true);

		splash_screen = new TextureRegion(screen_texture, 0, 0, 256, 512);
		splash_screen.flip(false, true);

		wait_screen = new TextureRegion(wait_texture, 0, 0, 256, 512);
		wait_screen.flip(false, true);

		game_screen = new TextureRegion(game_texture, 0, 0, 256, 512);
		game_screen.flip(false, true);

		story_screen = new TextureRegion(instr_texture, 0, 0, 256, 512);
		story_screen.flip(false, true);
		
		fruit_pt_screen = new TextureRegion(instr_texture, 256, 0, 256, 512);
		fruit_pt_screen.flip(false, true);

		paddle_top = new TextureRegion(paddle_texture, 0, 0, 128, 41);
		paddle_top.flip(false, false);

		paddle_bottom = new TextureRegion(paddle_texture, 0, 0, 128, 41);
		paddle_bottom.flip(false, true);
		
		sound_icon = new TextureRegion(sound_texture, 0, 0, 32, 32);
		sound_icon.flip(false, true);
		
		coco = new TextureRegion(coco_texture, 0, 0, 280, 300);
		coco.flip(false, true);
	}

	/**
	 * Dispose of all assets once the application closes.
	 * **/
	public void dispose() {
		font.dispose();
		fruitball_texture.dispose();
		screen_texture.dispose();
		paddle_texture.dispose();
		wait_texture.dispose();
		game_texture.dispose();
		instr_texture.dispose();
		music.dispose();
		chimp_long.dispose();
		chimp_short.dispose();
	}

}
