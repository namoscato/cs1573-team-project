import java.util.ArrayList;
import java.util.List;

public class ModelTreeTest {
	private final static int KFOLDS = 10;
	
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
		Configuration config = Parse.parseConfigFile("../config/config.txt", "../config/clean_config.txt");
		List<Data> examples = Parse.parseDataFile("../data-collection/clean_data.txt", 1, 2, config.getDiscrete(), config.getContinuous());
		
		// shuffle our data
		randomize(examples);
		
		// perform a 10-fold cross validation experiment
		List<List<Data>> subsets = splitList(examples, KFOLDS);
		double[] correct = new double[KFOLDS];
		for (int test = 0; test < subsets.size(); test++) {
			// create our training set
			List<Data> train = new ArrayList<Data>();
			for (int i = 0; i < subsets.size(); i++) {
				if (i != test) {
					train.addAll(subsets.get(i));
				}
			}
			
			ModelTree tree = new ModelTree(config.getDiscrete(), train, subsets.get(test));
			tree.printTree();
			System.exit(0);
			//correct[test] = tree.getTestAccuracy();
		}
	}
}
