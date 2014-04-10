package com.sutd.GameWorld;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.sutd.GameObjects.Ball;
import com.sutd.GameObjects.GameState;
import com.sutd.GameObjects.Paddle;
import com.sutd.PongHelpers.Constants;
import com.sutd.PongHelpers.Vector2D;

public class GameWorld {

	private Paddle player0;
	private Paddle player1;
	private List<Ball> balls;
	int simulatedLag = 123;
	
	// all elements in the array of Ball need to be initialized when GameWorld is created
	// how to initialize balls?
	/* WILL BE INITED BY SERVER */
	// for odd index, balls come from player0 side
	// for even index, balls come from player1 side
	/* OKAY CAN */
	// note that only set the first five elements' isMoving to be true

	public Constants calc;
	public long elapsedTimeMillis;
	
	private SecureRandom random;
	
	private long injectBalls = 0;
	private boolean init = true;

	public Boolean ready = new Boolean(false);
	
	public GameWorld(){
		this.elapsedTimeMillis = 0;
		this.player0 = new Paddle(0);
		this.player1 = new Paddle(1);	
		this.balls = new ArrayList<Ball>();
		random = new SecureRandom();
		random.setSeed(1234567890);
	}
	
	
	public void exit() {
		System.out.println("GAME OVER");
		System.out.println("Player 0: " + player0.getScore());
		System.out.println("Player 1: " + player1.getScore());
		System.out.println("Done!");
		System.exit(0);
	}

	public GameState getGameState() {
		GameState out = new GameState();
		Vector2D temp;
		//Set Balls:
		double[][] ballsData = new double[balls.size()][2];
		for (int i = 0; i < balls.size(); i++){
			temp = balls.get(i).getCurrentPosition();
			ballsData[i][0] = temp.x;
			ballsData[i][1] = temp.y;
		}
		out.setBallsData(ballsData);
		//Set Status:
		int status = ready?1:0;
		out.setStatus(1);
		//Set Player Data:
		temp = player0.getCenter();
		out.setPlayer0Data(new double[] {temp.x, temp.y});
		temp = player1.getCenter();
		out.setPlayer1Data(new double[] {temp.x, temp.y});
		//Set Scores
		out.setScores(new int[] {player0.getScore(), player1.getScore()});
		return out;
	}

	public double[][] getState(){
		Vector2D temp;
		int num;
		double[][] out;
		num = balls.size();
		out = new double[num+3][2];
		for (int i = 0; i < num; i++){
			temp = balls.get(i).getCurrentPosition();
			out[i][0] = temp.x;
			out[i][1] = temp.y;
		}
		temp = player0.getCenter();
		out[num] = new double[] {temp.x, temp.y};
		temp = player1.getCenter();
		out[num + 1] = new double[] {temp.x, temp.y};
		out[num+2]= new double[] {player0.getScore(), player1.getScore()};
		return out;
	}

	public void setInjectBalls() {
		injectBalls = elapsedTimeMillis + 100;
		injectRandomBall();
	}

	public void stopInjectBalls() {
		injectBalls = 0;
	}

	private void injectRandomBall() {
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

	/**
	 * prerequisite: input integer must be either 1 or 0
	 *
	 * @param p
	 * @return
	 */
	public Paddle getPaddle(int p) {
		return (p == 0) ? player0 : player1;
	}

	public void update(float delta) {
		if(!ready.booleanValue()) return;
			
		long deltaMillis = (long) (delta);
		
		//if (injectBalls) injectRandomBall();
		
		if (elapsedTimeMillis > Constants.START_GAME_DELAY && init) {
			init = false;
			injectRandomBall();
		}
		if (injectBalls > 0 && injectBalls < elapsedTimeMillis) injectRandomBall();

		
		elapsedTimeMillis += deltaMillis;

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
			if(balls.size() == 0) injectRandomBall();
		}
	}
	
	public void setP0fractional(double pos) {
		player0.setFractionalPosition(pos);
	}
	
	public void setP1fractional(double pos) {
		player1.setFractionalPosition(pos);
	}
}
