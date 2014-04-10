package com.sutd.GameObjects;

/**
 * Created by avery_000 on 08-Apr-14.
 */
public class GameState {
	private double[][] ballsData;
	private double[] player0Data;
	private double[] player1Data;
	private int status;
	private int timeLeft;
	private int[] scores;

	public double[][] getBallsData() {
		return ballsData;
	}

	public double[] getPlayer0Data() {
		return player0Data;
	}

	public double[] getPlayer1Data() {
		return player1Data;
	}

	public int getStatus() {
		return status;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public int[] getScores() {
		return scores;
	}

	public void setBallsData(double[][] ballsData) {
		this.ballsData = ballsData;
	}

	public void setPlayer0Data(double[] player0Data) {
		this.player0Data = player0Data;
	}

	public void setPlayer1Data(double[] player1Data) {
		this.player1Data = player1Data;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	public void setScores(int[] scores) {
		this.scores = scores;
	}
	
	/**
	 * Returns the image of GameState
	 * IMPORTANT: Not everything flips. Make sure to duplicate!
	 * Invariant: gamestate.flip.flip == gamestate
	 * @return
	 */
	public GameState flip() {
		GameState flipped = new GameState();
		flipped.setStatus(status);
		flipped.setTimeLeft(timeLeft);
		flipped.setBallsData(ballsData);

		//flip score
		flipped.setScores(new int[] {scores[1], scores[0]});
		//flip paddles
		flipped.setPlayer1Data(new double[] {1 - player0Data[0], 1});
		flipped.setPlayer0Data(new double[] {1 - player1Data[0], 0});
		//flip balls
		// TODO: Flip balls.
		return flipped;
	}
}
