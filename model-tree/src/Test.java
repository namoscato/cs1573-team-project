public class Test {
	public static double standardDeviation(double[] array) {
		// compute average
		double average = 0;
		for (int i = 0; i < array.length; i++) {
			average += array[i];
		}
		average /= array.length;
		
		double result = 0;
		for (int i = 0; i < array.length; i++) {
			double temp = array[i] - average;
			result += temp * temp;
		}
		result /= array.length - 1;
		return Math.sqrt(result);
	}
	
	public static double logisticRegression(double[] weights, double[] input) {
		if (validFunction(weights, input)) {
			return 10.0 / (1 + Math.exp(-1 * solveLinearFunction(weights, input)));
		} else {
			return Double.NaN;
		}
	}
	
	public static double solveLinearFunction(double[] c, double[] x) {
		if (validFunction(c, x)) {
			double result = 0;
			for (int i = 0; i < x.length; i++) {
				result += c[i + 1] * x[i];
			}
			return result + c[0];
		} else {
			return Double.NaN;
		}
	}
	
	public static boolean validFunction(double[] c, double[] x) {
		if (c.length - 1 == x.length) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void test(double[] a) {
		a[0] = -1;
	}
	
	public static void main(String[] args) {
		//double[] array = {1.2, 1.5, 3, 2.4, 2.1, 2, 2.7, 1.9};
		//System.out.println(standardDeviation(array));
		double[] weights = {0, 0};
		double[] input = {120};
		System.out.println(weights[0]);
		test(weights.clone());
		System.out.println(weights[0]);
		//System.out.println(logisticRegression(weights, input));
	}
	
}
