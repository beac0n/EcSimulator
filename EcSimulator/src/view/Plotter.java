package view;

import static java.awt.Toolkit.getDefaultToolkit;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import oneDimensionalObjects.EllipticCurve;
import oneDimensionalObjects.EllipticCurveYvalue;
import oneDimensionalObjects.Line;
import oneDimensionalObjects.Point;

import org.math.plot.Plot2DPanel;

public class Plotter {

	private EllipticCurve curve;
	private JFrame frame;
	private Plot2DPanel plot;
	private double xMin;
	private double xMax;
	private double steps;

	public Plotter(EllipticCurve curve, double xMin, double xMax,
			double steps) {
		this.curve = curve;
		this.xMin = xMin;
		this.xMax = xMax;
		this.steps = steps;

		List<Point> points = generateCurvePoints(curve);

		double[] xValues = new double[points.size()];
		double[] yValues = new double[points.size()];

		for (int i = 0; i < points.size(); ++i) {
			xValues[i] = points.get(i).getX();
			yValues[i] = points.get(i).getY();
		}

		plot = new Plot2DPanel();
		plot.addLegend("SOUTH");
		plot.addLinePlot(curve.toString(), xValues, yValues);

		plot.setFocusable(true);
		plot.requestFocusInWindow();

		plot.addKeyListener(new EllipticCurveKeyListener(plot, curve));  

		frame = new JFrame("Elliptic Curve");

		frame.add(plot);
		frame.setSize(getDefaultToolkit().getScreenSize());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public List<Point> generateCurvePoints(EllipticCurve curve) {
		// define your data
		ArrayList<Double> xArr = new ArrayList<Double>();
		ArrayList<Double> yArr = new ArrayList<Double>();

		for (double i = xMax; i > xMin; i -= steps) {
			try {
				EllipticCurveYvalue tempVal = curve.getYvalues(i);

				xArr.add(i);
				yArr.add(tempVal.getNegativeRoot());

			} catch (IllegalArgumentException ex) {
			}
		}

		for (double i = xMin; i < xMax; i += steps) {
			try {
				EllipticCurveYvalue tempVal = curve.getYvalues(i);

				xArr.add(i);
				yArr.add(tempVal.getPositiveRoot());

			} catch (IllegalArgumentException ex) {
			}
		}

		double[] x = new double[xArr.size()];
		double[] y = new double[yArr.size()];

		for (int i = 0; i < xArr.size(); ++i) {
			x[i] = xArr.get(i);
			y[i] = yArr.get(i);
		}

		List<Point> pointList = new ArrayList<Point>();

		for (int i = 0; i < x.length; ++i) {
			pointList.add(new Point(x[i], y[i]));
		}
		return pointList;
	}

	public void plotLine(Line line) {
		ArrayList<Double> xArr = new ArrayList<Double>();
		ArrayList<Double> yArr = new ArrayList<Double>();

		for (double i = xMin; i < xMax; i += steps) {
			double xTemp = 0;
			double yTemp = 0;

			try {
				xTemp = i;
				yTemp = line.getYvalue(i);

			} catch (InvalidParameterException slopeInfEx) {
				try {
					xTemp = line.getXvalue(0);
					double yMax = curve.getYmax(xMin, xMax, steps);
					double yMin = curve.getYmin(xMin, xMax, steps);

					xArr.add(xTemp);
					xArr.add(xTemp);

					yArr.add(yMin);
					yArr.add(yMax);
					break;
				} catch (InvalidParameterException slopeZeroEx) {
					yTemp = line.getYvalue(0);

					xArr.add(xMin);
					xArr.add(xMax);

					yArr.add(yTemp);
					yArr.add(yTemp);
				}
			}

			xArr.add(xTemp);
			yArr.add(yTemp);
		}

		xArr.add(xArr.get(xArr.size()-1));
		yArr.add(yArr.get(yArr.size()-1));
		
		double[] xValues = new double[xArr.size()];
		double[] yValues = new double[yArr.size()];

		for (int i = 0; i < xArr.size(); ++i) {
			xValues[i] = xArr.get(i);
			yValues[i] = yArr.get(i);
		}
		
		plot.addLinePlot(line.toString(), xValues, yValues);
	}
}
