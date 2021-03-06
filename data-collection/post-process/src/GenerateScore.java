import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * Class GenerateScore:
 * 
 * Places all actors, writers, and directors from two files,
 * a training and test file,
 * into several HashMaps, the key being the name of the said actor/director/writer
 * and the value being an ArrayList of rating values times some coefficient
 * to mark the position in the film (lead actors get more weight than the 
 * fifth actor mentioned).
 * 
 * These maps then used to generate a total actor, writer, and director
 * score for each film, which replaces the list of actors, writers, and 
 * directors, respectively, for that example.
 */

public class GenerateScore {
	
	HashMap <String, ArrayList<Double>> trainActors;
	HashMap <String, ArrayList<Double>> trainWriters;
	HashMap <String, ArrayList<Double>> trainDirectors;

	HashMap <String, ArrayList<Double>> testActors;
	HashMap <String, ArrayList<Double>> testWriters;
	HashMap <String, ArrayList<Double>> testDirectors;
	
	File training;
	File testing;
	
	public GenerateScore(String inputTraining, String inputTesting) {
		//reads from two text files
		training = new File(inputTraining);
		testing = new File(inputTesting);

		trainActors = new HashMap <String, ArrayList<Double>>();
		trainWriters = new HashMap <String, ArrayList<Double>>();
		trainDirectors = new HashMap <String, ArrayList<Double>>();

		testActors = new HashMap <String, ArrayList<Double>>();
		testWriters = new HashMap <String, ArrayList<Double>>();
		testDirectors = new HashMap <String, ArrayList<Double>>();
	}

