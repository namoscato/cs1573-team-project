import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Parse {
	/*
	 * Opens a file with Scanner
	 * @param file filename of file
	 * @return Scanner object or null if file doesn't exist
	 */
	private static Scanner openFile(String file) {
		try {
			Scanner scanner = new Scanner(new File(file));
			return scanner;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Creates a formatted Data object from a raw string in a data file.
	 * @param raw unformatted string
	 * @param id unique identifier
	 * @param idIndex index of unique identifier
	 * @param outputIndex index of actual output
	 * @param discreteFeatures list of discrete features
	 * @param continuousFeatures list of continuous features
	 * @return formatted Data object
	 */
	private static Data createData(String raw, int id, int idIndex, int outputIndex, List<Feature> discreteFeatures, List<Feature> continuousFeatures) throws ParseException {
		String[] example = raw.split("\t");
		
		// find our discrete feature values
		List<List<String>> discreteValues = new ArrayList<List<String>>();
		for (Feature feature : discreteFeatures) {
			discreteValues.add( feature.getID(), Arrays.asList(example[feature.getOldID()].split(",")) );
		}
		
		// find our continuous feature values
		List<Number> continuousValues = new ArrayList<Number>();
		for (Feature feature : continuousFeatures) {
			continuousValues.add( feature.getID(), NumberFormat.getInstance().parse(example[feature.getOldID()]) );
		}
		
		return new Data(id, example[idIndex], Float.parseFloat(example[outputIndex]), discreteValues, continuousValues);
	}
	
	/*
	 * Parses a file and creates a list of Data objects from each line.
	 * @param file filename of file
	 * @param idIndex index of unique identifier
	 * @param outputIndex index of actual output
	 * @param discreteFeatures list of discrete features
	 * @param continuousFeatures list of continuous features
	 * @return formatted list of Data objects
	 */
	public static List<Data> parseDataFile(String file, int idIndex, int outputIndex, List<Feature> discreteFeatures, List<Feature> continuousFeatures) throws ParseException {
		Scanner scanner = openFile(file);
		List<Data> examples = new ArrayList<Data>();
		int count = 0;
		while (scanner.hasNextLine()) {
			examples.add( createData(scanner.nextLine(), count, idIndex, outputIndex, discreteFeatures, continuousFeatures) );
			count++;
		}
		return examples;
	}
	
	/*
	 * Setup project configuration.
	 * @param config text file that includes list of discrete and continuous features
	 * @param values text file that includes list of discrete feature values
	 */
	public static Configuration parseConfigFile(String config, String values) {
		// bring discrete values into memory
		Scanner scanner = openFile(values);
		Map<String, String> rawValues = new HashMap<String, String>();
		while (scanner.hasNextLine()) {
			String[] temp = scanner.nextLine().split("\t");
			rawValues.put(temp[0], temp[1]);
		}
		scanner.close();
		
		// add the features defined in the config file
		scanner = openFile(config);
		
		// get discrete features
		String[] line = scanner.nextLine().split("\t");
		List<Feature> discrete = new ArrayList<Feature>(line.length);
		int count = 0; // feature values will be re-assigned an id starting from 0
		for (String feature : line) {
			String[] temp = feature.split(","); // split id from name
			discrete.add(new Feature( count, Integer.parseInt(temp[0]), temp[1], Arrays.asList(rawValues.get(temp[1]).split(",")) ));
			count++;
		}
		
		line = scanner.nextLine().split("\t");
		List<Feature> continuous = new ArrayList<Feature>(line.length);
		count = 0;
		for (String feature : line) {
			String[] temp = feature.split(","); // split id from name
			continuous.add(new Feature( count, Integer.parseInt(temp[0]), temp[1] ));
			count++;
		}
		
		return new Configuration(discrete, continuous);
	}
}
