import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Random {
	public static void main(String args[]) {
		try {
			File input = new File("../clean_data.txt");
			Scanner scanner = new Scanner(input);
			int count = 0;
			while (scanner.hasNextLine()) {
				String str = scanner.nextLine();
				List<String> example = Arrays.asList(str.split("\t"));
				if (example.get(10).equals("unrated")) {
					//System.out.println(example.get(2) + "," + example.get(14));
					count++;
					System.out.println(str);
				}
			}
			System.out.println(count);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
