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

	public Renderer(Dimension d) {
		dim = d;
		calc = new Constants(d);
		game = new GameWorld();
		thread = new Thread(this);
		thread.start();
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		this.setPreferredSize(d);
		inputHandler = new InputHandler(game, calc);
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

		int[][] balls;
		double[] ballTypes;
		int[] player0;
		int[] player1;


		/* get all the info */
		double[][] state = game.getState();

		balls = calc.makeBallXYs(state);
		player0 = calc.makePaddleXY(state, 0);
		player1 = calc.makePaddleXY(state, 1);
		int[] scores = calc.makeScores(state);
		ballTypes = calc.makeBallTypes(state);

		/* render all the balls */
		for (int i = 0; i < balls.length; i++) {
			int type = (int) Math.floor(ballTypes[i]);
			switch (type) {
				case 0:
					g.setColor(Color.BLACK);
					break;
				case 1:
					g.setColor(Color.ORANGE);
					break;
				case 2:
					g.setColor(Color.WHITE);
					break;
				case 3:
					g.setColor(Color.DARK_GRAY);
					break;
				case 4:
					g.setColor(Color.GREEN);
					break;
				case 5:
					g.setColor(Color.MAGENTA);
					break;
				case 6:
					g.setColor(Color.YELLOW);
					break;
				case 7:
					g.setColor(Color.PINK);
					break;
				case 8:
					g.setColor(Color.CYAN);
					break;
				case 9:
					g.setColor(Color.GRAY);
					break;
				case 10:
					g.setColor(Color.LIGHT_GRAY);
					break;
				case 11:
					g.setColor(Color.BLUE);
					break;
				case 12:
					g.setColor(Color.RED);
					break;
				default:
					Color BROWN = new Color(165, 42, 42);
					g.setColor(BROWN);
					break;
			}
			drawBall(g, balls[i][0], balls[i][1]);
		}

		//		for (int[] ball : balls) drawBall(g, ball[0], ball[1]);

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
		int height = (int) (calc.getPaddlePixelDepth() * 0.75);
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
