package view;

import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

import oneDimensionalObjects.EllipticCurve;
import oneDimensionalObjects.Line;
import oneDimensionalObjects.Point;

import org.math.plot.Plot2DPanel;

public class EllipticCurveKeyListener implements KeyEventDispatcher {

	private Plotter plot;
	private EllipticCurve curve;
	private Point currPoint;
	private double oldXmax;

	public EllipticCurveKeyListener(Plotter plot, EllipticCurve curve) {
		this.oldXmax = plot.getXmax();
		this.plot = plot;
		this.curve = curve;
		this.currPoint = curve.getStartPoint();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_RELEASED
				&& e.getKeyCode() == KeyEvent.VK_ENTER) {

			Point thirdPointNmir = curve.getThirdInterceptionPoint(curve.getStartPoint(), currPoint);
			Point thirdPoint = curve.getMirroredPoint(thirdPointNmir);

			Line tempLine;
			if (curve.getStartPoint().equals(currPoint)) {
				tempLine = new Line(currPoint, curve.getSlopeAt(currPoint));
			} else {
				tempLine = new Line(curve.getStartPoint(), currPoint);
			}

			Line verticalLine = new Line(thirdPoint.getX());

			if (thirdPoint.getX() < oldXmax && currPoint.getX() < oldXmax && plot.getXmax() != oldXmax) {
				plot.replot(oldXmax); // fall back to old bounds
			} else if(thirdPoint.getX() > plot.getXmax() || currPoint.getX() > plot.getXmax()) {
				// one of the two new points is outside the bounds
				double xMax = thirdPoint.getX();
				if (currPoint.getX() > xMax) {
					xMax = currPoint.getX();
				}
				
				plot.replot(xMax + 10);				
			}

			plot.plotLine(Color.RED, tempLine);
			plot.plotLine(Color.GREEN, verticalLine);

			plot.addPoint("P", Color.BLACK, curve.getStartPoint());
			plot.addPoint("Q", Color.BLUE, currPoint);
			plot.addPoint("P + Q", Color.RED, thirdPoint);

			currPoint = thirdPoint;
		}

		return false;
	}
}
