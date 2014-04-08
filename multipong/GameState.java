package multipong;

/**
 * Created by avery_000 on 08-Apr-14.
 */
public class GameState {
	private double player0fractional;
	private double player1fractional;
	private double[][] balls;

	public GameState(double player0fractional, double player1fractional, double[][] balls) {
		this.player0fractional = player0fractional;
		this.player1fractional = player1fractional;
		this.balls = balls;
	}

	public double getPlayer0fractional() {

		return player0fractional;
	}

	public double getPlayer1fractional() {
		return player1fractional;
	}

	public double[][] getBalls() {
		return balls;
	}
}
