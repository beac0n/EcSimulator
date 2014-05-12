package oneDimensionalObjects;

import java.math.BigDecimal;
import java.security.InvalidParameterException;

public class Line {

	private double slope;
	private double yIntercept;
	private boolean infSlope = false;
	private double x;

	public Line(Point a, Point b) {		
		if(a.equals(b)) {
			throw new IllegalArgumentException("the two points are equal");
		}
		
		if (a.getX() == b.getX()) {
			infSlope = true;
			x = a.getX();
		} else {
			slope = (a.getY() - b.getY()) / (a.getX() - b.getX());
			yIntercept = a.getY() - slope * a.getX();
		}
	}
	
	public Line(Point a, double slope) {
		this.slope = slope;
		this.yIntercept = a.getY() - slope * a.getX();		
	}
	
	public Line(double x) {
		infSlope = true;
		this.x = x;
	}

	public double getYvalue(double x) throws InvalidParameterException {
		if (infSlope) {
			throw new InvalidParameterException(
					"slope is infinite");
		}

		return slope * x + yIntercept;
	}
	
	public BigDecimal getYvalue(BigDecimal x) throws InvalidParameterException {
		if (infSlope) {
			throw new InvalidParameterException(
					"slope is infinite");
		}

		return BigDecimal.valueOf(yIntercept).add(BigDecimal.valueOf(slope).multiply(x));
		
		//return slope * x + yIntercept;
	}
	
	public double getXvalue(double y) throws InvalidParameterException {
		if(infSlope) return this.x;
		if(slope == 0) throw new InvalidParameterException("slope is 0");
		return (y - yIntercept)/slope; 
	}
	
	public String toString() {
		if(infSlope) return "x = " + x;
		if(slope == 0) return "y = " + yIntercept;		
		return "y = " + slope + " x + " + yIntercept; 
	}
	
	public double getSlope() {
		return slope;
	}
}
