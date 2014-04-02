package multipong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by avery_000 on 01-Apr-14.
 */
public class PongMain extends JFrame {

	final static Dimension dim = new Dimension(500, 500);
	private Renderer gameRenderer = null;
	private JPanel jContentPane = null;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				PongMain thisClass = new PongMain();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	public PongMain() {
		super();
		gameRenderer = new Renderer(dim);
		jContentPane = new JPanel();
		jContentPane.setLayout(new BorderLayout());
		jContentPane.add(gameRenderer, BorderLayout.CENTER);
		initialize();

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				gameRenderer.keyDown(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				gameRenderer.keyUp(e);
			}
		});
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setResizable(false);
		this.setBounds(new Rectangle(312, 184, 500, 500)); // Position on the desktop
		this.setMinimumSize(dim);
		this.setMaximumSize(dim);
		this.setContentPane(jContentPane);
		this.setTitle("MultiPong");
	}
}
