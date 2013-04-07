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
			while (scanner.hasNextLine()) {
				List<String> example = Arrays.asList(scanner.nextLine().split("\t"));
				System.out.println(example.get(2) + "," + example.get(14));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
