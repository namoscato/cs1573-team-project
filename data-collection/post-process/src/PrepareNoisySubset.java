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


public class PrepareNoisySubset {
	private static final int SUBSET_SIZE = 5000;
	
	public static void randomize(List<String> list) {
		for (int i = list.size() - 1; i > 0; i--) {
			int rand = (int)( Math.random() * i );
			String temp = list.get(i);
			list.set(i, list.get(rand));
			list.set(rand, temp);
		}
	}
	
	public static void main(String[] args) {
		try {
			// open our clean dataset
			File input = new File("../datasets/clean/clean_data.txt");
			Scanner scanner = new Scanner(input);
			
			// and bring all of the IDs into memory
			List<String> cleanSet = new ArrayList<String>();
			while (scanner.hasNext()) {
				String[] example = scanner.nextLine().split("\t");
				cleanSet.add(example[1]);
			}
			scanner.close();
			
			// open our noisy dataset
			input = new File("../datasets/noisy/noisy_data_revision1.txt");
			scanner = new Scanner(input);
			
			// and bring these examples into memory
			List<String> noisySet = new ArrayList<String>();
			while (scanner.hasNext()) {
				noisySet.add(scanner.nextLine());
			}
			
			// shuffle these examples
			randomize(noisySet);
			
			// output our training set
			int pointer = 0;
			File output = new File("../datasets/noisy/noisy_data_train.txt");
			output.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(output));
			do {
				bw.write(noisySet.get(pointer) + "\n");
				pointer++;
			} while (pointer < SUBSET_SIZE - (SUBSET_SIZE / 10));
			bw.close();
			
			// output our test set
			output = new File("../datasets/noisy/noisy_data_test.txt");
			output.createNewFile();
			bw = new BufferedWriter(new FileWriter(output));
			for (int i = pointer; i < noisySet.size(); i++) {
				String str = noisySet.get(i);
				String[] temp = str.split("\t");
				if (cleanSet.contains(temp[1])) {
					// only add clean examples to the test set
					bw.write(str + "\n");
					pointer++;
					if (pointer >= SUBSET_SIZE) {
						break;
					}
				}
			}
			bw.close();
			
			System.out.println(pointer);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
