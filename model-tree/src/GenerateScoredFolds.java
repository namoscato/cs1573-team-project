import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class GenerateScoredFolds {
	
	/*
	 * Shuffles the elements in a list.
	 * @param list list to shuffle
	 */
	public static void randomize(List<List<String>> list) {
		for (int i = list.size() - 1; i > 0; i--) {
			int rand = (int)( Math.random() * i );
			List<String> temp = list.get(i);
			list.set(i, list.get(rand));
			list.set(rand, temp);
		}
	}
	
	/*
	 * @param examples list to average
	 * @return average of list
	 */
	public static double average(List<Double> examples) {
		double average = 0;
		for (int i = 0; i < examples.size(); i++) {
			average += examples.get(i);
		}
		return average / examples.size();
	}
	
	/*
	 * Splits a list into k distinct subsets in preparation for
	 * k-fold validation.
	 * @param list list to split
	 * @param k number of distinct subsets
	 * @return k distinct subsets
	 */
	public static List<List<List<String>>> splitList(List<List<String>> list, int k) {
		List<List<List<String>>> subsets = new ArrayList<List<List<String>>>();
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
	 * Generates k folds with actor, director and writer attributes
	 * converted to nominal scores.
	 * @param inputStr path to input file
	 * @param k number of folds
	 */
	public static void generateFolds(String inputStr, int k) throws ParseException {
		
	}
	
	/*
	 * Replaces the nominal values of a specified attribute with a calculated score 
	 */
	public static List<List<String>> replaceScore(List<List<String>> examples, List<Map<String, List<Double>>> ratings, int attr) {
		return replaceScore(examples, ratings, attr, -1, -1, -1);
	}
	
	/*
	 * Replaces the nominal values of a specified attribute with a calculated score
	 */
	public static List<List<String>> replaceScore(List<List<String>> examples, List<Map<String, List<Double>>> ratings, int attr, double average, double min, double max) {
		for (int i = 0; i < examples.size(); i++) {
			List<String> example = examples.get(i);
			
			// find our scores for each person
			String[] temp = example.get(attr).split(",");
			List<Double> myScores = new ArrayList<Double>(temp.length);
			for (String person : temp) {
				if (average > 0 && !ratings.contains(person)) {
					// person only exists in our test set
					double score = average;
					if (max > 0) {
						score = (score - min) / (max - min);
					}
					myScores.add(score);
				} else {
					// we saw this person in our training, so we're good to go
					double score = average(ratings.get(attr - 4).get(person));
					if (max > 0) {
						score = (score - min) / (max - min);
					}
					myScores.add(score);
				}
			}
			
			// replace this aggregated score with nominal values
			example.set(attr, String.valueOf(calcScore(myScores)));
		}
		return examples;
	}
	
	/*
	 * Combine actor/director/writer scores into one numerical value.
	 */
	public static double calcScore(List<Double> scores) {
		double score = 0;
		int max = 0;
		for (int i = 0; i < scores.size(); i++) {
			double weight;
			switch(i) {
				case 0:
					weight = 10;
					break;
				case 1:
					weight = 5;
					break;
				case 2:
					weight = 1;
					break;
				case 3:
					weight = .5;
					break;
				default:
					weight = .3;
					break;
			}
			score += scores.get(i);
			//max += weight * 10;
		}
		return score;
	}
	
	/*
	 * Calculates the score on the test dataset
	 * @param testset test dataset
	 * @param index 0:actor, 1:director, 2:writer
	 */
	public static Map<String, List<Double>> buildMap(List<List<String>> testset, int index) {
		Map<String, List<Double>> scores = new HashMap<String, List<Double>>();
		
		int count = 0;
		for (List<String> example : testset) {
			String[] people = example.get(index).split(",");
			for (String person : people) {
				double rating = Double.parseDouble(example.get(2));
				List<Double> temp = scores.get(person);
				if (temp == null) {
					count++;
					scores.put(person, new ArrayList<Double>( Arrays.asList(rating) ));
				} else {
					temp.add(rating);
					scores.put(person, temp);
				}
			}
		}
		return scores;
	}
	
	public static void main(String[] args) {
		final String INPUT = "../data-collection/subsets/USA_data.txt";
		final int FOLDS = 10;
		
		List<List<String>> examples = new ArrayList<List<String>>();
		BufferedWriter bw;
		File file;
		
		try {			
			// open our input file
			File input = new File(INPUT);
			String filename = input.getName();
			filename = filename.substring(0, filename.lastIndexOf('.'));
			String path = input.getPath();
			path = path.substring(0, path.lastIndexOf('/')) + '/';
			
			// bring our dataset into main memory
			Scanner scan = new Scanner(input);
			while (scan.hasNext()) {
				examples.add(Arrays.asList(scan.nextLine().split("\t")));
			}
			
			randomize(examples);
			
			// generate k training and test datasets
			List<List<List<String>>> subsets = splitList(examples, FOLDS);
			for (int test = 0; test < subsets.size(); test++) {
				
				// create our training set
				List<List<String>> trainSet = new ArrayList<List<String>>();
				for (int i = 0; i < subsets.size(); i++) {
					if (i != test) {
						for (List<String> a : subsets.get(i)) {
							List<String> values = new ArrayList<String>();
							for (String b : a) {
								values.add(b);
							}
							trainSet.add(values);
						}
					}
				}
				
				// get our test set
				List<List<String>> testSet = new ArrayList<List<String>>();
				for (List<String> item : subsets.get(test)) {
					List<String> values = new ArrayList<String>();
					for (String b : item) {
						values.add(b);
					}
					testSet.add(values);
				}
				
				// get our ratings from the training set
				List<Map<String, List<Double>>> ratings = Arrays.asList(buildMap(trainSet, 4), buildMap(trainSet, 5), buildMap(trainSet, 6));
				
				// calculate the scores for our training set
				for (int j = 4; j <= 6; j++) {
					trainSet = replaceScore(trainSet, ratings, j);
				}
				
				// find the average score for examples in the training set
				double[] averages = new double[3];
				double[] min = {Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE};
				double[] max = {Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE};
				for (List<String> example : trainSet) {
					for (int i = 4; i <= 6; i++) {
						double temp = Double.parseDouble(example.get(i));
						averages[i - 4] += Double.parseDouble(example.get(2));
						if (temp < min[i - 4]) {
							min[i - 4] = temp;
						}
						if (temp > max[i - 4]) {
							max[i - 4] = temp;
						}
					}
				}
				for (int i = 0; i < 3; i++) {
					averages[i] /= trainSet.size();
				}
				
				// normalize scores
				for (List<String> example : trainSet) {
					for (int i = 4; i <= 6; i++) {
						double temp = Double.parseDouble(example.get(i));
						example.set( i, String.valueOf((temp - min[i - 4]) / (max[i - 4] - min[i - 4])) );
					}
				}
				
				// calculate the scores for our test set
				for (int j = 4; j <= 6; j++) {
					replaceScore(testSet, ratings, j, averages[j - 4], min[j - 4], max[j - 4]);
				}
				
				// write stuff to files
				file = new File(path + filename + '/' + filename + "-" + test + "-train.txt" );
				file.getParentFile().mkdirs();
				bw = new BufferedWriter(new FileWriter(file));
				for (List<String> example : trainSet) {
					bw.write(Parse.createLine(example));
				}
				bw.close();
				
				file = new File(path + filename + '/' + filename + "-" + test + "-test.txt" );
				file.getParentFile().mkdirs();
				bw = new BufferedWriter(new FileWriter(file));
				for (List<String> example : testSet) {
					bw.write(Parse.createLine(example));
				}
				bw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
