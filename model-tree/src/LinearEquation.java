
public class LinearEquation {
	private double[] weights;
	private double[] input;
	private double result;
	
	public LinearEquation(double[] w) {
		weights = w;
		result = Double.NaN;
	}
	
	public LinearEquation(double[] w, double[] x) {
		if (w.length - 1 == x.length) {
			weights = w;
			input = x;
			result = solve();
		} else {
			System.err.println("Invalid parameters to LinearEquation");
			System.exit(1);
		}
	}
	
	private double solve() {
		double result = 0;
		for (int i = 0; i < input.length; i++) {
			result += weights[i + 1] * input[i];
		}
		result += weights[0];
		return result;
	}
	
	public double solve(double[] x) {
		if (weights.length - 1 == x.length) {
			input = x;
			result = solve();
			return result;
		} else {
			System.err.println("Invalid parameters to LinearEquation");
			System.exit(1);
			return Double.NaN;
		}
	}
	
	public double getResult() {
		return result;
	}
}
