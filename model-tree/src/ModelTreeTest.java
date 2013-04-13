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
	static final double MIN_DEVIATION = .35; // stop if deviation is less than this
	
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
		Configuration config = Parse.parseConfigFile("../config/config.txt", "config/clean_config.txt");
		List<Data> examples = Parse.parseDataFile("input/clean_data.txt", 1, 2, config.getDiscrete(), config.getContinuous());
		
		// shuffle our data
		// randomize(examples);
		
		File output = new File("output/deviations.txt");
		FileWriter fw = new FileWriter(output.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		// perform a 10-fold cross validation experiment
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
			
			/*
			// write deviations to file
			if (ModelTree.DEBUG) {
				for (Double value : tree.getDebugDeviations()) {
					bw.append(value.toString() + "\n");
				}	
			}
			*/
			
			if (FOLD >= 0) {
				break;
			}
		}
		bw.close();
		
		if (FOLD < 0) {
			System.out.println(average(error) + "\t" + average(normError));
		}
	}
}
