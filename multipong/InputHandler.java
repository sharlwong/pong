package multipong;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by avery_000 on 01-Apr-14.
 */
public class InputHandler {
	private final Constants calc;
	private final GameWorld game;

	public InputHandler(GameWorld gameWorld, Constants calc) {
		this.game = gameWorld;
		this.calc = calc;
	}

	public void keyDown(KeyEvent e) {
		switch (e.getKeyCode()) {
					/* player 0 moves (alternate) */
			case KeyEvent.VK_1:
				game.setP1fractional(0.000);
				break;
			case KeyEvent.VK_2:
				game.setP1fractional(0.111);
				break;
			case KeyEvent.VK_3:
				game.setP1fractional(0.222);
				break;
			case KeyEvent.VK_4:
				game.setP1fractional(0.333);
				break;
			case KeyEvent.VK_5:
				game.setP1fractional(0.444);
				break;
			case KeyEvent.VK_6:
				game.setP1fractional(0.556);
				break;
			case KeyEvent.VK_7:
				game.setP1fractional(0.667);
				break;
			case KeyEvent.VK_8:
				game.setP1fractional(0.778);
				break;
			case KeyEvent.VK_9:
				game.setP1fractional(0.889);
				break;
			case KeyEvent.VK_0:
				game.setP1fractional(1.000);
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
			/* insert ball*/
			case KeyEvent.VK_SPACE:
				game.stopInjectBalls();
				break;
		}
	}

	public void mouseAt(MouseEvent e) {
		double relativePos = e.getX() - calc.getPaddlePixelWidth() / 2;
		double relativeMax = calc.getHorizontalPixelUnitLength() + 2 * calc.getBallPixelRadius() - calc.getPaddlePixelWidth();
		relativePos = relativePos < 0 ? 0 : relativePos;
		relativePos = relativePos > relativeMax ? relativeMax : relativePos;
		game.setP0fractional(relativePos / relativeMax);
	}

	public void mouseDown() {
		game.setInjectBalls();
	}

	public void mouseUp() {
		game.stopInjectBalls();
	}
}
