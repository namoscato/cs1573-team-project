import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/*
 * This class will produce 20 files (10 training, 10 testing)
 * it takes the input file as formated in clean_data.txt
 * the output files are to be used by GenerateScore.java
 */
public class SplitFile {
	int numFold = 10;
	
	public SplitFile(String filename){
		try{
			splitFile(filename);
		}catch(Exception e){
			System.err.println(e.toString());
		}
	}
	
	public  void splitFile(String filename) throws Exception{
		for(int l=0; l<numFold; l++){
			Scanner sc = new Scanner(new File(filename));
			FileWriter fw1 = new FileWriter("train"+l+".txt");
			FileWriter fw2 = new FileWriter("test"+l+".txt");
			BufferedWriter bwTrain = new BufferedWriter(fw1);
			BufferedWriter bwTest = new BufferedWriter(fw2);
			String line;
			int j = 0;
			while(sc.hasNextLine()){
				line = sc.nextLine();
				String split[] = line.split("\t");
				if(j == 10) j = 0;
				if(j == l){
					for(int k=3;k<split.length;k++){
						bwTest.write(split[k]+"\t");
					}
					bwTest.write(split[2]+"\n");
				}
				else{
					for(int k=3;k<split.length;k++){
						bwTrain.write(split[k]+"\t");
					}
					bwTrain.write(split[2]+"\n");
				}
				j++;
			}
			sc.close();
			bwTrain.close();
			bwTest.close();
		}
	}
	
}
