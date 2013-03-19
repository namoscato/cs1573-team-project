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


public class FixMistakes {
	// actors and writers and directors
	public static String removeSubstring(String s, String substring) {
		List<String> people = new ArrayList<String>(Arrays.asList(s.split(",")));
		for (int i = people.size() - 1; i >= 0; i--) {
			if (people.get(i).matches("(?i).*" + substring + ".*")) {
				people.remove(i);
				break;
			}
		}
		return formatCommaString(people);
	}
	
	public static String formatCommaString(List<String> list) {
		if (list.size() == 0) {
			return "null";
		} else {
			String result = "";
			for (int i = 0; i < list.size(); i++) {
				result += list.get(i);
				if (i < list.size() - 1) {
					result += ",";
				}
			}
			return result;	
		}
	}
	
	public static String removeDuplicates(String s) {
		String[] array = s.split(",");
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < array.length; i++) {
			set.add(array[i]);
		}
		return formatCommaString(new ArrayList<String>(set));
	}
	
	public static String createLine(List<String> list) {
		String result = "";
		for (int i = 0; i < list.size(); i++) {
			result += list.get(i);
			if (i < list.size() - 1) {
				result += "\t";
			}
		}
		return result + "\n";
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
		
		File input = new File("../movie_data.txt");
		File output = new File("../movie_data_fixed.txt");
		
		try {
			output.createNewFile();
			FileWriter fw;
			BufferedWriter bw;
			try {
				fw = new FileWriter(output.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				Scanner scanner = new Scanner(input);
				while (scanner.hasNextLine()) {
					List<String> values = Arrays.asList(scanner.nextLine().split("\t"));

					// remove "# more credit" from actors, directors and writers
					values.set(4, removeSubstring(values.get(4), "more credit")); // actors
					values.set(5, removeSubstring(values.get(5), "more credit")); // directors
					values.set(6, removeSubstring(values.get(6), "more credit")); // writers
					
					// set "None" languages to null
					if (values.get(8).equals("None")) {
						values.set(8, "null");
					}
					
					// set "See more" writers to null
					if (values.get(6).equals("See more")) {
						values.set(6, "null");
					}
					
					// remove duplicate directors and writers
					values.set(5, removeDuplicates(values.get(5)));
					values.set(6, removeDuplicates(values.get(6)));
					
					bw.write(createLine(values));
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
