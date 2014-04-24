package multipong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by avery_000 on 01-Apr-14.
 */
public class Renderer extends JPanel implements Runnable {
	GameWorld    game;
	InputHandler inputHandler;
	Thread       thread;
	Dimension    dim;
	Constants    calc;
	GameState    lastKnownState;

	int[][]  balls;
	int[]    player0;
	int[]    player1;
	int[]    scores;
	int[]    ballTypes;
	double[] ballDoubles;

	public Renderer(Dimension d) {
		dim = d;
		calc = new Constants(d);
		game = new GameWorld();
		thread = new Thread(this);
		inputHandler = new InputHandler(game, calc);
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		this.setPreferredSize(d);

		thread.start();
	}

	public void keyPressed(KeyEvent e) {
		inputHandler.keyDown(e);
	}

	public void keyReleased(KeyEvent e) {
		inputHandler.keyUp(e);
	}

	public void mouseAt(MouseEvent e) {inputHandler.mouseAt(e);}

	public void mouseDown() {inputHandler.mouseDown();}

	public void mouseUp() {inputHandler.mouseUp();}

	@Override
	protected void paintComponent(Graphics g) {
		/* i have no idea what these do */
		setOpaque(false);
		super.paintComponent(g);

		/* get state */
		GameState state = game.getGameState();

		/* check state and store */
		if (state == null && lastKnownState == null) {
			System.out.println("Nothing to render...");
			return;
		}
		if (state == null) {
			state = lastKnownState;
			System.out.println("Missed frame to render...");
		}
		else lastKnownState = state;

		/* make things to render */
		balls = calc.makeBallXYs(state.getBallsData());
		player0 = calc.makePaddleXY(state.getPlayer0Data(), 0);
		player1 = calc.makePaddleXY(state.getPlayer1Data(), 1);
		scores = state.getScores();
		ballTypes = state.getBallsType();

		/* render all the balls */
		for (int i = 0; i < balls.length; i++) {
			int type = (int) Math.floor(ballTypes[i]);
			switch (type) {
				case 0:
					g.setColor(Color.ORANGE);
					break;
				case 1:
					g.setColor(Color.GREEN);
					break;
				case 2:
					g.setColor(Color.RED);
					break;
				default:
					g.setColor(Color.LIGHT_GRAY);
					break;
			}
			drawBall(g, balls[i][0], balls[i][1]);
		}

		/* render player 0 at the bottom */
		g.setColor(Color.BLUE);
		drawPaddle(g, player0[0], player0[1]);
		g.drawString("Player 0: " + scores[0], dim.width / 10, (int) (dim.height * 0.99));

		/* render player 1 at the top */
		g.setColor(Color.RED);
		drawPaddle(g, player1[0], player1[1]);
		g.drawString("Player 1: " + scores[1], dim.width / 10, (int) (dim.height * 0.02));
	}

	private void drawBall(Graphics g, int centerX, int centerY) {
		int radius = (int) calc.getBallPixelRadius();
		g.fillOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
	}

	private void drawPaddle(Graphics g, int centerX, int centerY) {
		int width = (int) calc.getPaddlePixelWidth();
		int height = (int) calc.getPaddlePixelDepth();
		g.fillRect(centerX - width / 2, centerY - height / 2, width, height);
	}

	@Override
	public void run() {
		long jump = Constants.UPDATE_DELTA;
		long time1 = System.currentTimeMillis();
		long time2;

		while (true) {
			try {
				Thread.sleep(jump);
			} catch (InterruptedException e) { }
			time2 = System.currentTimeMillis();
			long delta = time2 - time1;
			delta = delta > 0 ? delta : jump;
			game.updateDeltaTime(delta);
			time1 = time2;
			repaint();
		}
	}
}
