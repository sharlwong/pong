package multipong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PongMain extends JFrame {

	final static Dimension dim = new Dimension(1357, 987);
	private JPanel jContentPane = null;
	private Renderer gameRenderer = null;

	public PongMain() {

		/* magic methods */
		super();
		gameRenderer = new Renderer(dim);
		jContentPane = new JPanel();
		jContentPane.setLayout(new BorderLayout());
		jContentPane.add(gameRenderer, BorderLayout.CENTER);

		/* find center on desktop */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = screenSize.width - dim.width - 10;
		x = x > 0 ? x / 2 : 0;
		int y = screenSize.height - dim.height - 75;
		y = y > 0 ? y / 2 : 0;

		/* more magic */
		this.setResizable(false);
		this.setBounds(new Rectangle(x, y, dim.width + 7, dim.height + 29));
		//		this.setMinimumSize(dim);
		//		this.setMaximumSize(dim);
		this.setPreferredSize(dim);
		this.setContentPane(jContentPane);
		this.setTitle("MultiPong");

		/* listen to keyboard */
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) { gameRenderer.keyPressed(e); }

			@Override
			public void keyReleased(KeyEvent e) { gameRenderer.keyReleased(e); }
		});

		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) { gameRenderer.mouseAt(e); }

			@Override
			public void mouseMoved(MouseEvent e) { gameRenderer.mouseAt(e); }
		});

		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { }

			@Override
			public void mousePressed(MouseEvent e) { gameRenderer.mouseDown(); }

			@Override
			public void mouseReleased(MouseEvent e) { gameRenderer.mouseUp(); }

			@Override
			public void mouseEntered(MouseEvent e) { gameRenderer.mouseAt(e); }

			@Override
			public void mouseExited(MouseEvent e) { gameRenderer.mouseAt(e); }
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				PongMain thisClass = new PongMain();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}
}
