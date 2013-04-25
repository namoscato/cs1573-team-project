import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Evaluate {
	private double rms;
	private double normRMS;
	
	public Evaluate(ModelTree tree, List<Data> test) {
		rms = 0;
		normRMS = 0;
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (Data example : test) {
			double temp = tree.testExample(example, ModelTreeTest.PREDICTIONS); // testExample returns square of difference
			rms += temp;
			if (temp < min) {
				min = temp;
			}
			if (temp > max) {
				max = temp;
			}
		}
		rms = Math.sqrt(rms / test.size());
		normRMS = rms / (max - min);
		/*
		if (ModelTreeTest.PREDICTIONS) {
			System.out.println(rms + "\t" + normRMS);
			System.exit(0);
		}
		*/
	}
	
	public double getRMS() {
		return rms;
	}
	
	public double getNormRMS() {
		return normRMS;
	}
	
	/*
	 * @param array double array to average
	 * @return average of double array
	 */
	public static double average(List<Float> list) {
		double sum = 0;
		for (int i = 0; i < list.size(); i++) {
			sum += list.get(i);
		}
		return sum / list.size();
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
		double avg = average(examples);
		for (Float example : examples) {
			double temp = avg - example.floatValue();
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
		Scanner scanner = Parse.openFile("../data-collection/datasets/usa/USA_data.txt");
		List<Float> ratings = new ArrayList<Float>();
		while (scanner.hasNextLine()) {
			String[] example = scanner.nextLine().split("\t");
			ratings.add(Float.parseFloat(example[2]));
		}
		System.out.println(rootMeanSquare(ratings, false));
		System.out.println(rootMeanSquare(ratings, true));
	}
}
