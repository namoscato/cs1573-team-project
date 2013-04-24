
public class Significance {
	private double averageDifference;
	private double averageVariance;
	private double lowerBound;
	private double upperBound;
	private final double[] tValue = {12.71, 4.303, 3.182, 2.776, 2.571, 2.447, 2.365, 2.306, 2.262, 2.228,
									 2.201, 2.179, 2.160, 2.145, 2.131, 2.120, 2.110, 2.101, 2.093, 2.086};
	/*
	 * Computes the statistical significance between two lists A and B where
	 * the ith element of A is from the same test set as the ith element of B.
	 */
	public Significance (double[] a, double[] b) {
		int length = a.length; // assume a and b have the same length
		averageDifference = 0;
		averageVariance = 0;
		for (int i = 0; i < length; i++) {
			double temp = a[i] - b[i];
			averageDifference += temp;
			averageVariance += temp * temp;
		}
		averageDifference /= length;
		averageVariance /= length - 1;
		
		double temp = Math.sqrt(averageVariance) / Math.sqrt(length) * tValue[length - 2];
		lowerBound = averageDifference - temp;
		upperBound = averageDifference + temp;
	}
	
	/*
	 * @return confidence interval
	 */
	public String toString() {
		return String.format("[" + lowerBound + ", " + upperBound + "]");
	}
	
	public static void main(String[] args) {
		double[] a = {.85, .83, .86, .92, .9};
		double[] b = {.83, .82, .9, .89, .91};
		
		Significance s = new Significance(a, b);
		System.out.println(s);
	}
}
