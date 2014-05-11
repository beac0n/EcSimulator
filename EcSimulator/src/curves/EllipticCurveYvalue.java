package curves;

public class EllipticCurveYvalue {

	private double positiveRoot;
	private double negativeRoot;

	public EllipticCurveYvalue(double positiveRoot, double negativeRoot) {
		this.positiveRoot = positiveRoot;
		this.negativeRoot = negativeRoot;
	}

	public double getPositiveRoot() {
		return positiveRoot;
	}

	public double getNegativeRoot() {
		return negativeRoot;
	}
}