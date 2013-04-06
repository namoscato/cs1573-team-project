import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ModelTree { 
	private final List<String> classValues;
	private final List<Feature> features;
	private final List<Data> trainingData;
	private final List<Data> testData;
	private Node root;
	
	private int trainingCorrect;
	private double trainingAccuracy;
	private int testCorrect;
	private double testAccuracy;
	
	private static final boolean DEBUG = false;
	private static final String hr = "---------------------------";
	
	/*
	 * Creates a ModelTree object and calculates the accuracy on
	 * the training and test data.
	 * @param classValues list of classification values
	 * @param features list of features
	 * @param trainingData list of training data
	 * @param testData list of test data
	 * @param function number that specifies gain function
	 */
	public ModelTree(List<String> classValues, List<Feature> features, List<Data> trainingData, List<Data> testData, int function) {
		this.classValues = classValues;
		this.features = new ArrayList<Feature>(features); // we mess with the features array
		this.trainingData = trainingData;
		this.testData = testData;
		if (DEBUG) {
			System.out.println(classValues + "\n" + features + "\n" + trainingData + "\n" + testData + "\n");
		}
		//root = ID3(this.trainingData, this.features, function);
		
		// see how we did
		/*
		trainingCorrect = numberCorrect(trainingData);
		trainingAccuracy = (double)trainingCorrect / trainingData.size();
		testCorrect = numberCorrect(testData);
		testAccuracy = (double)testCorrect / testData.size();
		*/
	}
	
	
	public void M5(List<Data> examples, List<Feature> features) {
		Node parent = new Node();
		
		// if examples.size() is small or standard deviation (difference of examples) is below some threshold
		// if features is empty, just run the linear model
		// else:
		
		Feature chosen;
		double min = Double.POSITIVE_INFINITY;
		List<List<Data>> subsets = null;
		for (Feature feature : features) {
			List<List<Data>> sub = classifyByFeatureValue(examples, feature);
			
			// find total count (including duplicates)
			double count = 0;
			for (List<Data> subset : sub) {
				count += subset.size();
			}
			
			// find standard deviation and adjust for size of subset
			double temp = 0;
			for (List<Data> subset : sub) {
				temp += standardDeviation(subset) * subset.size() / count;
			}
			if (temp < min) {
				min = temp;
				chosen = feature;
				subsets = sub;
			}
		}
		
		// we now know chosen is the feature we are going to use
		for (List<Data> subset : subsets) {
			if (subset.size() == 0) {
				// run examples through linear model with continuous feature values
				//count = classifyByLabel(examples);
				//Node child = new Node( classValues.get(max(count)) );
				//parent.addChild(chosen.getValue(i), child);
			} else {
				// only remove the feature for our recursion
				//List<Feature> smaller = new ArrayList<Feature>(features);
				//smaller.remove(chosen);
				//parent.addChild( chosen.getValue(i), ID3(subsets.get(i), smaller, function, level + 1) );
			}
		}
	}
	
	/*
	 * Compute sample standard deviation of set of example outputs.
	 * @param examples set of data examples
	 * @return sample standard deviation
	 */
	public double standardDeviation(List<Data> examples) {
		// compute average
		double average = 0;
		for (int i = 0; i < examples.size(); i++) {
			average += examples.get(i).getOutput();
		}
		average /= examples.size();
		
		double result = 0;
		for (int i = 0; i < examples.size(); i++) {
			double temp = examples.get(i).getOutput() - average;
			result += temp * temp;
		}
		result /= examples.size() - 1;
		return Math.sqrt(result);
	}
	
	/*
	 * Classify set by the values of a specified feature
	 * @param set set of data examples
	 * @param feature feature to classify examples by
	 * @return list of subsets classified by feature value
	 */
	private List<List<Data>> classifyByFeatureValue(List<Data> set, Feature feature) {
		// get the values for this feature
		List<String> featureValues = feature.getValues();
				
		// initialize our subsets
		List<List<Data>> subsets = new ArrayList<List<Data>>(featureValues.size());
		for (int i = 0; i < featureValues.size(); i++) {
			subsets.add( new ArrayList<Data>() );
		}
				
		// classify the set according to chosen feature value
		for (Data example : set) {
			int valueIndex = featureValues.indexOf( example.getDiscrete(feature.getID()) );
			subsets.get(valueIndex).add(example);
		}
		return subsets;
	}
	
	/*
	 * ModelTree node represents a feature or a classification value.
	 * If the node represents a feature, it has n children represented as
	 * a Map<String, Node> where the n key values represent the possible
	 * values for that feature.
	 */
	protected class Node {
		private Feature feature;
		private Map<String, Node> children;
		private List<Data> examples;
		private double output;
		
		private static final double LEARNING_RATE = .005;
		
		public Node() {
			feature = null;
			children = null;
			examples = null;
			output = Double.NaN;
		}
		
		public Node(List<Data> examples) {
			feature = null;
			children = null;
			this.examples = examples;
			// compute continuous output if leaf
			//output = new LinearEquation(perceptron(examples);
		}
		
		public Feature getFeature() {
			return feature;
		}
		
		public String getFeatureName() {
			return feature.getName();
		}
		
		public double getOutput() {
			return output;
		}
		
		public Map<String, Node> getChildren() {
			return children;
		}
		
		public Node getChild(String key) {
			return children.get(key);
		}
		
		public void setFeature(Feature feature) {
			this.feature = feature;
		}
		
		public void addChild(String key, Node node) {
			if (children == null) {
				children = new HashMap<String, Node>();
			}
			children.put(key, node);
		}
		
		public int countChildren() {
			if (children == null) {
				return 0;
			} else {
				return children.size();
			}
		}
		
		public boolean isLeaf() {
			if (output == Double.NaN) {
				return false;
			} else {
				return true;
			}
		}
		
		// keep going through perceptron until we hit some defined limit or we converge
		// NEED TO COME UP WITH SOME OTHER WAY TO STOP PERCEPTRON
		// assume data has at least one continuous feature
		private double[] perceptron(List<Data> examples) {
			double[] weights = new double[examples.get(0).getContinuousSize() + 1];
			while (true) {
				double[] newWeights = perceptron(examples, weights);
				if (!newWeights.equals(weights)) {
					weights = newWeights; // continue
				} else {
					break; // nothing was updated, so we're done
				}
			}
			return weights;
		}
		
		private double[] perceptron(List<Data> examples, double[] oldWeights) {
			double weights[] = oldWeights.clone();
			double[] diff = new double[weights.length];
			for (Data example : examples) {
				// sum the difference for every weight
				for (int i = 0; i < weights.length; i++) {
					List<Number> input = example.getContinuous();
					LinearEquation eq = new LinearEquation(weights, convertDoubles(input));
					double temp = LEARNING_RATE * (example.getOutput() - logisticRegression(eq));
					if (i > 0) {
						// w_0 has an x value of 1; otherwise, multiply by input
						temp *= input.get(i - 1).doubleValue();
					}
					diff[i] += temp;
				}
			}
			
			// update weights
			for (int i = 0; i < weights.length; i++) {
				weights[i] += diff[i];
			}
			return weights;
		}
		
		/*
		 * Compute logistic regression with an adjusted coefficient of 10.
		 * @param eq linear equation
		 */
		private double logisticRegression(LinearEquation eq) {
			return 10.0 / (1 + Math.exp(-1 * eq.getResult()));
		}
		
		/*
		 * Convert a list of numbers to a primitive double array
		 * @param list list of Numbers
		 */
		private double[] convertDoubles(List<Number> list) {
			double[] result = new double[list.size()];
			for (int i = 0; i < result.length; i++) {
				result[i] = list.get(i).doubleValue();
			}
			return result;
		}
	}
	
	public static void main(String[] args) throws ParseException {
		Configuration config = Parse.parseConfigFile("../config/config.txt", "../config/clean_config.txt");
		List<Data> dataset = Parse.parseDataFile("../data-collection/clean_data.txt", 1, 2, config.getDiscrete(), config.getContinuous());
		System.out.println(dataset.get(0));
	}
}