import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
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

	File training;
	File test;
	public GenerateScore(String training, String test) {
		//reads from two text files
		training = new File(training);
		test = new File(test);
	}

	public void Calculate() {
		//calc for training
		try {
			Scanner scanner = new Scanner(training);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				ArrayList<String> example = new ArrayList<String>(Arrays.asList(line.split("\t")));
				if (example.get(2) != null) {
					Double rating = Double.parseDouble(example.get(2));
					calcActorsTraining(example.get(4), rating);
					calcDirectorsTraining(example.get(5), rating);
					calcWritersTraining(example.get(6), rating);
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
				if (example.get(2) != null) {
					Double rating = Double.parseDouble(example.get(2));
					calcActorsTesting(example.get(4), rating);
					calcDirectorsTesting(example.get(5), rating);
					calcWritersTesting(example.get(6), rating);
				}
				else {
					//do not adjust the score if for whatever reason the rating is null
				}
			}


		} catch(IOException e) {
			e.printStackTrace();
		}


	}

	private void calcActorsTraining(String a, Double rating) {
		if (a == null) { return ;}
		ArrayList<String> actors = new ArrayList<String>(Arrays.asList(a.split(",")));
		for (int i = 0; i < actors.size(); i++) {
			switch (i) {
				case 0: {
					actorscore_training += 10 * rating;
					break;
				}
				case 1: {
					actorscore_training += 5 * rating;
					break;
				}
				case 2: {
					actorscore_training += 1 * rating;
					break;
				}
				case 3: {
					actorscore_training += 0.5 * rating;
					break;
				}
				case 4: {
					actorscore_training += 0.3 * rating;
					break;
				}
			}
		}

	}

	private void calcDirectorsTraining(String d, Double rating) {
		if (d == null) { return; }
		ArrayList<String> directors = new ArrayList<String>(Arrays.asList(d.split(",")));
		for (int i = 0; i < directors.size(); i++) {
			switch (i) {
				case 0: {
					directorscore_training += 10 * rating;
					break;
				}
				case 1: {
					directorscore_training += 5 * rating;
					break;
				}
				case 2: {
					directorscore_training += 1 * rating;
					break;
				}
				case 3: {
					directorscore_training += 0.5 * rating;
					break;
				}
				case 4: {
					directorscore_training += 0.3 * rating;
					break;
				}
			}
		}
		
	}

	private void calcWritersTraining(String w, Double rating) {
		if (w == null ) { return;}
		ArrayList<String> writers = new ArrayList<String>(Arrays.asList(w.split(",")));
		for (int i = 0; i < writers.size(); i++) {
			switch (i) {
				case 0: {
					writerscore_training += 10 * rating;
					break;
				}
				case 1: {
					writerscore_training += 5 * rating;
					break;
				}
				case 2: {
					writerscore_training += 1 * rating;
					break;
				}
				case 3: {
					writerscore_training += 0.5 * rating;
					break;
				}
				case 4: {
					writerscore_training += 0.3 * rating;
					break;
				}
			}
		}
		
	}

	private void calcActorsTesting(String a, Double rating) {
		if (a == null) { return ;}
		ArrayList<String> actors = new ArrayList<String>(Arrays.asList(a.split(",")));
		for (int i = 0; i < actors.size(); i++) {
			switch (i) {
				case 0: {
					actorscore_testing += 10 * rating;
					break;
				}
				case 1: {
					actorscore_testing += 5 * rating;
					break;
				}
				case 2: {
					actorscore_testing += 1 * rating;
					break;
				}
				case 3: {
					actorscore_testing += 0.5 * rating;
					break;
				}
				case 4: {
					actorscore_testing += 0.3 * rating;
					break;
				}
			}
		}

	}

	private void calcDirectorsTesting(String d, Double rating) {
		if (d == null) { return; }
		ArrayList<String> directors = new ArrayList<String>(Arrays.asList(d.split(",")));
		for (int i = 0; i < directors.size(); i++) {
			switch (i) {
				case 0: {
					directorscore_testing += 10 * rating;
					break;
				}
				case 1: {
					directorscore_testing += 5 * rating;
					break;
				}
				case 2: {
					directorscore_testing += 1 * rating;
					break;
				}
				case 3: {
					directorscore_testing += 0.5 * rating;
					break;
				}
				case 4: {
					directorscore_testing += 0.3 * rating;
					break;
				}
			}
		}
		
	}

	private void calcWritersTesting(String w, Double rating) {
		if (w == null ) { return;}
		ArrayList<String> writers = new ArrayList<String>(Arrays.asList(w.split(",")));
		for (int i = 0; i < writers.size(); i++) {
			switch (i) {
				case 0: {
					writerscore_testing += 10 * rating;
					break;
				}
				case 1: {
					writerscore_testing += 5 * rating;
					break;
				}
				case 2: {
					writerscore_testing += 1 * rating;
					break;
				}
				case 3: {
					writerscore_testing += 0.5 * rating;
					break;
				}
				case 4: {
					writerscore_testing += 0.3 * rating;
					break;
				}
			}
		}
		
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