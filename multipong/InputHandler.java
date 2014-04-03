package multipong;

import java.awt.event.KeyEvent;

/**
 * Created by avery_000 on 01-Apr-14.
 */
public class InputHandler {
	private final GameBoard game;
	boolean disablePlayer0Movement = false;
	Paddle player0;
	Paddle player1;

	public InputHandler(Paddle player0, Paddle player1, GameBoard gameBoard) {
		this.player0 = player0;
		this.player1 = player1;
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

	public void keyDown(KeyEvent e) {
		switch (e.getKeyCode()) {
					/* player 0 moves (alternate) */
			case KeyEvent.VK_1:
				disableWASD();
				player0.setFractionalPosition(0.000);
				break;
			case KeyEvent.VK_2:
				disableWASD();
				player0.setFractionalPosition(0.111);
				break;
			case KeyEvent.VK_3:
				disableWASD();
				player0.setFractionalPosition(0.222);
				break;
			case KeyEvent.VK_4:
				disableWASD();
				player0.setFractionalPosition(0.333);
				break;
			case KeyEvent.VK_5:
				disableWASD();
				player0.setFractionalPosition(0.444);
				break;
			case KeyEvent.VK_6:
				disableWASD();
				player0.setFractionalPosition(0.556);
				break;
			case KeyEvent.VK_7:
				disableWASD();
				player0.setFractionalPosition(0.667);
				break;
			case KeyEvent.VK_8:
				disableWASD();
				player0.setFractionalPosition(0.778);
				break;
			case KeyEvent.VK_9:
				disableWASD();
				player0.setFractionalPosition(0.889);
				break;
			case KeyEvent.VK_0:
				disableWASD();
				player0.setFractionalPosition(1.000);
				break;

					/* player 1 moves */
			case KeyEvent.VK_LEFT:
				player1.setVelocity(-1);
				break;
			case KeyEvent.VK_RIGHT:
				player1.setVelocity(1);
				break;

					/* player 0 moves */
			case KeyEvent.VK_A:
				if (checkP0Disabled()) break;
				player0.setVelocity(-1);
				break;
			case KeyEvent.VK_D:
				if (checkP0Disabled()) break;
				player0.setVelocity(1);
				break;

					/* insert ball*/
			case KeyEvent.VK_SPACE:
				game.injectRandomBall();
				break;

					/* quit game */
			case KeyEvent.VK_ESCAPE:
				game.exit();
				System.exit(-1);
		}

//		System.out.println(player0.getCenter().x);
//		System.out.println(player0.getCenter().y);
//		System.out.println(player1.getCenter().x);
//		System.out.println(player1.getCenter().y);
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
				player1.setVelocity(0);
				break;
			case KeyEvent.VK_RIGHT:
				player1.setVelocity(0);
				break;

					/* player 0 moves */
			case KeyEvent.VK_A:
				if (checkP0Disabled()) break;
				player0.setVelocity(0);
				break;
			case KeyEvent.VK_D:
				if (checkP0Disabled()) break;
				player0.setVelocity(0);
				break;
		}
	}
}
