
public class LinearEquation {
	private double[] weights;
	private double[] input;
	private double result;
	
	// linear equation should have the option of just creating an equation with only weights (no input)
	// then it would have the option to solve it at a particular value/vector x
	
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
	
	public LinearEquation(double[] w) {
		weights = w;
		result = Double.NaN;
	}
	
	public void setInput(double[] x) {
		if (weights.length - 1 == x.length) {
			input = x;
			result = solve();
		} else {
			System.err.println("Invalid parameters to LinearEquation");
			System.exit(1);
		}
	}
	
	public double getResult() {
		return result;
	}
	
	private double solve() {
		double result = 0;
		for (int i = 0; i < input.length; i++) {
			result += weights[i + 1] * input[i];
		}
		return result + weights[0];
	}
	
	public static void main(String[] args) {
		
	}
}
