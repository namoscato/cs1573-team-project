import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class NominalToBinary {
	private static File input = new File("../../data-collection/datasets/clean/clean_data_5000.txt");
	private static File output = new File("../../data-collection/datasets/clean/clean_data_5000_binary.txt");
	
	private List<Set<String>> values;
	private static final int START = 7;
	private static final int END = 9;
	
	private static final String TRUE = "y";
	private static final String FALSE = "n";
	
	private String attributes = null;
	
	public NominalToBinary() {
		values = initialize();
		splitNominal();
		printConfig();
	}
	
	public NominalToBinary(String inFile, String outFile) {
		input = new File(inFile);
		output = new File(outFile);
		values = initialize();
		splitNominal();
		printConfig();
	}
	
	/*
	 * Add distinct feature values to set
	 */
	public List<Set<String>> initialize() {
		try {
			Scanner scanner = new Scanner(input);
		
			// initialize sets
			List<Set<String>> values = new ArrayList<Set<String>>();
			for (int i = START; i <= END; i++) {
				values.add(new TreeSet<String>());
			}
			
			// figure out our distinct feature values
			while (scanner.hasNextLine()) {
				List<String> example = new ArrayList<String>(Arrays.asList(scanner.nextLine().split("\t")));
				for (int i = START; i <= END; i++) {
					String[] temp = example.get(i).split(",");
					for (String key : temp) {
						values.get(i - START).add(key);
					}
				}
			}
			scanner.close();
			
			return values;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Split old features into set of new binary features
	 */
	public void splitNominal() {
		try {
			
			output.createNewFile();
			FileWriter fw = new FileWriter(output.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			Scanner scanner = new Scanner(input);
			
			while (scanner.hasNextLine()) {
				List<String> example = new ArrayList<String>(Arrays.asList(scanner.nextLine().split("\t")));
				
				// compute actor, director and writer score
				/*example.set(4, "ACTOR_SCORE");
				example.set(5, "DIRECTOR_SCORE");
				example.set(6, "WRITER_SCORE");*/
				
				// split up remaining features
				int currentStart = START;
				for (int i = START; i <= END; i++) {
					// remove list of nominal values so we can add set of new binary features
					String[] myValues = example.remove(currentStart).split(","); 
					
					// for each possible feature value
					Set<String> featureValues = values.get(i - START);
					for (String value : featureValues) {
						if (Arrays.binarySearch(myValues, value) < 0) {
							// example does not have this value
							example.add(currentStart, FALSE);
						} else {
							// example does have this value
							example.add(currentStart, TRUE);
						}
						currentStart++;
					}
				}
				
				// remove id and imdb id
				example.remove(0);
				example.remove(0);
				
				// remove rating count
				example.remove(1);
				
				// remove actor, writer, director
				example.remove(1);
				example.remove(1);
				example.remove(1);
				
				// write new example to output file
				bw.write(createLine(example));
			}
			scanner.close();
			bw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getAttributes(){
		return attributes;
	}
	public static String formatFeatureName(String name) {
		String result = "is";
		String[] parts = name.split("\\s");
		for (String part : parts) {
			part = part.trim();
			if (!part.isEmpty()) {
				result += Character.toUpperCase(part.charAt(0)) + part.substring(1);	
			}
		}
		return result;
	}
	
	public void printConfig() {
		StringBuffer sb = new StringBuffer();
		// first couple of features in data output are numerical...
		// I don't know if we need to print config for them
		
		// print the names of features we just split up
		for (Set<String> feature : values) {
			for (String value : feature) {
				String formatted = formatFeatureName(value);
				System.out.print(formatted + ",");
				sb.append(formatted+",");
			}
			System.out.println();
		}
		attributes = sb.toString();
		// print config stuff for last features?
	}
	
	public static String createLine(List<String> list) {
		String result = "";
		for (int i = 0; i < list.size(); i++) {
			result += list.get(i);
			if (i < list.size() - 1) {
				result += ",";
			}
		}
		return result + "\n";
	}
	
	public static void main(String[] args) {
		NominalToBinary test = new NominalToBinary();
	}
}
