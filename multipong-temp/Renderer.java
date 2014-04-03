package multipong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by avery_000 on 01-Apr-14.
 */
public class Renderer extends JPanel implements Runnable {
	GameBoard game;
	int[][] balls;
	int[] player0;
	int[] player1;
	static int unit;



	public Renderer(Dimension d){
		game = new GameBoard(d);
	}

	public void keyDown(KeyEvent e) {
		game.keyDown(e);
	}
	public void keyUp(KeyEvent e) {
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
		System.out.println(player0[0]);
		System.out.println(player0[1]);
		System.out.println(player1[0]);
		System.out.println(player1[1]);


//		drawRect(g,);


		drawRect(g,player0[0], player0[1], (int)game.calc.getPaddlePixelWidth(), (int)game.calc.getPaddlePixelDepth() );
		drawRect(g,player1[0], player1[1], (int)game.calc.getPaddlePixelWidth(), (int)game.calc.getPaddlePixelDepth() );


		for (int[] ball:balls){
//			g.fillOval(ball[0], ball[1], (int) Constants.BALL_RADIUS, (int) Constants.BALL_RADIUS);
		}
	}

	private void drawBall(Graphics g,  int centerX, int centerY, int radius){
		g.fillOval(centerX - radius, centerY - radius, 2*radius, 2*radius);
	}


	private void drawRect(Graphics g,  int centerX, int centerY, int width, int height){
		g.fillRect(centerX - width / 2, centerY - height / 2, width, height);
	}

	@Override
	public void run() {

	}
}
