import java.io.*;
import java.util.Random;
import java.util.*;


public class GenerateCSV {
	
	public static void main(String args[]) throws Exception{
		NominalToBinary nb = new NominalToBinary("clean_data.txt","cleanBinary.txt");
		SplitFile sf = new SplitFile(nb.getAttributes(),"cleanBinary.txt");
		//put this into 10 loops later
		GenerateScore2 gs = new GenerateScore2("train0.txt", "test0.txt");
		gs.CreateCSV("train0.csv", "test0.csv");
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
