import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ModelTreeTest {
	static final boolean DEBUG = true;
	static final boolean PREDICTIONS = false;
	
	private final static int KFOLDS = 10;
	private final static int FOLD = -1; // run through kfolds manually
	
	static final int MIN_SUBSET_SIZE = 20; // stop if subset size is less than this
	static final double MIN_DEVIATION = 0.2; // stop if deviation is less than this
	
	/*
	 * Shuffles the elements in a list.
	 * @param list list to shuffle
	 */
	public static void randomize(List<Data> list) {
		for (int i = list.size() - 1; i > 0; i--) {
			int rand = (int)( Math.random() * i );
			Data temp = list.get(i);
			list.set(i, list.get(rand));
			list.set(rand, temp);
		}
	}
	
	/*
	 * Splits a list into k distinct subsets in preparation for
	 * k-fold validation.
	 * @param list list to split
	 * @param k number of distinct subsets
	 * @return k distinct subsets
	 */
	public static List<List<Data>> splitList(List<Data> list, int k) {
		List<List<Data>> subsets = new ArrayList<List<Data>>();
		int size = list.size() / k; //5
		for (int i = 0; i < k; i++) {
			int max = (i + 1) * size - 1;
			if (i == k - 1) {
				// our last subset is probably going to bigger
				max = list.size() - 1;
			}
			subsets.add( list.subList(i * size, max) );
		}
		return subsets;
	}
	
	/*
	 * @param array double array to average
	 * @return average of double array
	 */
	public static double average(double[] array) {
		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		return sum / array.length;
	}
	
	/*
	 * Prints an array to the screen
	 * @param array array to be printed
	 */
	public static String formatArray(double[] array) {
		String result = "[";
		for (int i = 0; i < array.length; i++) {
			result += array[i];
			if (i < array.length - 1) {
				result += ", ";
			} else {
				result += "]";
			}
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception {		
		double[] error = new double[10];
		double[] normError = new double[10];
		for (int fold = 0; fold < 10; fold++) {
			String subset = "USA_data";
			Configuration config = Parse.parseConfigFile("config/score_config.txt", "config/USA_config.txt");
			List<Data> trainSet = Parse.parseDataFile("../data-collection/subsets/" + subset + "/" + subset + "-" + fold + "-train.txt", 1, 2, config.getDiscrete(), config.getContinuous());
			List<Data> testSet = Parse.parseDataFile("../data-collection/subsets/" + subset + "/" + subset + "-" + fold + "-test.txt", 1, 2, config.getDiscrete(), config.getContinuous());
			
			ModelTree tree = new ModelTree(config.getDiscrete(), trainSet);
			Evaluate eval = new Evaluate(tree, testSet);
			System.out.println(eval.getRMS() + "\t" + eval.getNormRMS());
			error[fold] = eval.getRMS();
			normError[fold] = eval.getNormRMS();
		}
		
		System.out.println(average(error) + "\t" + average(normError));
		
		
		/*
		// perform a 10-fold cross validation experiment
		// randomize(examples);
		
		List<List<Data>> subsets = splitList(examples, KFOLDS);
		double[] error = new double[KFOLDS];
		double[] normError = new double[KFOLDS];
		for (int test = 0; test < subsets.size(); test++) {
			if (FOLD >= 0) {
				// if we are doing this manually
				test = FOLD;
			}
			// create our training set
			List<Data> train = new ArrayList<Data>();
			for (int i = 0; i < subsets.size(); i++) {
				if (i != test) {
					train.addAll(subsets.get(i));
				}
			}
			
			ModelTree tree = new ModelTree(config.getDiscrete(), train);
			Evaluate eval = new Evaluate(tree, subsets.get(test));
			System.out.println(eval.getRMS() + "\t" + eval.getNormRMS());
			error[test] = eval.getRMS();
			normError[test] = eval.getNormRMS();
			
			if (FOLD >= 0) {
				break;
			}
		}
		*/
	}
}
