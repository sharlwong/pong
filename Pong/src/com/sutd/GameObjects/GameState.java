package com.sutd.GameObjects;

/**
 * GameState is a Go-Between Class that contains information on scores and fruit count for each player, 
 * status of game play and time left, as well as paddle and ball positions from both local client and server.
 * 
 * It is the glue between the server and the client, but unlike network class primarily concerns itself 
 * with Game Layer Processing. It is an extremely important class!
 */

public class GameState {
	private double[][]	ballsData;
	private double[]   	player0Data;
	private double[]   	player1Data;
	private int[]     	ballsType;
	private int        	status;
	private int        	timeLeft;
	private int[]      	scores;
	private int 		orangeP0;
	private int 		kiwiP0;
	private int 		watermelonP0;
	private int 		orangeP1;
	private int 		kiwiP1;
	private int 		watermelonP1;

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
	
	public int[] getOrange(){
		return new int[] {orangeP0, orangeP1};
	}
	
	public int[] getKiwi(){
		return new int[] {kiwiP0, kiwiP1};
	}
	
	public int[] getWatermelon(){
		return new int[] {watermelonP0, watermelonP1};
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
	
	public void setOrange(int orange1, int orange2){
		this.orangeP0 = orange1;
		this.orangeP1 = orange2;
	}
	
	public void setKiwi(int kiwi1, int kiwi2){
		this.kiwiP0 = kiwi1;
		this.kiwiP1 = kiwi2;
	}
	
	public void setWatermelon(int watermelon1, int watermelon2){
		this.watermelonP0 = watermelon1;
		this.watermelonP1 = watermelon2;
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
		flipped.setOrange(orangeP1, orangeP0);
		flipped.setKiwi(kiwiP1, kiwiP0);
		flipped.setWatermelon(watermelonP1, watermelonP0);
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
