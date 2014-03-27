package com.sutd.PongHelpers;

import com.badlogic.gdx.InputProcessor;
import com.sutd.GameObjects.Paddle;
import com.sutd.GameWorld.GameWorld;

/**
 * Created by avery_000 on 3/23/14.
 */
public class InputHandler implements InputProcessor{
	private Paddle myPaddle;
	private GameWorld myWorld;

	public InputHandler(GameWorld myWorld) {
	     // myBird now represents the gameWorld's bird.
	    this.myWorld = myWorld;
	    // user side: getPaddle(0) returns the paddle of current player (not opponent's paddle)
	    myPaddle = myWorld.getPaddle(0);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
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
//		if (myWorld.isReady()) {
//            myWorld.start();
//        }

		// parse x coordinate to Paddle
        myPaddle.onClick(screenX);

//        if (myWorld.isGameOver()) {
//            // Reset all variables, go to GameState.READ
//            myWorld.restart();
//        }

        return true;
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
