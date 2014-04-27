package multipong;

public class Vector2D {
	/* various constants */
	public static final Vector2D X    = new Vector2D(1, 0);
	public static final Vector2D Y    = new Vector2D(0, 1);
	public static final Vector2D ZERO = new Vector2D(0, 0);

	/* object vars are doubles for max precision */
	public double x;
	public double y;

	/**
	 * C O N S T R U C T O R S
	 */

	public Vector2D() {
	}

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2D(Vector2D v) {
		set(v);
	}

	public Vector2D cpy() {
		return new Vector2D(this);
	}

	/**
	 * S I M P L E  M A T H
	 */

	public Vector2D add(Vector2D v) {
		this.x += v.x;
		this.y += v.y;
		return this;
	}

	public Vector2D add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public Vector2D div(double value) {
		return multiply(1.0 / value);
	}

	public Vector2D div(double vx, double vy) {
		return multiply(1 / vx, 1 / vy);
	}

	public Vector2D div(Vector2D other) {
		return multiply(1 / other.x, 1 / other.y);
	}

	public Vector2D multiply(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
		return this;
	}

	public Vector2D multiply(double x, double y) {
		this.x *= x;
		this.y *= y;
		return this;
	}

	public Vector2D multiply(Vector2D v) {
		this.x *= v.x;
		this.y *= v.y;
		return this;
	}

	public Vector2D subtract(Vector2D v) {
		this.x -= v.x;
		this.y -= v.y;
		return this;
	}

	public Vector2D subtract(double x, double y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	/**
	 * I N F O
	 */

	/* from x-axis clockwise, in degrees */
	public double angle() {
		double angle = Math.atan2(this.y, this.x) * (180 / Math.PI);
		if (angle < 0) angle += 360;
		return angle;
	}

	/* from x-axis clockwise, in radians */
	public double radians() {
		double angle = Math.atan2(this.y, this.x);
		if (angle < 0) angle += 2 * Math.PI;
		return angle;
	}

	/* roughly equals */
	public boolean epsilonEquals(Vector2D obj, double epsilon) {
		if (obj == null) return false;
		if (Math.abs(obj.x - this.x) > epsilon) return false;
		if (Math.abs(obj.y - this.y) > epsilon) return false;
		return true;
	}

	/* roughly equals */
	public boolean epsilonEquals(double x, double y, double epsilon) {
		if (Math.abs(x - this.x) > epsilon) return false;
		if (Math.abs(y - this.y) > epsilon) return false;
		return true;
	}

	/* checks equality then non-equivalence then congruence */
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Vector2D other = (Vector2D) obj;
		if (Double.compare(this.x, other.x) != 0) return false;
		if (Double.compare(this.y, other.y) != 0) return false;
		return true;
	}

	/* random-ish hash */
	public int hashCode() {
		long prime = 7919;
		long result = 1;
		result = prime * result + Double.doubleToLongBits(this.x);
		result = prime * result + Double.doubleToLongBits(this.y);
		return (int) result;
	}

	/* distance from origin at [0:0] */
	public double length() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}

	public double lengthSquared() {
		return this.x * this.x + this.y * this.y;
	}

	/* how far from here to there */
	public double distance(double x, double y) {
		double x_d = x - this.x;
		double y_d = y - this.y;
		return Math.sqrt(x_d * x_d + y_d * y_d);
	}

	/* how far from here to there */
	public double distance(Vector2D v) {
		double x_d = v.x - this.x;
		double y_d = v.y - this.y;
		return Math.sqrt(x_d * x_d + y_d * y_d);
	}

	public double distanceSquared(Vector2D v) {
		double x_d = v.x - this.x;
		double y_d = v.y - this.y;
		return x_d * x_d + y_d * y_d;
	}

	public double distanceSquared(double x, double y) {
		double x_d = x - this.x;
		double y_d = y - this.y;
		return x_d * x_d + y_d * y_d;
	}

	/* gimme a string */
	public String toString() {
		return "[" + this.x + ":" + this.y + "]";
	}

	/**
	 * T R A N S F O R M S
	 */

	/* enforce bounds on a vector */
	public Vector2D clamp(double min, double max) {
		double l2 = lengthSquared();
		if (l2 == 0) return this;
		if (l2 > max * max) return makeUnitVector().multiply(max);
		if (l2 < min * min) return makeUnitVector().multiply(min);
		return this;
	}

	/* enforce length limit */
	public Vector2D limit(double limit) {
		if (lengthSquared() > limit * limit) {
			makeUnitVector();
			multiply(limit);
		}
		return this;
	}

	/* make this length == 1 */
	public Vector2D makeUnitVector() {
		double len = length();
		if (len != 0) {
			this.x /= len;
			this.y /= len;
		}
		return this;
	}

	/* rotate clockwise */
	public Vector2D rotate(double degrees) {
		/* make angles */
		double rad = degrees * (Math.PI / 180);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);
		/* apply rotation matrix */
		double newX = this.x * cos - this.y * sin;
		double newY = this.x * sin + this.y * cos;
		/* set */
		this.x = newX;
		this.y = newY;
		return this;
	}

	/* make this == that */
	public Vector2D set(Vector2D v) {
		this.x = v.x;
		this.y = v.y;
		return this;
	}

	/* make this == that */
	public Vector2D set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/* make this angle == that angle */
	public Vector2D setAngle(double degrees) {
		set(length(), 0);
		rotate(degrees);

		return this;
	}

	/**
	 * V E C T O R  F U N C T I O N S
	 */

	public double crossProduct(Vector2D v) {
		return this.x * v.y - this.y * v.x;
	}

	public double crossProduct(double x, double y) {
		return this.x * y - this.y * x;
	}

	public double dotProduct(Vector2D v) {
		return this.x * v.x + this.y * v.y;
	}

	public double dotProduct(double x, double y) {
		return this.x * x + this.y * y;
	}

	public Vector2D linearInterpolate(Vector2D target, double alpha) {
		double invAlpha = 1 - alpha;
		this.x = (this.x * invAlpha + target.x * alpha);
		this.y = (this.y * invAlpha + target.y * alpha);
		return this;
	}
}