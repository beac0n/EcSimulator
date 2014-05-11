package main;
 
 import java.awt.BorderLayout;
 import java.awt.Color;
 import java.awt.Dimension;
 import java.awt.GridLayout;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.KeyEvent;
 import java.awt.event.KeyListener;
 import java.util.ArrayList;
 
 import javax.swing.*;
 
 import org.math.plot.*;
 
 import curves.EllipticCurve;
 import curves.EllipticCurveYvalue;
 
 public class Main {
 
 	public static void main(String[] args) {
 
 		EllipticCurve curve = new EllipticCurve(0d, 0d, 0d, -4d, 4);
 
 		// define your data
 		ArrayList<Double> xArr = new ArrayList<Double>();
 		ArrayList<Double> yArr = new ArrayList<Double>();
 
 		for (double i = 3; i > -3; i -= 0.001) {
 			try {
 				EllipticCurveYvalue tempVal = curve.getYvalue(i);
 
 				xArr.add(i);
 				yArr.add(tempVal.getNegativeRoot());
 
 			} catch (IllegalArgumentException ex) {
 			}
 		}
 
 		for (double i = -3; i < 3; i += 0.001) {
 			try {
 				EllipticCurveYvalue tempVal = curve.getYvalue(i);
 
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
 
 		// create your PlotPanel (you can use it as a JPanel)
 		Plot2DPanel plot = new Plot2DPanel();
 		// define the legend position
 		plot.addLegend("SOUTH");
 		// add a line plot to the PlotPanel
 		plot.addLinePlot("my plot", x, y);
 
 		plot.setFocusable(true);
 		plot.requestFocusInWindow();
 		
 		plot.addKeyListener(new KeyListener() {
 			public void keyPressed(KeyEvent e) {
 				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
 					System.out.println("keyPressed");
 				}
 			}
 			public void keyReleased(KeyEvent e) {
 				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
 					System.out.println("keyReleased");
 				}
 
 			}
 			public void keyTyped(KeyEvent e) {	}
 		});
 		
 		
 		// put the PlotPanel in a JFrame like a JPanel
 		JFrame frame = new JFrame("Elliptic Curve");
 
 		frame.add(plot);
 		frame.setSize(600, 600);
 		frame.setVisible(true);
 
 	}
 }