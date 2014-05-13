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
			
			System.out.println("now plotting...");

			Point tempPoint = curve.getThirdInterceptionPoint(curve.getStartPoint(), currPoint);
			tempPoint = new Point(tempPoint.getX(), -tempPoint.getY());

			Line tempLine;
			if (curve.getStartPoint().equals(currPoint)) {
				tempLine = new Line(currPoint, curve.getSlopeAt(currPoint));
			} else {
				tempLine = new Line(curve.getStartPoint(), currPoint);
			}

			Line verticalLine = new Line(tempPoint.getX());

			if (tempPoint.getX() <= oldXmax && currPoint.getX() <= oldXmax) {
				plot.replot(oldXmax);
			} else {
				double xMax = tempPoint.getX();
				if (currPoint.getX() > xMax) {
					xMax = currPoint.getX();
				}

				plot.replot(xMax+10);
			}

			plot.plotLine(Color.RED, tempLine);
			plot.plotLine(Color.GREEN, verticalLine);

			plot.addPoint("P", Color.BLACK, curve.getStartPoint());
			plot.addPoint("Q", Color.BLUE, currPoint);
			plot.addPoint("P + Q", Color.RED, tempPoint);

			currPoint = tempPoint;
			System.out.println("plotting finished");
		}

		return false;
	}
}
