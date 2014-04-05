package com.sutd.GameWorld;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.sutd.GameObjects.Ball;
import com.sutd.GameObjects.Paddle;
import com.sutd.PongHelpers.Constants;
import com.sutd.PongHelpers.InputHandler;
import com.sutd.PongHelpers.Vector2D;

public class GameWorld {

	private Paddle player0;
	private Paddle player1;
	private List<Ball> balls;
	private Rectangle rect = new Rectangle(0, 0, 17, 12);
	int simulatedLag = 123;
	private long timeCount = 0;
	
	// all elements in the array of Ball need to be initialized when GameWorld is created
	// how to initialize balls?
	/* WILL BE INITED BY SERVER */
	// for odd index, balls come from player0 side
	// for even index, balls come from player1 side
	/* OKAY CAN */
	// note that only set the first five elements' isMoving to be true

	public Constants calc;
	private int heightPixels;
	private int widthPixels;
	public long elapsedTimeMillis;
	
	private SecureRandom random;
	
	private InputHandler inputHandler;
	private Dimension dim;
	
	private boolean injectBalls = false;
	public Boolean ready = new Boolean(false);
	
	public GameWorld(Dimension sizePixels){
		this.calc = new Constants(sizePixels);
		this.heightPixels = sizePixels.height;
		this.widthPixels = sizePixels.width;
		this.elapsedTimeMillis = 0;
		this.player0 = new Paddle(0);
		this.player1 = new Paddle(1);	
		this.balls = new ArrayList<Ball>();
		random = new SecureRandom();
		random.setSeed(1234567890);
		
		inputHandler = new InputHandler(this);
		dim = sizePixels;
		
		injectRandomBall();
	}
	
	
	public void exit() {
		System.out.println("GAME OVER");
		System.out.println("Player 0: " + player0.getScore());
		System.out.println("Player 1: " + player1.getScore());
		System.out.println("Done!");
		System.exit(0);
	}

	public int[][] getBallXYs() {
		synchronized (balls) {
			int[][] out = new int[balls.size()][2];
			for (int i = 0; i < balls.size(); i++) {
				Dimension temp = calc.translateBallReferenceFrame(balls.get(i).getCurrentPosition());
				out[i][0] = temp.width;
				out[i][1] = temp.height;
			}
			return out;
		}
	}

	public int[] getBottomPaddleXY() {
		int[] out = new int[2];
		Dimension temp = calc.translateBallReferenceFrame(player0.getCenter());
		out[0] = temp.width;
		out[1] = temp.height + (int) calc.getBallPixelRadius();
		return out;
	}

	public int[] getTopPaddleXY() {
		int[] out = new int[2];
		Dimension temp = calc.translateBallReferenceFrame(player1.getCenter());
		out[0] = temp.width;
		out[1] = temp.height - (int) calc.getBallPixelRadius();
		return out;
	}

	public void setInjectBalls() {
		injectBalls = true;
		injectRandomBall();
	}

	public void stopInjectBalls() {
		injectBalls = false;
	}

	public void injectRandomBall() {
		Vector2D position = new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2);
		double speed1 = random.nextDouble() - 0.5;
		double speed2 = random.nextDouble() - 0.5;
		Vector2D speed;
		if (Math.abs(speed1) < Math.abs(speed2)) speed = new Vector2D(speed1, speed2);
		else speed = new Vector2D(speed2, speed1);
		synchronized (balls) {
			balls.add(new Ball(position, speed, elapsedTimeMillis, simulatedLag < 0 ? (int) (random.nextDouble() * 500) : simulatedLag));
		}
	}

	public int[] getScores() {
		int[] out = new int[2];
		out[0] = player0.getScore();
		out[1] = player1.getScore();
		return out;
	}
	
	
	
	public Vector2D startPositionGen(int index){
		if (index%2==0) return new Vector2D(Math.random(), 0);
		else return new Vector2D(Math.random(), 1);
	}
	public Vector2D startVelocityGen(int index){
		if (index%2==0) return new Vector2D(Math.random()-0.5, Math.random());
		else return new Vector2D(Math.random()-0.5, -1);
	}

	

	/**
	 * prerequisite: input integer must be either 1 or 0
	 *
	 * @param p
	 * @return
	 */
	public Paddle getPaddle(int p) {
		return (p == 0) ? player0 : player1;
	}

	public Rectangle getRect() {
		return rect;
	}

	/**
	 * initially there will be a certain number of balls (for example, 5 balls)
	 * every second there will be two more balls (each one comes from each side)
	 * when the total number of alive balls reached 20 (for example), there will be no more balls added in
	 * when there are balls stopped, reset those balls
	 *
	 * @param delta
	 */
	public synchronized void update(float delta) {
		if(!ready.booleanValue()) return;
			
		long deltaMillis = (long) (delta * 1000);
		timeCount += deltaMillis;
		
		//if (injectBalls) injectRandomBall();
		
		if (timeCount/1000 == 1){
			timeCount -= 1000;
			injectRandomBall();
			injectRandomBall();
			injectRandomBall();
			injectRandomBall();
			injectRandomBall();
			injectRandomBall();
		}
		
		

		elapsedTimeMillis += deltaMillis;
		player0.updateDeltaTime(deltaMillis);
		player1.updateDeltaTime(deltaMillis);
		List<Ball> removeThese = new ArrayList<Ball>();
		List<Ball> addThese = new ArrayList<Ball>();
		synchronized (balls) {
			for (Ball b : balls) {
				b.updateCurrentTime(elapsedTimeMillis);
				if (player1.collisionCheck(b)) {
					addThese.add(player1.bounce(b, elapsedTimeMillis));
					//				b.kill();
					removeThese.add(b);
				}
				if (player0.collisionCheck(b)) {
					addThese.add(player0.bounce(b, elapsedTimeMillis));
					//				b.kill();
					removeThese.add(b);
				}
				if (!b.inGame()) {
					//				b.kill();
					removeThese.add(b);
					if (b.getCurrentPosition().y < 0) player1.incrementScore();
					if (b.getCurrentPosition().y > Constants.HEIGHT) player0.incrementScore();
				}
			}

			for (Ball b : removeThese) balls.remove(b);
			for (Ball b : addThese) balls.add(b);
		}
	}
	
	public void keyDown(int keycode) {
		inputHandler.keyDown(keycode);
	}

	public void keyUp(int keycode) {
		inputHandler.keyUp(keycode);
	}
	
	public Dimension getDim(){
		return dim;
	}
	
}
