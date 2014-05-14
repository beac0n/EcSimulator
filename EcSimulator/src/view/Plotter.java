package view;

import static java.awt.Toolkit.getDefaultToolkit;

import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedList;
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
	private List<double[][]> pointSets;

	private JFrame frame;
	private Plot2DPanel plot;
	private double xMin;
	private double xMax;
	private double steps;

	private int plotCounter = 0;

	public Plotter(EllipticCurve curve, double xMin, double xMax, double steps) {
		this.curve = curve;
		this.xMin = xMin;
		this.xMax = xMax;
		this.steps = steps;

		initPlot();

		plotEllipticCurve();
	}

	public double getXmax() {
		return xMax;
	}

	private double[][] listsToArray(ArrayList<Double> curXvals,
			ArrayList<Double> curYvals) {
		if (curXvals.size() > 0) {
			double[][] currPointSet = new double[curXvals.size()][2];

			for (int j = 0; j < curXvals.size(); ++j) {
				currPointSet[j][0] = curXvals.get(j);
				currPointSet[j][1] = curYvals.get(j);
			}

			return currPointSet;
		}

		return null;
	}

	public ArrayList<Double> arrayToXvals(double[][] array) {
		ArrayList<Double> retVal = new ArrayList<Double>();

		for (int i = 0; i < array.length; ++i) {
			retVal.add(array[i][0]);
		}

		return retVal;
	}

	public ArrayList<Double> arrayToYvals(double[][] array) {
		ArrayList<Double> retVal = new ArrayList<Double>();

		for (int i = 0; i < array.length; ++i) {
			retVal.add(array[i][1]);
		}

		return retVal;
	}

	public void replot(double xMax) {
		this.xMax = xMax;

		plot.removeAllPlots();
		plotEllipticCurve();
	}

	private void plotEllipticCurve() {

		pointSets = new LinkedList<double[][]>();
		double zeroX;

		ArrayList<Double> curXvals = new ArrayList<Double>();
		ArrayList<Double> curYvals = new ArrayList<Double>();

		for (double i = xMin; i < xMax; i += steps) {
			try {
				EllipticCurveYvalue tempVal = curve.getYvalues(i);

				curXvals.add(i);
				curYvals.add(tempVal.getPositiveRoot());

			} catch (IllegalArgumentException ex) {
				if (curXvals.size() > 0) {

					double lastGoodX = getLastGoodXtoTheLeft(curXvals.get(0));
					EllipticCurveYvalue lastGoodYvalues = curve
							.getYvalues(lastGoodX);

					curXvals.add(0, lastGoodX);
					curYvals.add(0, lastGoodYvalues.getPositiveRoot());

					lastGoodX = getLastGoodXtoTheRight(curXvals.get(curXvals
							.size() - 1));
					lastGoodYvalues = curve.getYvalues(lastGoodX);

					curXvals.add(lastGoodX);
					curYvals.add(lastGoodYvalues.getPositiveRoot());

					pointSets.add(listsToArray(curXvals, curYvals));

					curXvals = new ArrayList<Double>();
					curYvals = new ArrayList<Double>();
				}
			}
		}

		double lastGoodX = getLastGoodXtoTheLeft(curXvals.get(0));
		EllipticCurveYvalue lastGoodYvalues = curve.getYvalues(lastGoodX);

		curXvals.add(0, lastGoodX);
		curYvals.add(0, lastGoodYvalues.getPositiveRoot());

		pointSets.add(listsToArray(curXvals, curYvals));

		curXvals = new ArrayList<Double>();
		curYvals = new ArrayList<Double>();

		for (double i = xMax; i > xMin; i -= steps) {
			try {
				EllipticCurveYvalue tempVal = curve.getYvalues(i);

				curXvals.add(i);
				curYvals.add(tempVal.getNegativeRoot());

			} catch (IllegalArgumentException ex) {
				if (curXvals.size() > 0) {

					if (curXvals.get(0) < xMax) {
						lastGoodX = getLastGoodXtoTheRight(curXvals.get(0));
						lastGoodYvalues = curve.getYvalues(lastGoodX);

						curXvals.add(0, lastGoodX);
						curYvals.add(0, lastGoodYvalues.getPositiveRoot());
					}

					lastGoodX = getLastGoodXtoTheLeft(i);
					lastGoodYvalues = curve.getYvalues(lastGoodX);

					curXvals.add(lastGoodX);
					curYvals.add(lastGoodYvalues.getNegativeRoot());

					pointSets.add(listsToArray(curXvals, curYvals));

					curXvals = new ArrayList<Double>();
					curYvals = new ArrayList<Double>();
				}
			}
		}

		for (int i = 0; i < pointSets.size(); ++i) {
			plot.addLinePlot(curve.toString(), Color.GRAY, pointSets.get(i));
		}
	}

	private double getLastGoodXtoTheLeft(double lastGoodX) {
		// go forth baby steps and if it crashes => take last good value
		double j = lastGoodX + steps;
		double littleSteps = 0.00000001;
		EllipticCurveYvalue littleTempVal;
		while (true) {
			j -= littleSteps;
			try {
				littleTempVal = curve.getYvalues(j);
			} catch (IllegalArgumentException littleEx) {
				break;
			}
		}
		j += littleSteps;
		return j;
	}

	private double getLastGoodXtoTheRight(double lastGoodX) {
		// go forth baby steps and if it crashes => take last good value
		double j = lastGoodX - steps;
		double littleSteps = 0.00000001;
		EllipticCurveYvalue littleTempVal;
		while (true) {
			j += littleSteps;
			try {
				littleTempVal = curve.getYvalues(j);
			} catch (IllegalArgumentException littleEx) {
				break;
			}
		}
		j -= littleSteps;
		return j;
	}

	private void initPlot() {
		plot = new Plot2DPanel();
		plot.addLegend("SOUTH");

		frame = new JFrame("Elliptic Curve");

		frame.setFocusable(true);
		frame.requestFocusInWindow();

		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new EllipticCurveKeyListener(this, curve));

		frame.add(plot);
		frame.setSize(getDefaultToolkit().getScreenSize());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void plotLine(Color color, Line line) {

		if (plotCounter > pointSets.size()) {
			while (plot.getPlots().size() > pointSets.size()) {
				plot.removePlot(pointSets.size());
			}
			plotCounter = 0;
		}

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

		xArr.add(xArr.get(xArr.size() - 1));
		yArr.add(yArr.get(yArr.size() - 1));

		double[] xValues = new double[xArr.size()];
		double[] yValues = new double[yArr.size()];

		for (int i = 0; i < xArr.size(); ++i) {
			xValues[i] = xArr.get(i);
			yValues[i] = yArr.get(i);
		}

		plot.addLinePlot(line.toString(), color, xValues, yValues);
		plotCounter++;
	}

	public void addPoint(String name, Color color, Point p) {
		double[] x = { p.getX() };
		double[] y = { p.getY() };

		plot.addScatterPlot(name + " (" + x[0] + "," + y[0] + ")", color, x, y);
		plotCounter++;
	}
}
