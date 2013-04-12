import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
	
	/*
	 * @param examples data set of example ratings
	 * @return root-mean-squared deviation
	 */
	public static double rootMeanSquare(List<Float> examples) {
		return rootMeanSquare(examples, false);
	}
	
	/*
	 * Baseline evaluation always predicts average movie rating
	 * @param examples data set of example ratings
	 * @param normalize whether or not to normalize the deviation
	 * @return root-mean-square or normalized root-mean-square deviation
	 */
	public static double rootMeanSquare(List<Float> examples, boolean normalize) {
		double result = 0;
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (Float example : examples) {
			double temp = 6.3689 - example.floatValue();
			temp *= temp;
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
		result = Math.sqrt(result / examples.size());
		if (normalize) {
			result /= max - min;
		}
		return result;
	}
	
	public static void main(String[] args) throws ParseException {
		Scanner scanner = Parse.openFile("../data-collection/noisy_data_revision1.txt");
		List<Float> ratings = new ArrayList<Float>();
		while (scanner.hasNextLine()) {
			String[] example = scanner.nextLine().split("\t");
			ratings.add(Float.parseFloat(example[2]));
		}
		System.out.println(rootMeanSquare(ratings, false));
	}
}