	private void addToMap(HashMap <String, ArrayList<Double>> m, String str, Double rating) {
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(str.split(",")));
		for (int i = 0; i < list.size(); i++) {
			String key = list.get(i);
			ArrayList<Double> values = m.get(key);
			if  (values == null) {
				values = new ArrayList<Double>();
				m.put(key, values);
			}
			Double v = 1.0;
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

	public void CreateCSV(String outputTraining, String outputTesting) {
		buildHashMaps();
		
		//assume files don't exist
		File train_data = new File(outputTraining);
 		File test_data = new File(outputTesting);
		
		try {
	 		train_data.createNewFile();
			Scanner scanner = new Scanner(training);
			FileWriter train_fw = new FileWriter(train_data.getAbsoluteFile());
			BufferedWriter train_bw = new BufferedWriter(train_fw);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				ArrayList<String> example = new ArrayList<String>(Arrays.asList(line.split("\t")));
				Double a = calcScore(example.get(1), trainActors);
				Double d = calcScore(example.get(2), trainDirectors);
				Double w = calcScore(example.get(3), trainWriters);
				//String[] genrearr = example.get(4).split(",");
				//String genre = genrearr[0];
				String[] languagearr = example.get(5).split(",");
				String language = languagearr[0];
				String[] countryarr = example.get(6).split(",");
				String country = countryarr[0];
				String outputline = example.get(0) + "," + a + "," + d + "," + w + ",";
				outputline += getGenres(example.get(4));
				outputline += language + "," + country + "," + example.get(7) + "," + example.get(8) + "," + example.get(9) + "," + example.get(10) + "," + example.get(11) + "," + example.get(12) + "\n";
				train_bw.write(outputline);
			}
			scanner.close();
			train_bw.close();
			train_bw.close();

		} catch(IOException e) {
			e.printStackTrace();
		}

		//then testing
		try {
			test_data.createNewFile();
			FileWriter test_fw = new FileWriter(test_data.getAbsoluteFile());
			BufferedWriter test_bw = new BufferedWriter(test_fw);
			Scanner scanner = new Scanner(testing);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				ArrayList<String> example = new ArrayList<String>(Arrays.asList(line.split("\t")));
				Double a = calcScore(example.get(1), testActors);
				Double d = calcScore(example.get(2), testDirectors);
				Double w = calcScore(example.get(3), testWriters);
				//String[] genrearr = example.get(4).split(",");
				//String genre = genrearr[0];
				String[] languagearr = example.get(5).split(",");
				String language = languagearr[0];
				String[] countryarr = example.get(6).split(",");
				String country = countryarr[0];
				String outputline = example.get(0) + "," + a + "," + d + "," + w + ",";
				outputline += getGenres(example.get(4));
				outputline += language + "," + country + "," + example.get(7) + "," + example.get(8) + "," + example.get(9) + "," + example.get(10) + "," + example.get(11) + "," + example.get(12) + "\n";
				test_bw.write(outputline);
			}
			scanner.close();
			test_bw.close();
			test_bw.close();

		} catch(IOException e) {
			e.printStackTrace();
		}


	}
	
	//please don't look at this code
	//it is very bad
	//look away
	private String getGenres(String g) {
		String answer = "";
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(g.split(",")));
		String isFilmNoir = "false";
		String isCrime = "false";
		String isComedy = "false";
		String isShort = "false";
		String isMystery = "false";
		String isNews = "false";
		String isBiography = "false";
		String isAction = "false";
		String isWar = "false";
		String isHistory = "false";
		String isFantasy = "false";
		String isAdult = "false";
		String isMusic = "false";
		String isMusical = "false";
		String isThriller = "false";
		String isAnimation = "false";
		String isRomance = "false";
		String isHorror = "false";
		String isWestern = "false";
		String isAdventure = "false";
		String isFamily = "false";
		String isSciFi = "false";
		String isSport = "false";
		String isDrama = "false";
		String isDocumentary = "false";
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals("Film-Noir")) {
				isFilmNoir = "true";
			}
			else if (list.get(i).equals("Crime")) {
				isCrime = "true";
			}
			else if (list.get(i).equals("Comedy")) {
				isComedy = "true";
			}
			else if (list.get(i).equals("Short")) {
				isShort = "true";
			}
			else if (list.get(i).equals("Mystery")) {
				isMystery = "true";
			}
			else if (list.get(i).equals("News")) {
				isNews = "true";
			}
			else if (list.get(i).equals("Biography")) {
				isBiography = "true";
			}
			else if (list.get(i).equals("Action")) {
				isAction = "true";
			}
			else if (list.get(i).equals("War")) {
				isWar = "true";
			}
			else if (list.get(i).equals("History")) {
				isHistory = "true";
			}
			else if (list.get(i).equals("Fantasy")) {
				isFantasy = "true";
			}
			else if (list.get(i).equals("Adult")) {
				isAdult = "true";
			}
			else if (list.get(i).equals("Music")) {
				isMusic = "true";
			}
			else if (list.get(i).equals("Musical")) {
				isMusical = "true";
			}
			else if (list.get(i).equals("Thriller")) {
				isThriller = "true";
			}
			else if (list.get(i).equals("Animation")) {
				isAnimation = "true";
			}
			else if (list.get(i).equals("Romance")) {
				isRomance = "true";
			}
			else if (list.get(i).equals("Horror")) {
				isHorror = "true";
			}
			else if (list.get(i).equals("Western")) {
				isWestern = "true";
			}
			else if (list.get(i).equals("Adventure")) {
				isAdventure = "true";
			}
			else if (list.get(i).equals("Family")) {
				isFamily = "true";
			}
			else if (list.get(i).equals("Sci-Fi")) {
				isSciFi = "true";
			}
			else if (list.get(i).equals("Sport")) {
				isSport = "true";
			}
			else if (list.get(i).equals("Drama")) {
				isDrama = "true";
			}
			else if (list.get(i).equals("Documentary")) {
				isDocumentary = "true";
			}
		}
		answer += isFilmNoir + "," +isCrime + "," +isComedy + "," +isShort + "," +isMystery + "," +isNews + "," +isBiography + "," +isAction + "," +isWar + "," +isHistory +"," +isFantasy +"," +isAdult +"," +isMusic +"," +isMusical +"," +isThriller +"," +isAnimation +"," +isRomance +"," +isHorror +"," +isWestern +"," +isAdventure +"," +isFamily +"," +isSciFi + "," + isSport + "," + isDrama + "," + isDocumentary + ",";
		return answer;
	}

	private Double calcScore(String a, HashMap<String, ArrayList<Double>> m) {
		Double rating = 0.0;
		if (a == null) { return rating;}
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(a.split(",")));
		for (int i = 0; i < list.size(); i++) {
			ArrayList<Double> values = m.get(list.get(i));
			if (values == null) {
				//do nothing
			}
			else {
				double total = 0;
				for (int j = 0; j < values.size(); j++) {
					total += values.get(j);
				}
				total /= values.size();
				rating += total;
			}
		}
		return rating;
	}

	public float Normalize(float var, float max, float min) {
		float answer = var - min;
		answer /= (max-min);
		return answer;
	}




}