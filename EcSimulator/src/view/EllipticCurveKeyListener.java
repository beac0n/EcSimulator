package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import oneDimensionalObjects.EllipticCurve;
import oneDimensionalObjects.Line;
import oneDimensionalObjects.Point;

import org.math.plot.Plot2DPanel;

public class EllipticCurveKeyListener implements KeyListener {

	private Plot2DPanel plot;
	private EllipticCurve curve;
	private Point currPoint;
	
	public EllipticCurveKeyListener(Plot2DPanel plot, EllipticCurve curve) {
		this.plot = plot;
		this.curve = curve;
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			Line tempLine = new Line(currPoint, curve.getSlopeAt(currPoint));
			
			
		}

	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

}
