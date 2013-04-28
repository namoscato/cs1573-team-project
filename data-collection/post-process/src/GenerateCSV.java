import java.io.*;
import java.util.Random;
import java.util.*;


public class GenerateCSV {
	
	public static void main(String args[]) throws Exception{

		//put this into 10 loops later
		if (args.length < 1) { 
			System.out.println("Usage:  java GenerateCSV <input>");
			System.exit(0);
		}
		String input = args[0] + ".txt";
		String input_binary = args[0] + "binary.txt";
		NominalToBinary nb = new NominalToBinary(input, input_binary);
		//SplitFile sf = new SplitFile(nb.getAttributes(), input_binary);
		GenerateScore2 gs = new GenerateScore2(input, nb.getAttributes());
		String input_csv = args[0] + ".csv";
		gs.CreateCSV(input_csv);
	}
	
/*	public static void randomChooseThousand(){
		Scanner sc = null;
		FileWriter fw = null;
		try {
			sc = new Scanner(new File("clean.txt"));
			fw = new FileWriter("randomThousand.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		int num[] = new int[1000];
		ArrayList<Integer> array = new ArrayList<Integer>(1000);
		
		while(array.size()<1000){
			Random r = new Random();
			int n = r.nextInt(11889);
			if(array.contains(n)){
				continue;
			}
			else{
				array.add(n);
			}
		}
		int count = 0;
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			if(array.contains(count)){
				try {
					bw.write(line+"\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			count++;
		}
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	
}
