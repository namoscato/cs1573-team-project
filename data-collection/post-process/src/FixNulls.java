import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class FixNulls {
	
	public static void main(String[] args) {		
		File input = new File("../movie_data_revision1.txt");
		File output = new File("../movie_data_revision2.txt");
		
		try {
			output.createNewFile();
			FileWriter fw;
			BufferedWriter bw;
			try {
				fw = new FileWriter(output.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				Scanner scanner = new Scanner(input);
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					List<String> values = new ArrayList<String>(Arrays.asList(line.split("\t")));

					if (values.size() < 15) {
						values.add(values.size() - 2, "null");
						values.add(values.size() - 2, "null");
						bw.write(FixMistakes.createLine(values));
					} else {
						bw.write(line + "\n");
					}
				}
				bw.flush();
				bw.close();
				scanner.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
