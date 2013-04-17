import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Random {
	public static void main(String args[]) {
		try {
			File input = new File("../subsets/noisy_data.txt");
			Scanner scanner = new Scanner(input);
			double average_rating = 0;
			int count = 0;
			while (scanner.hasNextLine()) {
				String str = scanner.nextLine();
				List<String> example = Arrays.asList(str.split("\t"));
				average_rating += Double.parseDouble(example.get(2));
				count++;
			}
			System.out.println(average_rating / count);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
