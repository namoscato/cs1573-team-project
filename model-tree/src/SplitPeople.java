import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class SplitPeople {
	
	
	public static void main(String[] args) throws IOException {
		Scanner scanner = Parse.openFile("input/noisy_data.txt");
		List<String> examples = new ArrayList<String>();
		while (scanner.hasNextLine()) {
			examples.add(scanner.nextLine());
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("input/noisy_data_split_people.txt").getAbsoluteFile()));
		int count = 0;
		for (String example : examples) {
			List<String> values = new ArrayList<String>(Arrays.asList(example.split("\t")));
			
			// split actors
			String[] temp = values.get(4).split(",");
			if (temp.length >= 3) {
				values.set(4, temp[0]);
				values.add(5, temp[1]);
				values.add(6, temp[2]);
			} else {
				count++;
				continue;
			}
			
			// write updated example to file
			bw.write(Parse.createLine(values));
		}
		bw.close();
		
		System.out.println(count + " examples were scrapped");
	}
}
