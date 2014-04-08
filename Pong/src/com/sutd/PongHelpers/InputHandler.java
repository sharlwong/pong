package com.sutd.PongHelpers;

import com.badlogic.gdx.InputProcessor;
import com.sutd.GameWorld.GameWorld;

/**
 * Created by avery_000 on 01-Apr-14.
 */
public class InputHandler implements InputProcessor{
	private final GameWorld game;
	private final Constants calc;

	public InputHandler(GameWorld gameBoard, Constants calc) {
		this.game = gameBoard;
		this.calc = calc;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
					/* player 0 moves (alternate) */
			case 8:
				game.setP1fractional(0.000);
				break;
			case 9:
				game.setP1fractional(0.111);
				break;
			case 10:
				game.setP1fractional(0.222);
				break;
			case 11:
				game.setP1fractional(0.333);
				break;
			case 12:
				game.setP1fractional(0.444);
				break;
			case 13:
				game.setP1fractional(0.556);
				break;
			case 14:
				game.setP1fractional(0.667);
				break;
			case 15:
				game.setP1fractional(0.778);
				break;
			case 16:
				game.setP1fractional(0.889);
				break;
			case 7:
				game.setP1fractional(1.000);
				break;

					/* insert ball*/
			case 62:
				game.setInjectBalls();
				break;

					/* quit game */
			case 131:
				game.exit();
				System.exit(0);
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
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
		game.setInjectBalls();
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		game.stopInjectBalls();
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		double relativePos = screenX - calc.getPaddlePixelWidth() / 2;
		//double relativeMax = game.calc.getHorizontalPixelUnitLength() + 2 * game.calc.getBallPixelRadius() - game.calc.getPaddlePixelWidth();
		double relativeMax = calc.getHorizontalPixelUnitLength()+ calc.getPaddlePixelWidth()/2;
		relativePos = relativePos < 0 ? 0 : relativePos;
		relativePos = relativePos > relativeMax ? relativeMax : relativePos;
		game.setP0fractional(relativePos / relativeMax);
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
