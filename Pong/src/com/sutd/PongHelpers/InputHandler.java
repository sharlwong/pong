package com.sutd.PongHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.sutd.GameObjects.Paddle;
import com.sutd.GameWorld.GameWorld;

/**
 * Created by avery_000 on 01-Apr-14.
 */
public class InputHandler implements InputProcessor{
	private final Paddle player_paddle;
	private final Constants calc;
	private double previousPosition;

	public InputHandler(Paddle paddle, Constants calc) {
		this.calc = calc;
		this.player_paddle = paddle;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
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
		double relativePos = screenX;
		if (Math.abs(previousPosition - relativePos) > Gdx.graphics.getWidth() * 0.6){
			return true;
		}else{
			//double relativeMax = game.calc.getHorizontalPixelUnitLength() + 2 * game.calc.getBallPixelRadius() - game.calc.getPaddlePixelWidth();
			double relativeMax = Gdx.graphics.getWidth();
			relativePos = relativePos < 0 ? 0 : relativePos;
			relativePos = relativePos > relativeMax ? relativeMax : relativePos;
			this.player_paddle.setFractionalPosition(relativePos / relativeMax);
			previousPosition = screenX;
			return true;
		}
		
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		double relativePos = screenX - calc.getPaddlePixelWidth() / 2;
		//double relativeMax = game.calc.getHorizontalPixelUnitLength() + 2 * game.calc.getBallPixelRadius() - game.calc.getPaddlePixelWidth();
		double relativeMax = calc.getHorizontalPixelUnitLength()+ calc.getPaddlePixelWidth()/2;
		relativePos = relativePos < 0 ? 0 : relativePos;
		relativePos = relativePos > relativeMax ? relativeMax : relativePos;
		this.player_paddle.setFractionalPosition(relativePos / relativeMax);
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
