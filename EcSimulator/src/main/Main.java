package main;

import oneDimensionalObjects.EllipticCurve;
import oneDimensionalObjects.Line;
import oneDimensionalObjects.Point;
import view.Plotter;

public class Main {

	public static void main(String[] args) {

		EllipticCurve curve = new EllipticCurve(1, 0d, 0d, 0d, -4d, 4);
		Plotter plotter = new Plotter(curve, -3, 9, 0.001);

		double xa = -2;
		
		Point a = new Point(xa, curve.getYvalues(xa).getPositiveRoot());
		
		plotter.plotLine(new Line(a, curve.getSlopeAt(a)));
		
		Point c = curve.getThirdInterceptionPoint(a, a);
		
		plotter.plotLine(new Line(c, new Point(c.getX(), -c.getY())));		
	}
}