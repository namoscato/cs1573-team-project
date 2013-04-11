import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CountOccurrences {
	private static final String[] FEATURES = {"actor", "director", "writer", "genre", "language", "country", "mpaa_rating", "release_year"};
	private static final int START_INDEX = 4;
	
	public static void main(String[] args) {
		File input = new File("../clean_data.txt");
		File output = new File("../../config/clean_config.txt");
		
		try {
			// create output file
			output.createNewFile();
			FileWriter fw = new FileWriter(output.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			// open input file
			Scanner scanner = new Scanner(input);
			
			// initialize nominal feature value sets
			List<Map<String, Integer>> values = new ArrayList<Map<String, Integer>>(FEATURES.length);
			for (int i = 0; i < FEATURES.length; i++) {
				values.add(new HashMap<String, Integer>());
			}
			
			// traverse through all examples
			while (scanner.hasNextLine()) {
				List<String> example = new ArrayList<String>(Arrays.asList(scanner.nextLine().split("\t")));
				// add actors, directors, writers, genres, languages, countries, mpaa_rating and release_year
				for (int i = 0; i < FEATURES.length; i++) {
					List<String> temp = Arrays.asList(example.get(i + START_INDEX).split(","));
					for (String key : temp) {
						if (values.get(i).containsKey(key)) {
							values.get(i).put(key, new Integer(values.get(i).get(key).intValue() + 1));
						} else {
							values.get(i).put(key, new Integer(1));
						}
					}
					
					//values.get(i).add();
				}
			}
			
			System.out.println("here we go");
			
			for (int i = 0; i < FEATURES.length; i++) {
				int size = values.get(i).size();
				System.out.println("(" + i + ") - " + size + " distinct attributes");
				Iterator it = values.get(i).entrySet().iterator();
				int count = 0;
				while (it.hasNext()) {
					Map.Entry pairs = (Map.Entry)it.next();
					if (((Integer) pairs.getValue()).intValue() > 1) {
						System.out.println(pairs.getKey() + " = " + pairs.getValue());
						count++;
					}
					it.remove();
				}
				System.out.println("*"+count+"/"+size+"*");
				System.out.println("\n");
			}
			
			// write formatted output to file
			/*
			for (int i = 0; i < FEATURES.length; i++) {
				bw.write(FEATURES[i] + "\t" + FixMistakes.formatCommaString(values.get(i)) + "\n");
			}
			bw.write("release_month\t1,2,3,4,5,6,7,8,9,10,11,12\n");
			bw.write("weekend\t0,1");
			*/
			
			scanner.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
