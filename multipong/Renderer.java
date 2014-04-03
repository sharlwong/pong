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

	public Renderer(Dimension d) {
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
		setOpaque(false);
		super.paintComponent(g);

		g.setColor(Color.black);

		balls = game.getBallXYs();
		player0 = game.getBottomPaddleXY();
		player1 = game.getTopPaddleXY();

		//		System.out.println(balls.length);
		//		System.out.println(player0[1]);
		//		System.out.println(player1[0]);
		//		System.out.println(player1[1]);
		//		System.out.println(game.calc.getBallPixelRadius());
		//		System.out.println((int)game.calc.getPaddlePixelWidth());

		//		drawRect(g,);

		drawPaddle(g, player0[0], player0[1]);
		drawPaddle(g, player1[0], player1[1]);

		drawBall(g, 500, 500);
		drawBall(g, 0, 500);
		drawBall(g, 500, 0);
		drawBall(g, 0, 0);

		int[] scores = game.getScores();
		g.drawString("Player 0: "+ scores[0], 50,490);
		g.drawString("Player 1: "+ scores[0], 50,10);

//		System.out.println(balls.length);

		for (int[] ball : balls) {
			//			System.out.println(ball[0] + ", "+ ball[1]);
			drawBall(g, ball[0], ball[1]);
		}
		//		System.out.println("\n");
	}

	private void drawBall(Graphics g, int centerX, int centerY) {
		//		int radius = (int) game.calc.getBallPixelRadius();
		int radius = 10;
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
