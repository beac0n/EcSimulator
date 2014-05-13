package oneDimensionalObjects;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class EllipticCurve {

	private double[] a;
	private Point startPoint;

	/**
	 * Holds a representation of an elliptic curve, where the elliptic curve is
	 * y^2 + a_0*xy + a_1*y = x^3 + a_2*x^2 + a_3*x + a_4 the parameters a_0 to
	 * a_4 are in the order a_0, a_1, a_2, a_3, a_4 in the double-array params
	 * 
	 * @param params
	 *            the array which holds the parameters a_0 to a_4 in this order
	 */
	public EllipticCurve(double x, double a0, double a1, double a2, double a3,
			double a4) {
		a = new double[5];

		a[0] = (a0);
		a[1] = (a1);
		a[2] = (a2);
		a[3] = (a3);
		a[4] = (a4);

		this.startPoint = new Point(x, getYvalues(x).getPositiveRoot());
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public EllipticCurveYvalue getYvalues(double x)
			throws IllegalArgumentException {

		double rightOfRoot = -(a[0]*x) - a[1];
		double rightUnderRoot = -4*(-a[2]*x*x-a[3]*x-a[4]-x*x*x);
		double leftUnderRoot = (a[0]*x+a[1])*(a[0]*x+a[1]);
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

	public double getYmax(double xMin, double xMax, double steps) {
		double upperY = this.getYvalues(xMax).getPositiveRoot();

		for (double i = xMax - steps; i > xMin; i -= steps) {
			try {
				double curUpperY = this.getYvalues(i).getPositiveRoot();
				if (curUpperY > upperY)
					upperY = curUpperY;
			} catch (IllegalArgumentException ex) {
			}
		}

		return upperY;
	}

	public double getYmin(double xMin, double xMax, double steps) {
		double lowerY = this.getYvalues(xMax).getNegativeRoot();

		for (double i = xMax - steps; i > xMin; i -= steps) {
			try {
				double curLowerY = this.getYvalues(i).getNegativeRoot();
				if (curLowerY < lowerY)
					lowerY = curLowerY;
			} catch (IllegalArgumentException ex) {
			}
		}

		return lowerY;
	}

	public String toString() {
		String a0 = " + " + a[0] + "*xy";
		String a1 = " + " + a[1] + "*y";
		String a2 = " + " + a[2] + "*x^2";
		String a3 = " + " + a[3] + "*x";
		String a4 = " + " + a[4];

		if (a[0] == 0)
			a0 = "";
		else if (a[0] < 0)
			a0 = replaceNegSign(a0);

		if (a[1] == 0)
			a1 = "";
		else if (a[1] < 0)
			a1 = replaceNegSign(a1);

		if (a[2] == 0)
			a2 = "";
		else if (a[2] < 0)
			a2 = replaceNegSign(a2);

		if (a[3] == 0)
			a3 = "";
		else if (a[3] < 0)
			a3 = replaceNegSign(a3);

		if (a[4] == 0)
			a4 = "";
		else if (a[4] < 0)
			a4 = replaceNegSign(a4);

		return "y^2" + a0 + a1 + "= x^3" + a2 + a3 + a4;
	}

	private String replaceNegSign(String a) {
		return a.replace("-", "").replace("+", "-");
	}

	public double getSlopeAt(Point p) {

		double x = p.getX();
		double y = p.getY();

		double upperTerm = a[3] + 2 * a[2] * x + 3 * x * x - a[0] * y;
		double lowerTerm = a[1] + a[0] * x + 2 * y;

		return upperTerm / lowerTerm;
	}

	public Point getThirdInterceptionPoint(Point p, Point q) {
		Line tempLine;

		if (p.equals(q)) {
			tempLine = new Line(p, getSlopeAt(p));
		} else {
			tempLine = new Line(p, q);
		}

		double v = tempLine.getSlope();

		double x = v * v + a[0] * v + a[2] - p.getX() - q.getX(); // TODO: check if this is correct
		double y = tempLine.getYvalue(x);

		return new Point(x, y);
	}

	public double getZeroX(double startX) {
		
		double x = 0;
		
		try {
			x = startX;
			double y = getYvalues(startX).getPositiveRoot();			

			double newX = x - (y / getSlopeAt(new Point(x, y)));

			while (newX != x) {
				x = newX;
				y = getYvalues(x).getPositiveRoot();
				newX = x - (y / getSlopeAt(new Point(x, y)));
			}

		} catch (IllegalArgumentException ex) {
			return x;
		}

		return x;
	}

	private static final BigDecimal SQRT_DIG = new BigDecimal(150);
	private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG
			.intValue());

	/**
	 * Private utility method used to compute the square root of a BigDecimal.
	 * 
	 * @author Luciano Culacciatti
	 * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-
	 *      BigDecimal
	 */
	private static BigDecimal sqrtNewtonRaphson(BigDecimal c, BigDecimal xn,
			BigDecimal precision) {
		BigDecimal fx = xn.pow(2).add(c.negate());
		BigDecimal fpx = xn.multiply(new BigDecimal(2));
		BigDecimal xn1 = fx.divide(fpx, 2 * SQRT_DIG.intValue(),
				RoundingMode.HALF_DOWN);
		xn1 = xn.add(xn1.negate());
		BigDecimal currentSquare = xn1.pow(2);
		BigDecimal currentPrecision = currentSquare.subtract(c);
		currentPrecision = currentPrecision.abs();
		if (currentPrecision.compareTo(precision) <= -1) {
			return xn1;
		}
		return sqrtNewtonRaphson(c, xn1, precision);
	}

	/**
	 * Uses Newton Raphson to compute the square root of a BigDecimal.
	 * 
	 * @author Luciano Culacciatti
	 * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-
	 *      BigDecimal
	 */
	public static BigDecimal bigSqrt(BigDecimal c) {
		return sqrtNewtonRaphson(c, new BigDecimal(1),
				new BigDecimal(1).divide(SQRT_PRE));
	}

}