package com.sutd.PongHelpers;

import java.awt.event.KeyEvent;

import com.badlogic.gdx.InputProcessor;
import com.sutd.GameObjects.Paddle;
import com.sutd.GameWorld.GameWorld;

/**
 * Created by avery_000 on 01-Apr-14.
 */
public class InputHandler implements InputProcessor{
	private final GameWorld game;
	boolean disablePlayer0Movement = false;
	Paddle player0;
	Paddle player1;

	public InputHandler(GameWorld gameBoard) {
		this.player0 = gameBoard.getPaddle(0);
		this.player1 = gameBoard.getPaddle(1);
		this.game = gameBoard;
	}

	private boolean checkP0Disabled() {
		return disablePlayer0Movement;
	}

	private void disableWASD() {
		disablePlayer0Movement = true;
		player0.setVelocity(0);
	}

	private void enableWASD() {
		disablePlayer0Movement = false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
					/* player 0 moves (alternate) */
			case 8:
				disableWASD();
				player1.setFractionalPosition(0.000);
				break;
			case 9:
				disableWASD();
				player1.setFractionalPosition(0.111);
				break;
			case 10:
				disableWASD();
				player1.setFractionalPosition(0.222);
				break;
			case 11:
				disableWASD();
				player1.setFractionalPosition(0.333);
				break;
			case 12:
				disableWASD();
				player1.setFractionalPosition(0.444);
				break;
			case 13:
				disableWASD();
				player1.setFractionalPosition(0.556);
				break;
			case 14:
				disableWASD();
				player1.setFractionalPosition(0.667);
				break;
			case 15:
				disableWASD();
				player1.setFractionalPosition(0.778);
				break;
			case 16:
				disableWASD();
				player1.setFractionalPosition(0.889);
				break;
			case 7:
				disableWASD();
				player1.setFractionalPosition(1.000);
				break;

					/* player 1 moves */
			case 21:
				player0.setVelocity(-1);
				break;
			case 22:
				player0.setVelocity(1);
				break;

					/* player 0 moves */
			case 29:
				if (checkP0Disabled()) break;
				player1.setVelocity(-1);
				break;
			case 32:
				if (checkP0Disabled()) break;
				player1.setVelocity(1);
				break;

					/* insert ball*/
			case 62:
				game.setInjectBalls();
				break;

					/* quit game */
			case 131:
				game.exit();
				System.exit(-1);
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
			/* player 0 moves (alternate) */
			case 8:
				enableWASD();
				break;
			case 9:
				enableWASD();
				break;
			case 10:
				enableWASD();
				break;
			case 11:
				enableWASD();
				break;
			case 12:
				enableWASD();
				break;
			case 13:
				enableWASD();
				break;
			case 14:
				enableWASD();
				break;
			case 15:
				enableWASD();
				break;
			case 16:
				enableWASD();
				break;
			case 7:
				enableWASD();
				break;

					/* player 1 moves */
			case 21:
				player0.setVelocity(0);
				break;
			case 22:
				player0.setVelocity(0);
				break;

					/* player 0 moves */
			case 29:
				if (checkP0Disabled()) break;
				player1.setVelocity(0);
				break;
			case 32:
				if (checkP0Disabled()) break;
				player1.setVelocity(0);
				break;

			/* insert ball*/
			case 62:
				game.stopInjectBalls();
				break;
		}
		return true;
	}


	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
