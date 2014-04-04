package multipong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by avery_000 on 01-Apr-14.
 */
public class Renderer extends JPanel implements Runnable {
	static int unit;
	GameBoard game;
	int[][] balls;
	int[] player0;
	int[] player1;
	Thread thread;
	Dimension dim;

	public Renderer(Dimension d) {
		dim = d;
		game = new GameBoard(d);
		thread = new Thread(this);
		thread.start();
	}

	public void keyPressed(KeyEvent e) {
		game.keyDown(e);
	}

	public void keyReleased(KeyEvent e) {
		game.keyUp(e);
	}

	@Override
	protected void paintComponent(Graphics g) {
		/* i have no idea what these do */
		setOpaque(false);
		super.paintComponent(g);

		/* get all the info */
		balls = game.getBallXYs();
		player0 = game.getBottomPaddleXY();
		player1 = game.getTopPaddleXY();
		int[] scores = game.getScores();

		/* render player 0 at the bottom */
		g.setColor(Color.BLUE);
		drawPaddle(g, player0[0], player0[1]);
		g.drawString("Player 0: "+ scores[0], dim.width/10, (int) (dim.height*0.99));

		/* render lpayer 1 at the top */
		g.setColor(Color.RED);
		drawPaddle(g, player1[0], player1[1]);
		g.drawString("Player 1: "+ scores[1], dim.width/10, (int) (dim.height*0.02));

		/* render all the balls */
		g.setColor(Color.BLACK);
		for (int[] ball : balls) drawBall(g, ball[0], ball[1]);
	}

	private void drawBall(Graphics g, int centerX, int centerY) {
		int radius = (int) game.calc.getBallPixelRadius();
		g.fillOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
	}

	private void drawPaddle(Graphics g, int centerX, int centerY) {
		int width = (int) game.calc.getPaddlePixelWidth();
		int height = (int) game.calc.getPaddlePixelDepth();
		g.fillRect(centerX - width / 2, centerY - height / 2, width, height);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			game.updateDeltaTime(10);
			//			System.out.println(game.elapsedTimeMillis);
			repaint();
		}
	}
}
