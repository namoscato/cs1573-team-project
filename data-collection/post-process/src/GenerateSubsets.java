import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;


public class GenerateSubsets {
	private static File input = new File("../clean_data.txt");
	private static File output = new File("../subsets/test_subset.txt");
	
	public static void main(String[] args) {
		try {
			output.createNewFile();
			FileWriter fw = new FileWriter(output.getAbsoluteFile());
			Scanner scanner = new Scanner(input);
			
			if (true) {
				List<Set<String>> distinctPeople = new ArrayList<Set<String>>();
				for (int i = 0; i < 3; i++) {
					distinctPeople.add(new HashSet<String>());
				}
				
				int count = 0;
				
				while (scanner.hasNextLine()) {
					List<String> example = new ArrayList<String>(Arrays.asList(scanner.nextLine().split("\t")));
					int temp = Integer.parseInt(example.get(11)) / 10 * 10;
					//List<String> values = Arrays.asList(example.get(9).split(","));
					int runtime = Integer.parseInt(example.get(14));
					//if (values.contains("UK") && runtime < 146 && runtime > 63) {
					if (temp == 2000 && runtime < 146 && runtime > 63) {
						System.out.println(example.get(2));
						for (int i = 4; i <= 6; i++) {
							distinctPeople.get(i - 4).addAll(Arrays.asList(example.get(i).split(",")));		
						}
						count++;
					}
				}
				
				for (int i = 0; i < distinctPeople.size(); i++) {
					System.out.print(distinctPeople.get(i).size() + "\t");
				}
				
				System.out.println("\n" + count);
			} else {
					
				// output distribution
				Map<String, Integer> map = new HashMap<String, Integer>();
				
				// traverse through all examples
				while (scanner.hasNextLine()) {
					List<String> example = new ArrayList<String>(Arrays.asList(scanner.nextLine().split("\t")));
					//int temp = Integer.parseInt(example.get(11)) / 10 * 10;
					String values[] = example.get(14).split(",");
					System.out.println(example.get(14));
					for (String value: values) {
						if (map.containsKey(value)) {
							map.put(value, map.get(value) + 1);
						} else {
							map.put(value, 1);
						}
					}
				}
	
				scanner.close();
				
				// print out results
				Iterator<Entry<String, Integer>> it = map.entrySet().iterator();
				//int count = 0;
				while (it.hasNext()) {
					Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>)it.next();
					//System.out.println(pairs.getKey() + "\t" + pairs.getValue());
					it.remove();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
