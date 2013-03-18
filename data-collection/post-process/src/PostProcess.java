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
	
	public static void main(String[] args) {
		// populate misfits data structure
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
		
		file = new File("../movie_data.txt");
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				List<String> example = Arrays.asList(scanner.nextLine().split("\t"));
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
