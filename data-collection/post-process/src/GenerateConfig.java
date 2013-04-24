import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class GenerateConfig {
	private static final String[] FEATURES = {"actor", "director", "writer", "genre", "language", "country", "mpaa_rating", "release_year"};
	private static final int START_INDEX = 4;

	public static void main(String[] args) {
		File input = new File("../datasets/clean/clean_data_5000.txt");
		File output = new File("../datasets/clean/clean_config_5000.txt");

		try {
			// create output file
			output.createNewFile();
			FileWriter fw = new FileWriter(output.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			// open input file
			Scanner scanner = new Scanner(input);

			// initialize nominal feature value sets
			List<Set<String>> values = new ArrayList<Set<String>>(FEATURES.length);
			for (int i = 0; i < FEATURES.length; i++) {
				values.add(new HashSet<String>());
			}

			// traverse through all examples
			while (scanner.hasNextLine()) {
				List<String> example = new ArrayList<String>(Arrays.asList(scanner.nextLine().split("\t")));
				// add actors, directors, writers, genres, languages, countries, mpaa_rating and release_year
				for (int i = 0; i < FEATURES.length; i++) {
					values.get(i).addAll(Arrays.asList(example.get(i + START_INDEX).split(",")));
				}
			}

			// write formatted output to file
			for (int i = 0; i < FEATURES.length; i++) {
				System.out.println(FEATURES[i] + "\t" + values.get(i).size());
				bw.write(FEATURES[i] + "\t" + FixMistakes.formatCommaString(values.get(i)) + "\n");
			}
			bw.write("release_month\t01,02,03,04,05,06,07,08,09,10,11,12\n");
			bw.write("weekend\t0,1");

			scanner.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}