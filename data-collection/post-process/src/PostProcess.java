import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PostProcess {
	
	public int dayOfWeek (int d, int m, int y) {
		int c = getCentury(y);
		y = y % 100;
		return (int) ((d + m + y + (float)y/4 + c) % 7);
	}

	public int getCentury (int year) {
		int c = year / 100;
		if (c % 4 == 0) return 6;
		else if (c % 4 == 1) return 4;
		else if (c % 4 == 2) return 2;
		else return 0;
	}
	
	public int divideMonth(int d) {
		if (d < 11) return 0;
		else if (d < 21) return 1;
		else return 2;
	}

	public static int missingValues(String msg) {
		List<String> arr = Arrays.asList(msg.split("\t"));
		int count = 0;
		for (String a:  arr) {
			if (a == null) { count++;}
		}
		return count;
	}
	
	public static void main(String[] args) {
		// populate misfits data structure
		/*
		File file = new File("../misfits.txt");
		Map<String, List<String>> misfits = null;
		try {
			Scanner scanner = new Scanner(file);
			misfits = new HashMap<String, List<String>>();
			while (scanner.hasNextLine()) {
				List<String> temp = Arrays.asList(scanner.nextLine().split("\t"));
				misfits.put(temp.get(0), temp.subList(1, temp.size()));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		*/

		//create two files, one for clean data and one for noisy data
		File clean_data = new File("../clean_data.txt");
 		File noisy_data = new File("../noisy_data.txt");
		// if file doesnt exist, then create it
		if (!clean_data.exists()) {
			clean_data.createNewFile();
		}
		if (!noisy_data.exists()) {
			noisy_data.createNewFile();
		}

		//remove anything with five or above values, missing writers, actors, or directors
		FileWriter noisy_fw = new FileWriter(noisy_data.getAbsoluteFile());
		BufferedWriter noisy_bw = new BufferedWriter(noisy_fw);

		FileWriter clean_fw = new FileWriter(clean_data.getAbsoluteFile());
		BufferedWriter clean_bw = new BufferedWriter(clean_bw);

		file = new File("../movie_data_fixed.txt");

		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String str = scanner.nextLine();
				List<String> example = Arrays.asList(str.split("\t"));
				int missing = missingValues(str);
				if (missing > 0) {
					//if there are more than 3 missing values, throw out the data
					if (missing > 3) {
						//throw it out
					} 
					//iterate through array list checking for missing writers, actors, directors
					else {

						if (example.get(4) == null) { 
							//skip
						}
						else if (example.get(5) == null) {
							//skip
						}
						else if (example.get(6) == null) {
							//skip
						}
						else { //otherwise we are OK to write to noisy data
							noisy_bw.write(str + "\n");
						}

					}
				}
				else { // there aren't any missing feature values
					clean_bw.write(str + "\n"); //note we only write to clean if nothing's wrong
					noisy_bw.write(str + "\n");
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		noisy_bw.close();
		clean_bw.close();

		//find the average of rating, rating_count
		double rating_average = 0;
		double rating_count_average = 0;
		int average_count = 0;

		double runtime_average = 0;
		int runtime_count = 0;
		
		file = new File("../noisy_data.txt");
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String str = scanner.nextLine();
				List<String> example = Arrays.asList(str.split("\t"));
				if (example.get(2) != null && example.get(3) != null) {
					average_count++;
					rating_average += Double.parseDouble(example.get(2));
					rating_count_average += Double.parseDouble(example.get(3));
				}
				if (example.get(example.size()-1) != null) {
					runtime_count++;
					runtime_average += Double.parseDouble(example.get(example.size()-1));
				}
			}
			rating_average /= average_count;
			rating_count_average /= average_count;
			runtime_average /= runtime_count;
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//replace missing values
		file = new File("../noisy_data.txt");
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String str = scanner.nextLine();
				List<String> example = Arrays.asList(str.split("\t"));
				if (misfits.containsKey(example.get(1))) {
					// there are missing feature values 
					for (String feature : misfits.get(example.get(1))) {
						switch (feature) {
							case "rating":
								// do stuff
								break;
							default:
								break;
						}
					}
				} else {
					// there aren't any missing feature values
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
