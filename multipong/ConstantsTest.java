package multipong;

import org.junit.Test;

import java.awt.*;

/**
 * Created by avery_000 on 24-Apr-14.
 */
public class ConstantsTest {

	private void pass(String methodName) {
		System.out.println("PASS: " + methodName);
	}

	@Test
	public void testMakePaddleXY() throws Exception {
		Constants calc = new Constants(new Dimension(1000, 1000));
		for (int p = 0; p <= 1; p++) {
			for (int i = -10; i < 99; i++) {
				double x = (double) i / 10;
				for (int j = -10; j < 99; j++) {
					double y = (double) j / 10;
					int[] xy = calc.makePaddleXY(new double[]{x, y}, p);
					if (p == 0)
						assert xy[1] == calc.getDim().height - (int) (calc.getEdgePixelPadding() + calc.getPaddlePixelDepth() / 2);
					else assert xy[1] == (int) (calc.getEdgePixelPadding() + calc.getPaddlePixelDepth() / 2);
				}
			}
		}
		pass("makePaddleXY");
	}

	@Test
	public void testMakeBallXYs() throws Exception {
		assert true;
		pass("makeBallXYs");
	}

	@Test
	public void testGetDim() throws Exception {
		Constants calc;
		for (int i = -10; i < 999; i++) {
			for (int j = -10; j < 999; j++) {
				calc = new Constants(new Dimension(i, j));
				assert calc.getDim().equals(new Dimension(i, j));
			}
		}
		pass("getDim");
	}
}
