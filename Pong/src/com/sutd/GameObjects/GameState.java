package com.sutd.GameObjects;

public class GameState {
	private double[][] ballsData;
	private double[]   player0Data;
	private double[]   player1Data;
	private int[]      ballsType;
	private int        status;
	private int        timeLeft;
	private int[]      scores;

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

	public int[] getBallsType() {
		return ballsType;
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

	public void setBallsType(int[] ballsType) {
		this.ballsType = ballsType;
	}

	/**
	 * Returns the image of GameState
	 * IMPORTANT: Not everything flips. Make sure to duplicate!
	 * Invariant: gamestate.flip.flip == gamestate
	 *
	 * @return
	 */
	public GameState flip() {
		GameState flipped = new GameState();
		flipped.setStatus(status);
		flipped.setTimeLeft(timeLeft);
		flipped.setBallsData(ballsData);
		flipped.setBallsType(ballsType);

		//flip score
		flipped.setScores(new int[]{scores[1], scores[0]});
		//flip paddles
		flipped.setPlayer1Data(new double[]{1 - player0Data[0], 1});
		flipped.setPlayer0Data(new double[]{1 - player1Data[0], 0});
		//flip balls
		double temp[][] = new double[ballsData.length][2];
		for (int i = 0; i < ballsData.length; i++) {
			temp[i][0] = 1 - ballsData[i][0];
			temp[i][1] = 1 - ballsData[i][1];
		}
		flipped.setBallsData(temp);
		return flipped;
	}
}
