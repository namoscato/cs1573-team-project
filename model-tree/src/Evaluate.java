import java.util.List;

public class Evaluate {
	
	/*
	 * @param tree ModelTree to evaluate
	 * @param test data set of test examples
	 * @return root-mean-square deviation
	 */
	public static double rootMeanSquare(ModelTree tree, List<Data> test) {
		return rootMeanSquare(tree, test, false);
	}
	
	/*
	 * @param tree ModelTree to evaluate
	 * @param test data set of test examples
	 * @param normalize whether or not to normalize the deviation
	 * @return root-mean-square or normalized root-mean-square deviation
	 */
	public static double rootMeanSquare(ModelTree tree, List<Data> test, boolean normalize) {
		double result = 0;
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (Data example : test) {
			double temp = tree.testExample(example); // testExample returns square of difference
			result += temp;
			if (normalize) {
				if (temp < min) {
					min = temp;
				}
				if (temp > max) {
					max = temp;
				}	
			}
		}
		result = Math.sqrt(result / test.size());
		if (normalize) {
			result /= max - min;
		}
		return result;
	}
}
