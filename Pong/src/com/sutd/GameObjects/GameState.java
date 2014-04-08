package com.sutd.GameObjects;

/**
 * Created by avery_000 on 08-Apr-14.
 */
public class GameState {
	private double[][] state;

	public GameState(double[][] gameState) {
		this.state = gameState;
	}

	public double[] getPlayer0Position() {

		return state[state.length-3];
	}

	public double[] getPlayer1Position() {

		return state[state.length-2];
	}

	public double[][] getBalls() {
		double[][] out = new double[state.length - 3][2];
		for (int i = 0; i < state.length - 3; i++) out[i] = state[i];
		return out;
	}

	public int[] getScores(){

		double[] temp = state[state.length-1];
		return new int[] {(int) temp[0], (int) temp[1]};
	}
	
	public double[][] getState(){
		return this.state;
	}
}
