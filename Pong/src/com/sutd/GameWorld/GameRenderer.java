package com.sutd.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.sutd.GameObjects.Ball;
import com.sutd.GameObjects.Paddle;
import com.sutd.PongHelpers.Assets;
import com.sutd.PongHelpers.Vector2D;

public class GameRenderer {
    
	private GameWorld game_world;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private Texture texture;

	private SpriteBatch batcher;
	private TextureRegion ballTexture;
	private TextureRegion paddleTexture;
	
	private Vector2D screenSize;
	
	private Paddle paddle0 = new Paddle(0);
	private Paddle paddle1 = new Paddle(1);
	private Ball[] balls;
	private long totalTime;
	private float timeCounter;

	public GameRenderer(GameWorld world) {
		game_world = world;
		screenSize = world.getScreenSize();
		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, 204);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		balls = world.getBallsArray();
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);

		//
		texture = new Texture(Gdx.files.internal("data/texture.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		ballTexture = new TextureRegion(texture, 0, 0, (int) (Assets.BALL_RADIUS*screenSize.x), (int) (Assets.BALL_RADIUS*screenSize.x));
		paddleTexture = new TextureRegion(texture, 0, 0, (int) (Assets.PADDLE_WIDTH*screenSize.x), (int) (Assets.PADDLE_EFFECTIVE_DEPTH*screenSize.x));
	}

    public void render(float runTime) {
    	// This runTime keeps accumulating, can be used by Ball class directly
    	
        System.out.println("GameRenderer - render");
        
        /*
         * 1. We draw a black background. This prevents flickering.
         */

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        /*
         * 2. We draw the Filled rectangle
         */
        

        // Tells shapeRenderer to begin drawing filled shapes
        shapeRenderer.begin(ShapeType.Filled);

        // Chooses RGB Color of 87, 109, 120 at full opacity
        shapeRenderer.setColor(87 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);

        // Draws the rectangle from game_world (Using ShapeType.Filled)
        shapeRenderer.rect(game_world.getRect().x, game_world.getRect().y,
                game_world.getRect().width, game_world.getRect().height);

        // Tells the shapeRenderer to finish rendering
        // We MUST do this every time.
        shapeRenderer.end();

        /*
         * 3. We draw the rectangle's outline
         */

        // Tells shapeRenderer to draw an outline of the following shapes
        shapeRenderer.begin(ShapeType.Line);

        // Chooses RGB Color of 255, 109, 120 at full opacity
        shapeRenderer.setColor(255 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);

        // Draws the rectangle from game_world (Using ShapeType.Line)
        shapeRenderer.rect(game_world.getRect().x, game_world.getRect().y,
                game_world.getRect().width, game_world.getRect().height);
       
//        drawBalls(runTime);
//        drawPaddles();

        shapeRenderer.end();
        

        //System.out.println(runTime);
        timeCounter += runTime;
       // System.out.println(timeCounter);
        
        totalTime += runTime*100;
        if (timeCounter > 1) {
			timeCounter -= 1;
			System.out.println("Refresh");
			System.out.println(noAliveBalls());
			if (existDeadBall()) balls[getNextDeadBallIndex()] = balls[getNextDeadBallIndex()].restart(totalTime);
			System.out.println(getNextDeadBallIndex());
			//System.out.println("Restart");
			if (existDeadBall()) balls[getNextDeadBallIndex()] = balls[getNextDeadBallIndex()].restart(totalTime);
		}
        
        paddle0.update(runTime);
        
        batcher.begin();
        drawBalls(totalTime);
    	drawPaddles();
    	batcher.enableBlending();
    	batcher.end();
    }
    
    public void drawBalls(long runTime) {
		for (Ball b : balls) {
			if (b.isAlive()) {
				drawThisBall(b, runTime);
			}
		}
	}
    
    public void drawThisBall(Ball b, long totalTime) {
		
		Vector2D ballPosition = b.getPosition(totalTime);
		//System.out.println(ballPosition.x+" "+ballPosition.y);
		// require an Vector2D screenSize in this function
		batcher.draw(ballTexture, (float) (ballPosition.x * screenSize.x),
				(float) (ballPosition.y * screenSize.y), (float) (Assets.BALL_RADIUS* screenSize.x/2), (float) (Assets.BALL_RADIUS* screenSize.x/2)); //determine the height of game display region
	}
	
	public void drawPaddles(){
		//Paddle on the top:
		//Paddle top = game_world.player1;
		batcher.draw(paddleTexture, (float) (paddle0.positionForRenderer().x *screenSize.x), 
				(float) (paddle0.positionForRenderer().y*screenSize.y), (float) (Assets.PADDLE_WIDTH*screenSize.x), (float) (Assets.PADDLE_EFFECTIVE_DEPTH*screenSize.y));
		//Paddle down = game_world.player0;
		batcher.draw(paddleTexture, (float) (paddle1.positionForRenderer().x *screenSize.x), 
				(float) (paddle1.positionForRenderer().y*screenSize.y), (float) (Assets.PADDLE_WIDTH*screenSize.x), (float) (Assets.PADDLE_EFFECTIVE_DEPTH*screenSize.y));
		//System.out.println(paddle1.positionForRenderer().y*screenSize.y);
	}
	public boolean existDeadBall() {
		for (Ball b : balls)
			if (!b.isAlive()) return true;
		return false;
	}

	public int getNextDeadBallIndex() {
		for (int i = 0; i<balls.length; i++){
			if (!balls[i].isAlive()){
				return i;
			}
		}
		return -1;
	}
	
	public int noAliveBalls(){
		int counter = 0;
		for(Ball b: balls){
			if (b.isAlive()) counter++;
		}
		return counter;
	}
    
//    public void drawBalls(float runTime) {
//		for (Ball b : balls) {
//			if (b.isAlive()) {
//				drawThisBall(b);
//			}
//		}
//	}
//
//	public void drawThisBall(Ball b) {
//		
//		Vector2D ballPosition = b.getCurrentPosition();
//		// require an Vector2D screenSize in this function
//		batcher.draw(ballTexture, (float) (ballPosition.x * screenSize.x),
//				(float) (ballPosition.y * screenSize.y), (float) Assets.BALL_RADIUS, (float) Assets.BALL_RADIUS); //determine the height of game display region
//	}
//	
//	public void drawPaddles(){
//		//Paddle on the top:
//		Paddle top = game_world.player1;
//		batcher.draw(paddleTexture, (float) top.positionForRenderer().x, (float) top.positionForRenderer().y, (float) Assets.PADDLE_WIDTH, (float) Assets.PADDLE_EFFECTIVE_DEPTH);
//		Paddle down = game_world.player0;
//		batcher.draw(paddleTexture, (float) down.positionForRenderer().x, (float) down.positionForRenderer().y, (float) Assets.PADDLE_WIDTH, (float) Assets.PADDLE_EFFECTIVE_DEPTH);
//	}

}