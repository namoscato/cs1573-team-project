import java.util.ArrayList;
import java.util.List;

public class ModelTreeTest {
	static final boolean DEBUG = false;
	static final boolean PREDICTIONS = false;
	
	private final static int KFOLDS = 10;
	private final static int FOLD = -1; // run through kfolds manually
	
	static int MIN_SUBSET_SIZE; // stop if subset size is less than this
	static double MIN_DEVIATION; // stop if deviation is less than this
	
	private static final int[] SUBSET_SIZES = {30, 40, 50, 60, 70};
	private static final double[] DEVIATION_SIZES = {1};
	
	private static final boolean SCORED = true;
	
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
	
	/*
	 * Prints an array to the screen
	 * @param array array to be printed
	 */
	public static String formatArray(int[] array) {
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
		Configuration config;
		List<List<Data>> subsets;
		
		if (!SCORED) {
			config = Parse.parseConfigFile("config/config.txt", "../data-collection/datasets/clean/clean_config_5000.txt");
			List<Data> examples = Parse.parseDataFile("../data-collection/datasets/clean/clean_data_5000.txt", 1, 2, config.getDiscrete(), config.getContinuous());

			//randomize(examples);
			
			subsets = splitList(examples, KFOLDS);
		}
		
		for (int s = 0; s < SUBSET_SIZES.length; s++) {
			for (int d = 0; d < DEVIATION_SIZES.length; d++) {
				MIN_SUBSET_SIZE = SUBSET_SIZES[s];
				MIN_DEVIATION = DEVIATION_SIZES[d];
				
				double[] error = new double[KFOLDS];
				double[] normError = new double[KFOLDS];
				
				if (SCORED) {
					// use for pre-generated training/test sets
					for (int fold = 0; fold < 10; fold++) {
						String path = "../data-collection/datasets/clean/";
						String subset = "clean_data_5000";
						config = Parse.parseConfigFile("config/score_config.txt", "../data-collection/datasets/clean/clean_config_5000.txt");
						List<Data> trainSet = Parse.parseDataFile(path + subset + "-score-subsets/" + subset + "-" + fold + "-train.txt", 1, 2, config.getDiscrete(), config.getContinuous());
						List<Data> testSet = Parse.parseDataFile(path + subset + "-score-subsets/" + subset + "-" + fold + "-test.txt", 1, 2, config.getDiscrete(), config.getContinuous());
						
						ModelTree tree = new ModelTree(config.getDiscrete(), trainSet);
						Evaluate eval = new Evaluate(tree, testSet);
						//System.out.println(eval.getRMS() + "\t" + eval.getNormRMS());
						error[fold] = eval.getRMS();
						normError[fold] = eval.getNormRMS();
					}
					
					//System.out.println(average(error) + "\t" + average(normError));
				} else {
					// regular 10-fold experiment
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
						//System.out.println(eval.getRMS() + "\t" + eval.getNormRMS());
						error[test] = eval.getRMS();
						normError[test] = eval.getNormRMS();
						
						if (FOLD >= 0) {
							break;
						}
					}
				}
				
				// print results
				System.out.println(MIN_SUBSET_SIZE + "\t" + MIN_DEVIATION + "\t" + average(error) + "\t" + average(normError));
			}
		}
		
		
	}
}
