package multipong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PongMain extends JFrame {

	final static Dimension dim = new Dimension(500,500);
	private JPanel jContentPane = null;
	private Renderer gameRenderer = null;

	public PongMain() {
		super();

		gameRenderer = new Renderer(dim);

		jContentPane = new JPanel();
		jContentPane.setLayout(new BorderLayout());
		jContentPane.add(gameRenderer, BorderLayout.CENTER);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = screenSize.width - dim.width - 10;
		x = x > 0? x/2 : 0;
		int y = screenSize.height - dim.height - 75;
		y = y > 0? y/2 : 0;

		this.setResizable(false);
		this.setBounds(new Rectangle(x, y, dim.width + 7, dim.height + 29)); // Position on the desktop
		this.setMinimumSize(dim);
		this.setMaximumSize(dim);
		this.setContentPane(jContentPane);
		this.setTitle("MultiPong");

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				gameRenderer.keyPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				gameRenderer.keyReleased(e);
			}
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
