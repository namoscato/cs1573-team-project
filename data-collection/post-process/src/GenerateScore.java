import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IoException;
public class GenerateScore {
	//init all scores to be 1
	//then increase the score proportional to the rating
	// score += rating * coefficient
	// coefficients 10, 5, 1, 0.5, 0.3
	float actorscore_training = 1;
	float directorscore_training = 1;
	float writerscore_training = 1;

	float actorscore_testing = 1;
	float directorscore_testing = 1;
	float writerscore_testing = 1;

	HashMap trainActors<String, Collection<Double>>;
	HashMap trainWriters<String, Collection<Double>>;
	HashMap trainDirectors<String, Collection<Double>>;

	HashMap testActors<String, Collection<Double>>;
	HashMap trainWriters<String, Collection<Double>>;
	HashMap testDirectors<String, Collection<Double>>;
	File training;
	File test;
	public GenerateScore(String training, String test) {
		//reads from two text files
		training = new File(training);
		test = new File(test);

		trainActors = new HashMap<String, Collection<Double>>();
		trainWriters = new HashMap<String, Collection<Double>>();
		trainDirectors = new HashMap<String, Collection<Double>>();

		testActors = new HashMap<String, Collection<Double>>();
		testWriters = new HashMap<String, Collection<Double>>();
		testDirectors = new HashMap<String, Collection<Double>>();
	}

	private void addToMap(HashMap<String, ArrayList> m, String str, Double rating) {
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(str.split(",")));
		for (int i = 0; i < list.size(); i++) {
			String key = list.get(i);
			Collection<Double> values = m.get(key);
			if  (values == null) {
				values = new ArrayList<Double>();
				m.put(key, values);
			}
			Double v = 1;
			switch (i) {
				case 0: {
					v += 10 * rating;
					break;
				}
				case 1: {
					v += 5 * rating;
					break;
				}
				case 2: {
					v += 1 * rating;
					break;
				}
				case 3: {
					v += 0.5 * rating;
					break;
				}
				case 4: {
					v += 0.3 * rating;
					break;
				}
			}
			values.add(v);
		}

	}

	private void buildHashMaps() {
		try {
			Scanner scanner = new Scanner(training);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				ArrayList<String> example = new ArrayList<String>(Arrays.asList(line.split("\t")));
				if (example.get(12) != null) {
					Double rating = Double.parseDouble(example.get(12));
					addToMap(trainActors, example.get(1), rating);
					addToMap(trainDirectors, example.get(2), rating);
					addToMap(trainWriters, example.get(3), rating);
				}
			}
			scanner.close();


		} catch(IOException e) {
			e.printStackTrace();
		}

		try {
			Scanner scanner = new Scanner(testing);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				ArrayList<String> example = new ArrayList<String>(Arrays.asList(line.split("\t")));
				if (example.get(12) != null) {
					Double rating = Double.parseDouble(example.get(12));
					addToMap(testActors, example.get(1), rating);
					addToMap(testDirectors, example.get(2), rating);
					addToMap(testWriters, example.get(3), rating);
				}
			}
			scanner.close();

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void Calculate() {
		buildHashMaps();
		//calc for training
		try {
			Scanner scanner = new Scanner(training);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				ArrayList<String> example = new ArrayList<String>(Arrays.asList(line.split("\t")));
				if (example.get(12) != null) {
					Double rating = Double.parseDouble(example.get(12));
					calcActorsTraining(example.get(1), rating);
					calcDirectorsTraining(example.get(2), rating);
					calcWritersTraining(example.get(3), rating);
				}
				else {
					//do not adjust the score if for whatever reason the rating is null
				}
			}


		} catch(IOException e) {
			e.printStackTrace();
		}

		//then testing
		try {
			Scanner scanner = new Scanner(testing);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				ArrayList<String> example = new ArrayList<String>(Arrays.asList(line.split("\t")));
				if (example.get(12) != null) {
					Double rating = Double.parseDouble(example.get(12));
					calcActorsTesting(example.get(1), rating);
					calcDirectorsTesting(example.get(2), rating);
					calcWritersTesting(example.get(3), rating);
				}
				else {
					//do not adjust the score if for whatever reason the rating is null
				}
			}


		} catch(IOException e) {
			e.printStackTrace();
		}


	}

	private Double calcScore(String a) {
		Double rating = 1;
		if (a == null) { return rating;}
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(a.split(",")));
		for (int i = 0; i < list.size(); i++) {
			
		}
		return rating;
	}

	public float Normalize(float var, float max, float min) {
		float answer = var - min;
		answer /= (max-min);
		return answer;
	}

	public void printScores() {
		System.out.println("TRAINING:   ");
		Sstem.out.println("Actor score:  " + actorscore_training);
		Sstem.out.println("Director score:  " + directorscore_training);
		Sstem.out.println("Writer score:  " + writerscore_training);

		System.out.println("TESTING:   ");
		Sstem.out.println("Actor score:  " + actorscore_testing);
		Sstem.out.println("Director score:  " + directorscore_testing);
		Sstem.out.println("Writer score:  " + writerscore_testing);

	}



}