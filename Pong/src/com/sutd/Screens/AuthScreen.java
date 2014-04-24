package com.sutd.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.sutd.Pong.PongGame;
import com.sutd.PongHelpers.AssetLoader;

public class AuthScreen implements Screen{
	private Rectangle t2_button;
	private Rectangle t3_button;
	private Rectangle t4_button;
	private Rectangle t5_button;
	private Rectangle t6_button;
	private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batcher;
    private BitmapFont  font;
    private PongGame game;
    private TextureRegion auth_screen;
	
	public AuthScreen(PongGame game){
		this.game = game;
		System.out.println("AuthScreen Attached");
		cam = new OrthographicCamera();
        cam.setToOrtho(true, 136, 204);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        font = AssetLoader.font;
        
        t2_button = new Rectangle(20,54,95,25);
		t3_button = new Rectangle(20,84,95,25);
		t4_button = new Rectangle(20,114,95,25);
		t5_button = new Rectangle(20,144,95,25);
		t6_button = new Rectangle(20,174,95,25);
		
		auth_screen = AssetLoader.auth_screen;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

		batcher.begin();

    	batcher.draw(auth_screen, 0, 0, 136, 204);

        batcher.end();
		
		
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(255, 255, 183, 1);
        shapeRenderer.rect(t2_button.x, t2_button.y, t2_button.width, t2_button.height);
        shapeRenderer.rect(t3_button.x, t3_button.y, t3_button.width, t3_button.height);
        shapeRenderer.rect(t4_button.x, t4_button.y, t4_button.width, t4_button.height);
        shapeRenderer.rect(t5_button.x, t5_button.y, t5_button.width, t5_button.height);
        shapeRenderer.rect(t6_button.x, t6_button.y, t6_button.width, t6_button.height);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL10.GL_BLEND);
        
        batcher.begin();
        font.draw(batcher, "T2", t2_button.x + 35, t2_button.y + 5);
        font.draw(batcher, "T3", t3_button.x + 35, t3_button.y + 5);
        font.draw(batcher, "T4", t4_button.x + 35, t4_button.y + 5);
        font.draw(batcher, "T5", t5_button.x + 35, t5_button.y + 5);
        font.draw(batcher, "T6", t6_button.x + 35, t6_button.y + 5);
        batcher.end();
        
        check_if_touched();
	}
	
	private void check_if_touched() {
		if(Gdx.input.justTouched()) {
			Gdx.app.log("MyTag", "Just Touched!");
			float x = (float) (Gdx.input.getX());
			float y = (float)(Gdx.input.getY());
			if(x>= Gdx.graphics.getWidth()*((float) t2_button.x/136) && x<= Gdx.graphics.getWidth()*((float)(t2_button.x+t2_button.width)/136) && 
					 					y>= Gdx.graphics.getHeight()*((float)t2_button.y/204) && y<=Gdx.graphics.getHeight()*((float)(t2_button.y+t2_button.height)/204)) {
				game.t = "2";
				game.setScreen(new StartScreen(game));
			}
			
			else if(x>= Gdx.graphics.getWidth()*((float) t3_button.x/136) && x<= Gdx.graphics.getWidth()*((float)(t3_button.x+t3_button.width)/136) && 
 					y>= Gdx.graphics.getHeight()*((float)t3_button.y/204) && y<=Gdx.graphics.getHeight()*((float)(t3_button.y+t3_button.height)/204)) {
				game.t = "3";
				game.setScreen(new StartScreen(game));
			//connect to a server here.
			}else if(x>= Gdx.graphics.getWidth()*((float) t4_button.x/136) && x<= Gdx.graphics.getWidth()*((float)(t4_button.x+t4_button.width)/136) && 
 					y>= Gdx.graphics.getHeight()*((float)t4_button.y/204) && y<=Gdx.graphics.getHeight()*((float)(t4_button.y+t4_button.height)/204)) {
				game.t = "4";
				game.setScreen(new StartScreen(game));
			}else if(x>= Gdx.graphics.getWidth()*((float) t5_button.x/136) && x<= Gdx.graphics.getWidth()*((float)(t5_button.x+t5_button.width)/136) && 
 					y>= Gdx.graphics.getHeight()*((float)t5_button.y/204) && y<=Gdx.graphics.getHeight()*((float)(t5_button.y+t5_button.height)/204)) {
				game.t = "5";
				game.setScreen(new StartScreen(game));
			}else if(x>= Gdx.graphics.getWidth()*((float) t6_button.x/136) && x<= Gdx.graphics.getWidth()*((float)(t6_button.x+t6_button.width)/136) && 
 					y>= Gdx.graphics.getHeight()*((float)t6_button.y/204) && y<=Gdx.graphics.getHeight()*((float)(t6_button.y+t6_button.height)/204)) {
				game.t = "6";
				game.setScreen(new StartScreen(game));
			}
		}
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
