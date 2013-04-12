import java.text.DecimalFormat;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;

public class PostProcess {
	
	/*
	 * Returns the day of the week (Saturday
	 */
	public static int isWeekend(String s_d, String s_m, String s_y) {
		Calendar cal = new GregorianCalendar();
		cal.set(Integer.parseInt(s_y), Integer.parseInt(s_m) - 1, Integer.parseInt(s_d));
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1 || dayOfWeek == 6 || dayOfWeek == 7){
			return 1;
		} else {
			return 0;
		}
	}
	
	/*
	 * Splits month into two partitions
	 */
	public static int divideMonth(String s) {
		int d = Integer.parseInt(s);
		if (d < 16) return 0;
		else return 1;
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
	
	public static String formatArray(int[] array) {
		String result = "[";
		for (int i = 0; i < array.length; i++) {
			result += array[i];
			if (i < array.length - 1) {
				result += ", ";
			}
		}
		return result + "]";
	}
	
	public static int max(int[] array) {
		int max = Integer.MIN_VALUE;
		int result = -1;
		for (int i = 0; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
				result = i;
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		File input = new File("../movie_data_revision2.txt");
		
		// create two files, one for clean data and one for noisy data
		File clean_data = new File("../clean_data.txt");
 		File noisy_data = new File("../noisy_data.txt");
 		
 		// initialize some noisy data stuff
		double clean_rating_average = 0;
		int clean_rating_count = 0;
		double noisy_rating_average = 0;
		int noisy_rating_count = 0;
		int rating_count_average = 0;
		int rating_count = 0;
		int runtime_average = 0;
		int runtime_count = 0;
		int weekend_mode = -1;
		int[] weekend_count = new int[2];
		
		int clean_count = 0;
		int noisy_count = 0;
 		
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
				ArrayList<String> example = new ArrayList<String>(Arrays.asList(str.split("\t")));
				
				int missing = missingValues(str);
				
				// only keep examples with < 6 missing values (see FixNulls.java for distribution) and actors, directors, writers and year are not null
				if (missing < 6 && !example.get(4).equals("null") && !example.get(5).equals("null") && !example.get(6).equals("null") && !example.get(11).equals("null")) {
					// if release day is not null, compute whether or not it is weekend
					if (!example.get(13).equals("null")) {
						if (example.get(12).equals("null")) {
							example.set( 13, "null");
						} else {
							// can only compute weekend if month exists
							int temp = isWeekend(example.get(13), example.get(12), example.get(11));
							example.set( 13, Integer.toString(temp));
							weekend_count[temp]++;	
						}
					}
					
					// replace odd ratings with normal ratings
					String rating = example.get(10);
					if (rating.length() > 3 && rating.substring(0, 2).equals("TV")) {
						if (rating.equals("TV_MA")) {
							example.set(10, "R");
						} else if (rating.equals("TV_14")) {
							example.set(10, "PG_13");
						} else if (rating.equals("TV_PG")) {
							example.set(10, "PG");
						} else if (rating.equals("TV_G")) {
							example.set(10, "G");
						}
					} else if (rating.equals("NC_17")) {
						example.set(10, "X");
					}
					
					// if we have a rating count, include it in the average
					if (!example.get(3).equals("null")) {
						rating_count_average += Double.parseDouble(example.get(3));
						rating_count++;
					}
					
					// if we have a runtime, include it in the average
					if ( !example.get(example.size() - 1).equals("null") ) {
						runtime_average += Double.parseDouble(example.get(example.size() - 1));
						runtime_count++;
					}
					
					// always write to noisy as is (with nulls)
					noisy_bw.write(FixMistakes.createLine(example));
					noisy_count++;
					if (!example.get(2).equals("null")) {
						noisy_rating_average += Double.parseDouble(example.get(2));
						noisy_rating_count++;
					}
					
					// only write to clean if there are no missing values
					if (missing == 0) {
						clean_bw.write(FixMistakes.createLine(example));
						clean_count++;
						if (!example.get(2).equals("null")) {
							clean_rating_average += Double.parseDouble(example.get(2));
							clean_rating_count++;
						}
					}
				}
			}
			
			noisy_rating_average /= noisy_rating_count;
			clean_rating_average /= clean_rating_count;
			rating_count_average = (int)Math.round( rating_count_average * 1.0 / rating_count );
			runtime_average = (int)Math.round( runtime_average * 1.0 / runtime_count );
			weekend_mode = max(weekend_count);
			
			System.out.println("noisy_rating_average: " + clean_rating_average);
			System.out.println("clean_rating_average: " + clean_rating_average);
			System.out.println("rating_count_average: " + rating_count_average);
			System.out.println("runtime_average: " + runtime_average);
			System.out.println("weekend_mode: " + weekend_mode + "\t" + formatArray(weekend_count));
			
			System.out.println("clean = " + clean_count);
			System.out.println("noisy = " + noisy_count);
			
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
				
				for (int i = 0; i < example.size(); i++) {
					if (example.get(i).equals("null")) {
						switch (i) {
							case 14: // runtime
								example.set(i, Integer.toString(runtime_average));
								break;
							case 13: // weekend
								example.set(i, Integer.toString(weekend_mode));
								break;
							case 12: // release month
								example.set(i, "10");
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
								example.set(i, fmt.format(noisy_rating_average));
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
