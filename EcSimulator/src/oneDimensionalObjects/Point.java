package oneDimensionalObjects;

public class Point {

	private double x;
	private double y;
	private boolean infiniteP;
	
	
	public Point() {
		infiniteP = true;		
	}
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		if(infiniteP) throw new IllegalArgumentException();
		return x;
	}

	public double getY() {
		if(infiniteP) throw new IllegalArgumentException();
		return y;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Point))
			return false;
		Point o = (Point) obj;
		if (o.getX() == this.getX() && o.getY() == this.getY())
			return true;
		else return false;
	}
}
