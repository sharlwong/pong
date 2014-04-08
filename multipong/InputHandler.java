package multipong;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by avery_000 on 01-Apr-14.
 */
public class InputHandler {
	private final GameWorld game;
	boolean disablePlayer0Movement = false;
	Paddle player0;
	Paddle player1;
	Dimension dimension;

	public InputHandler(Paddle player0, Paddle player1, GameWorld gameWorld) {
		this.player0 = player0;
		this.player1 = player1;
		this.game = gameWorld;
		this.dimension = gameWorld.calc.getDim();
	}

	private boolean checkP0Disabled() {
		return disablePlayer0Movement;
	}

	private void disableWASD() {
		disablePlayer0Movement = true;
	}

	private void enableWASD() {
		disablePlayer0Movement = false;
	}

	public void keyDown(KeyEvent e) {
		switch (e.getKeyCode()) {
					/* player 0 moves (alternate) */
			case KeyEvent.VK_1:
				disableWASD();
				player1.setFractionalPosition(0.000);
				break;
			case KeyEvent.VK_2:
				disableWASD();
				player1.setFractionalPosition(0.111);
				break;
			case KeyEvent.VK_3:
				disableWASD();
				player1.setFractionalPosition(0.222);
				break;
			case KeyEvent.VK_4:
				disableWASD();
				player1.setFractionalPosition(0.333);
				break;
			case KeyEvent.VK_5:
				disableWASD();
				player1.setFractionalPosition(0.444);
				break;
			case KeyEvent.VK_6:
				disableWASD();
				player1.setFractionalPosition(0.556);
				break;
			case KeyEvent.VK_7:
				disableWASD();
				player1.setFractionalPosition(0.667);
				break;
			case KeyEvent.VK_8:
				disableWASD();
				player1.setFractionalPosition(0.778);
				break;
			case KeyEvent.VK_9:
				disableWASD();
				player1.setFractionalPosition(0.889);
				break;
			case KeyEvent.VK_0:
				disableWASD();
				player1.setFractionalPosition(1.000);
				break;

					/* player 1 moves */
			case KeyEvent.VK_LEFT:
				player0.startMoveLeft();
				break;
			case KeyEvent.VK_RIGHT:
				player0.startMoveRight();
				break;

					/* player 0 moves */
			case KeyEvent.VK_A:
				if (checkP0Disabled()) break;
				player1.startMoveLeft();
				break;
			case KeyEvent.VK_D:
				if (checkP0Disabled()) break;
				player1.startMoveRight();
				break;

					/* insert ball*/
			case KeyEvent.VK_SPACE:
				game.setInjectBalls();
				break;

					/* quit game */
			case KeyEvent.VK_ESCAPE:
				game.exit();
				System.exit(0);
		}
	}

	public void keyUp(KeyEvent e) {
		switch (e.getKeyCode()) {
			/* player 0 moves (alternate) */
			case KeyEvent.VK_1:
				enableWASD();
				break;
			case KeyEvent.VK_2:
				enableWASD();
				break;
			case KeyEvent.VK_3:
				enableWASD();
				break;
			case KeyEvent.VK_4:
				enableWASD();
				break;
			case KeyEvent.VK_5:
				enableWASD();
				break;
			case KeyEvent.VK_6:
				enableWASD();
				break;
			case KeyEvent.VK_7:
				enableWASD();
				break;
			case KeyEvent.VK_8:
				enableWASD();
				break;
			case KeyEvent.VK_9:
				enableWASD();
				break;
			case KeyEvent.VK_0:
				enableWASD();
				break;

					/* player 1 moves */
			case KeyEvent.VK_LEFT:
				player0.stopMoveLeft();
				break;
			case KeyEvent.VK_RIGHT:
				player0.stopMoveRight();
				break;

					/* player 0 moves */
			case KeyEvent.VK_A:
				if (checkP0Disabled()) break;
				player1.stopMoveLeft();
				break;
			case KeyEvent.VK_D:
				if (checkP0Disabled()) break;
				player1.stopMoveRight();
				break;

			/* insert ball*/
			case KeyEvent.VK_SPACE:
				game.stopInjectBalls();
				break;
		}
	}

	public void mouseDown() {
		game.setInjectBalls();
	}

	public void mouseUp() {
		game.stopInjectBalls();
	}

	public void mouseAt(MouseEvent e) {
		double relativePos = e.getX() - game.calc.getPaddlePixelWidth() / 2;
		double relativeMax = game.calc.getHorizontalPixelUnitLength() + 2 * game.calc.getBallPixelRadius() - game.calc.getPaddlePixelWidth();
		relativePos = relativePos < 0 ? 0 : relativePos;
		relativePos = relativePos > relativeMax ? relativeMax : relativePos;
		player0.setFractionalPosition(relativePos / relativeMax);
	}
}
