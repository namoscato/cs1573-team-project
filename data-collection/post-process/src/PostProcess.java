import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PostProcess {
	
	/*
	 * Returns the day of the week
	 */
	public static int dayOfWeek (String s_d, String s_m, String s_y) {
		int d = Integer.parseInt(s_d), m = Integer.parseInt(s_m), y = Integer.parseInt(s_y);
		int c = getCentury(y);
		y = y % 100;
		return (int) ((d + m + y + (float)y/4 + c) % 7);
	}
	
	private static int getCentury (int year) {
		int c = year / 100;
		if (c % 4 == 0) return 6;
		else if (c % 4 == 1) return 4;
		else if (c % 4 == 2) return 2;
		else return 0;
	}
	
	/*
	 * Splits month into three partitions
	 */
	public static int divideMonth(String s) {
		int d = Integer.parseInt(s);
		if (d < 11) return 0;
		else if (d < 21) return 1;
		else return 2;
	}
	
	/*
	 * Counts the number of missing values
	 */
	public static int missingValues(String msg) {
		List<String> arr = Arrays.asList(msg.split("\t"));
		int count = 0;
		for (String a : arr) {
			if (a.equals("null")) {
				count++;
			}
		}
		return count;
	}
	
	public static void main(String[] args) {
		File input = new File("../movie_data_revision2.txt");
		
		// create two files, one for clean data and one for noisy data
		File clean_data = new File("../clean_data.txt");
 		File noisy_data = new File("../noisy_data.txt");
 		
		try {
			// assume files do not exist
			clean_data.createNewFile();
			noisy_data.createNewFile();
			
			// clean file will only include examples with no missing values
			FileWriter clean_fw = new FileWriter(clean_data.getAbsoluteFile());
			BufferedWriter clean_bw = new BufferedWriter(clean_fw);
			
			// noisy file will include examples with adjusted missing values
			FileWriter noisy_fw = new FileWriter(noisy_data.getAbsoluteFile());
			BufferedWriter noisy_bw = new BufferedWriter(noisy_fw);
			
			Scanner scanner = new Scanner(input);
			
			// initialize some noisy data stuff
			double rating_average = 0;
			double rating_count_average = 0;
			int rating_count = 0;
			double runtime_average = 0;
			int runtime_count = 0;
			
			while (scanner.hasNextLine()) {
				String str = scanner.nextLine();
				List<String> example = new ArrayList<String>(Arrays.asList(str.split("\t")));
				
				int missing = missingValues(str); // reflects distribution in FixNulls.java (before we add any more features)
				
				// compute new feature values
				if (example.get(13).equals("null")) {
					// if release day is null, just add some null values
					example.add(example.size() - 1, "null");
					example.add(example.size() - 1, "null");
				} else {
					// otherwise, compute position in month and day of week 
					example.add( example.size() - 1, Integer.toString(divideMonth(example.get(13))) );
					example.add( example.size() - 1, Integer.toString(dayOfWeek(example.get(13), example.get(12), example.get(11))) );
				}
				
				// only write to clean if there are no missing values
				if (missing == 0) {
					clean_bw.write(FixMistakes.createLine(example));
				}
				
				// write to noisy if < 6 missing values (see FixNulls.java for distribution) and actors, directors and writers are not null
				if (missing < 6 && !example.get(4).equals("null") && !example.get(5).equals("null") && !example.get(6).equals("null")) {
					noisy_bw.write(FixMistakes.createLine(example)); // write line as is (with nulls)
					
					// if we have a rating and rating count, include it in the average
					if (!example.get(2).equals("null") && !example.get(3).equals("null")) {
						rating_average += Double.parseDouble(example.get(2));
						rating_count_average += Double.parseDouble(example.get(3));
						rating_count++;
					}
					
					// if we have a runtime, include it in the average
					if ( !example.get(example.size() - 1).equals("null") ) {
						runtime_average += Double.parseDouble(example.get(example.size() - 1));
						runtime_count++;
					}
					
					// what else do we need to average? are we doing anything with release_day/dayofweek/etc?
				}
			}
			
			rating_average /= rating_count;
			rating_count_average /= rating_count;
			runtime_average /= runtime_count;
			
			System.out.println(rating_average + "\n" + rating_count_average + "\n" + runtime_average);
			
			scanner.close();
			noisy_bw.close();
			clean_bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// go through and replace null values in noisy_data.txt with computed averages/modes
		
		/*

		//replace missing values
		file = new File("../noisy_data.txt");
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String str = scanner.nextLine();
				List<String> example = Arrays.asList(str.split("\t"));
				// 
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		*/
	}
}
