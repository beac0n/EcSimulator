package curves;

public class EllipticCurve {

	private double[] a;

	/**
	 * Holds a representation of an elliptic curve, where the elliptic curve is
	 * y^2 + a_1*xy + a_2*y = x^3 + a_3*x^2 + a_4*x + a_5 the parameters a_1 to
	 * a_5 are in the order a_1, a_2, a_3, a_4, a_5 in the double-array params
	 * 
	 * @param params
	 *            the array which holds the parameters a_1 to a_5 in this order
	 */
	public EllipticCurve(double... params) {

		a = new double[5];

		for (int i = 0; i < params.length; ++i) {
			a[i] = params[i];
		}
	}

	public EllipticCurveYvalue getYvalue(double x)
			throws IllegalArgumentException {

		double rightOfRoot = a[0] * (-x) - a[1];
		double rightUnderRoot = Math.pow((a[0] * (-x) - a[1]), 2);
		double leftUnderRoot = 4 * (a[2] * x * x + a[3] * x + a[4] + x * x * x);
		double underRoot = leftUnderRoot + rightUnderRoot;

		if (underRoot < 0) {
			throw new IllegalArgumentException(
					"there was a negative value under the root");
		}

		double root = Math.sqrt(underRoot);

		double yNeg = 0.5 * (-root + rightOfRoot);
		double yPos = 0.5 * (root + rightOfRoot);

		return new EllipticCurveYvalue(yPos, yNeg);
	}
}