import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Significance {
	private double averageDifference;
	private double averageVariance;
	private double lowerBound;
	private double upperBound;
	private final double[] tValue = {12.71, 4.303, 3.182, 2.776, 2.571, 2.447, 2.365, 2.306, 2.262, 2.228,
									 2.201, 2.179, 2.160, 2.145, 2.131, 2.120, 2.110, 2.101, 2.093, 2.086};
	
	private final boolean BASELINE = true;
	
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
		double[] a = new double[1];
		double[] b = new double[1];
		Scanner scan;
		
		try {
			// bring in first set of results
			scan = new Scanner(new File("results1.txt"));
			for (int i = 0; i < 1; i++) {
				a[i] = Double.parseDouble(scan.nextLine().split("\\s")[1]);
			}
			
			// bring in second set of results
			scan = new Scanner(new File("results2.txt"));
			for (int i = 0; i < 1; i++) {
				b[i] = Double.parseDouble(scan.nextLine().split("\\s")[1]);
			}
			
			// print out the significance
			System.out.println(new Significance(a, b));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
