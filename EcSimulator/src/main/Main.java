package main;

import oneDimensionalObjects.EllipticCurve;
import oneDimensionalObjects.Line;
import oneDimensionalObjects.Point;
import view.Plotter;

public class Main {

	public static void main(String[] args) {

		double startX = 0;
		double a0 = 0;
		double a1 = 0;
		double a2 = 2;
		double a3 = 0;
		double a4 = 4;

		double minX = -3;
		double maxX = 10;
		double steps = 0.0001;
		
		if(args.length >= 9) {
			 startX = Double.parseDouble(args[0]);
			 a0 = Double.parseDouble(args[1]);
			 a1 = Double.parseDouble(args[2]);
			 a2 = Double.parseDouble(args[3]);
			 a3 = Double.parseDouble(args[4]);
			 a4 = Double.parseDouble(args[5]);

			 minX = Double.parseDouble(args[6]);
			 maxX = Double.parseDouble(args[7]);
			 steps = Double.parseDouble(args[8]);
		}


		EllipticCurve curve = new EllipticCurve(startX, a0, a1, a2, a3, a4);
		Plotter plotter = new Plotter(curve, minX, maxX, steps);
	}
}