import java.text.DecimalFormat;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
 		
 		// initialize some noisy data stuff
		double rating_average = 0;
		int rating_count_average = 0;
		int rating_count = 0;
		int runtime_average = 0;
		int runtime_count = 0;
 		
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
			
			while (scanner.hasNextLine()) {
				String str = scanner.nextLine();
				List<String> example = new ArrayList<String>(Arrays.asList(str.split("\t")));
				
				int missing = missingValues(str);
				
				// only keep examples with < 6 missing values (see FixNulls.java for distribution) and actors, directors and writers are not null
				if (missing < 6 && !example.get(4).equals("null") && !example.get(5).equals("null") && !example.get(6).equals("null")) {
					// replace release_day with two new features values
					if (example.get(13).equals("null")) {
						// if release day is null, just add one new null value
						example.add(example.size() - 1, "null");
					} else {
						// otherwise, compute position in month and day of week 
						example.set( 13, Integer.toString(divideMonth(example.get(13))) );
						example.add( 14, Integer.toString(dayOfWeek(example.get(13), example.get(12), example.get(11))) );
						// INCREMENT AN AVERAGE & COUNT FOR BOTH OF THESE?
					}
					
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
					
					// always write to noisy as is (with nulls)
					noisy_bw.write(FixMistakes.createLine(example));
					// only write to clean if there are no missing values
					if (missing == 0) {
						clean_bw.write(FixMistakes.createLine(example));
					}
				}
			}
			
			rating_average /= rating_count;
			rating_count_average = (int)Math.round( rating_count_average * 1.0 / rating_count );
			runtime_average = (int)Math.round( runtime_average * 1.0 / runtime_count );
			
			System.out.println("rating_average: " + rating_average);
			System.out.println("rating_count_average: " + rating_count_average);
			System.out.println("runtime_average: " + runtime_average);
			
			scanner.close();
			noisy_bw.close();
			clean_bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// go through and replace null values in noisy_data.txt with computed averages/modes
		File revised_data = new File("../noisy_data_revision1.txt");
		
		try {
			revised_data.createNewFile();
			FileWriter revised_fw = new FileWriter(revised_data.getAbsoluteFile());
			BufferedWriter revised_bw = new BufferedWriter(revised_fw);
			
			Scanner scanner = new Scanner(noisy_data);
			
			DecimalFormat fmt = new DecimalFormat("0.0");
			
			while (scanner.hasNextLine()) {
				String str = scanner.nextLine();
				List<String> example = Arrays.asList(str.split("\t"));
<<<<<<< HEAD
				if (misfits.containsKey(example.get(1))) {
					// there are missing feature values 
					for (String feature : misfits.get(example.get(1))) {
						switch (feature) {
							case "release_month":
							
							case "rating":
								// do stuff
=======
				for (int i = 0; i < example.size(); i++) {
					if (example.get(i).equals("null")) {
						switch (i) {
							case 15: // runtime
								example.set(i, Integer.toString(runtime_average));
								break;
							case 14: // day of week
								// what are we doing with day of week?
								break;
							case 13: // month partition
								// do something
								break;
							case 12: // release month
								example.set(i, "10");
								break;
							case 11: // release year
								// do something
								break;
							case 10: // MPAA rating
								example.set(i, "unrated");
								break;
							case 9: // country
								example.set(i, "USA");
								break;
							case 8: // language
								example.set(i, "English");
								break;
							case 7: // genres
								example.set(i, "Drama");
								break;
							case 3: // rating count
								example.set(i, Integer.toString(rating_count_average));
								break;
							case 2: // rating
								example.set(i, fmt.format(rating_average));
>>>>>>> 9082871badf667f868ec9fb82c4206532925298f
								break;
							default:
								break;
						}
					}
				}
				revised_bw.write(FixMistakes.createLine(example));
			}
			scanner.close();
			revised_bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
