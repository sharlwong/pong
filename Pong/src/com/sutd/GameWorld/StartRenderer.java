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

public class StartRenderer {
	
    private StartWorld start_world;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    
    private SpriteBatch batcher;
	private TextureRegion ballTexture;
	private TextureRegion paddleTexture;
	private Texture texture;
	private Vector2D screenSize;
	private Paddle paddle0 = new Paddle(0);
	private Paddle paddle1 = new Paddle(1);
	private GameWorld gameworld;
	private Ball[] balls;
	private long totalTime;
    

    public StartRenderer(StartWorld world) {
        start_world = world;
        cam = new OrthographicCamera();
        cam.setToOrtho(true, 136, 204);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        
        screenSize = new Vector2D(136, 200);
        batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		texture = new Texture(Gdx.files.internal("data/texture.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		ballTexture = new TextureRegion(texture, 0, 0, (int) (Assets.BALL_RADIUS*screenSize.x), (int) (Assets.BALL_RADIUS*screenSize.x));
		paddleTexture = new TextureRegion(texture, 0, 0, (int) (Assets.PADDLE_WIDTH*screenSize.x), (int) (Assets.PADDLE_EFFECTIVE_DEPTH*screenSize.x));
		gameworld = new GameWorld(screenSize);
		balls = gameworld.getBallsArray();
		totalTime = 0;
    }

    public void render(float runTime) {
    	
    	
        //System.out.println("StartRenderer - render");
        
        /*
         * 1. We draw a black background. This prevents flickering.
         */

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        /*
         * 2. We draw a start button (filled rectangle).
         */
        

        // Tells shapeRenderer to begin drawing filled shapes
        shapeRenderer.begin(ShapeType.Filled);

        // Chooses RGB Color of 87, 109, 120 at full opacity
        shapeRenderer.setColor(87 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);

        // Draws the rectangle from start_world (Using ShapeType.Filled)
        shapeRenderer.rect(start_world.getStartButton().x, start_world.getStartButton().y,
                start_world.getStartButton().width, start_world.getStartButton().height);

        // Tells the shapeRenderer to finish rendering
        // We MUST do this every time.
        shapeRenderer.end();
        
        /*
         * 3. We draw a join button (filled rectangle).
         */
        

        // Tells shapeRenderer to begin drawing filled shapes
        shapeRenderer.begin(ShapeType.Filled);

        // Chooses RGB Color of 87, 109, 120 at full opacity
        shapeRenderer.setColor(87 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);

        // Draws the rectangle from start_world (Using ShapeType.Filled)
        shapeRenderer.rect(start_world.getJoinButton().x, start_world.getJoinButton().y,
                start_world.getJoinButton().width, start_world.getJoinButton().height);

        // Tells the shapeRenderer to finish rendering
        // We MUST do this every time.
        shapeRenderer.end();
        
        //System.out.println(runTime);
        totalTime += runTime*100;
        batcher.begin();
        drawBalls(totalTime);
    	drawPaddles();
    	batcher.enableBlending();
    	batcher.end();

        /*
         * 4. We draw the rectangle's outline
         */

//        // Tells shapeRenderer to draw an outline of the following shapes
//        shapeRenderer.begin(ShapeType.Line);
//
//        // Chooses RGB Color of 255, 109, 120 at full opacity
//        shapeRenderer.setColor(255 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);
//
//        // Draws the rectangle from start_world (Using ShapeType.Line)
//        shapeRenderer.rect(start_world.getStartButton().x, start_world.getStartButton().y,
//                start_world.getStartButton().width, start_world.getStartButton().height);
//
//        shapeRenderer.end();
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
		//Paddle top = myWorld.player1;
		batcher.draw(paddleTexture, (float) (paddle0.positionForRenderer().x *screenSize.x), 
				(float) (paddle0.positionForRenderer().y*screenSize.y), (float) (Assets.PADDLE_WIDTH*screenSize.x), (float) (Assets.PADDLE_EFFECTIVE_DEPTH*screenSize.y));
		//Paddle down = myWorld.player0;
		batcher.draw(paddleTexture, (float) (paddle1.positionForRenderer().x *screenSize.x), 
				(float) (paddle1.positionForRenderer().y*screenSize.y), (float) (Assets.PADDLE_WIDTH*screenSize.x), (float) (Assets.PADDLE_EFFECTIVE_DEPTH*screenSize.y));
		//System.out.println(paddle1.positionForRenderer().y*screenSize.y);
	}

}
