import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class Test {
	/*
	 * Shuffles the elements in a list.
	 * @param list list to shuffle
	 */
	public static void randomize(List<String> list) {
		for (int i = list.size() - 1; i > 0; i--) {
			int rand = (int)( Math.random() * i );
			String temp = list.get(i);
			list.set(i, list.get(rand));
			list.set(rand, temp);
		}
	}
	
	public static double average(List<Float> examples) {
		double average = 0;
		for (int i = 0; i < examples.size(); i++) {
			average += examples.get(i);
		}
		return average / examples.size();
	}
	
	/*
	 * Compute sample standard deviation of set of example outputs.
	 * Assume that examples contains at least two data elements.
	 * @param examples set of data examples
	 * @return sample standard deviation
	 */
	public static double standardDeviation(double average, List<Float> examples) {
		double result = 0;
		for (int i = 0; i < examples.size(); i++) {
			double temp = examples.get(i) - average;
			result += temp * temp;
		}
		result /= examples.size() - 1;
		return Math.sqrt(result);
	}
	
	public static void main(String[] args) throws Exception {
		// check number of multiple values for attribute
		/*
		Scanner scanner = Parse.openFile("../data-collection/noisy_data_revision1.txt");
		Map<Integer, List<Float>> map = new HashMap<Integer, List<Float>>();
		while (scanner.hasNextLine()) {
			String[] example = scanner.nextLine().split("\t");
			int key = Integer.parseInt(example[11]); 
			if (map.containsKey(key)) {
				map.get(key).add(Float.parseFloat(example[2]));
				//count.put(key, count.get(key) + 1);
			} else {
				map.put(key, new ArrayList<Float>( Arrays.asList(Float.parseFloat(example[2])) ));
			}
		}
		SortedSet<Integer> keys = new TreeSet<Integer>(map.keySet());
		for (Integer key : keys) {
			List<Float> set = map.get(key);
			double avg = average(set);
			System.out.println(key + "\t" + avg + "\t" + standardDeviation(avg, set));
		}
		*/
		
		// shuffle data file
		Scanner scanner = Parse.openFile("../data-collection/clean_data.txt");
		List<String> examples = new ArrayList<String>();
		while (scanner.hasNextLine()) {
			examples.add(scanner.nextLine());
		}
		randomize(examples);
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("../data-collection/subsets/clean_data.txt").getAbsoluteFile()));
		for (int i = 0; i < 1000; i++) {
			bw.write(examples.get(i) + "\n");
		}
		bw.close();
		
		// mess with runtimes
		/*
		// declare attributes
		Attribute rating = new Attribute("rating");
		Attribute runtime = new Attribute("runtime");
		
		// Declare the feature vector
		FastVector attributes = new FastVector(2);
		attributes.addElement(rating);
		attributes.addElement(runtime);
		
		// Create an empty training set
		Instances trainingSet = new Instances("movie-runtimes", attributes, 10);
		trainingSet.setClassIndex(0);
		
		// open file
		Scanner scanner = Parse.openFile("../data-collection/runtime-data-test.txt");
		
		// add examples
		while (scanner.hasNextLine()) {
			double[] values = new double[attributes.size()];
			String[] temp = scanner.nextLine().split(",");
			for (int i = 0; i < attributes.size(); i++) {
				values[i] = Double.parseDouble(temp[i]);
			}
			trainingSet.add(new Instance(1, values));
		}

		// add the instance
		LinearRegression model = new LinearRegression();
		model.buildClassifier(trainingSet);

		// test the model
		Evaluation eval = new Evaluation(trainingSet);
		eval.evaluateModel(model, trainingSet);

		// print results
		double[] coef = model.coefficients();
		System.out.println("[" + coef[1] + ", " + coef[2] + "]");
		System.out.println(eval.toSummaryString());
		*/
	}
}
