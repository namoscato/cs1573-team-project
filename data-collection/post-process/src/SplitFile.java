import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/*
 * This class will produce 20 files (10 training, 10 testing)
 * it takes the input file as formated in clean_data.txt
 * the output files are to be used by GenerateScore.java

original:
[0] number
[1] movie id
[2] rating
[3] vote count
[4] actor
[5] director
[6] writer
[7] genre
[8] language
[9] country
[10] MPAA
[11] year
[12] month
[13] weekend
[14] runtime

after:
[0] vote count
[1] actor
[2] director
[3] writer
[4] genre
[5] language
[6] country
[7] MPAA
[8] year
[9] month
[10] weekend
[11] runtime
[12] rating

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
			FileWriter full = new FileWriter("full.csv");//this csv is only to be used to generate arff header in weka - to be appended in all other arff
			BufferedWriter bwTrain = new BufferedWriter(fw1);
			BufferedWriter bwTest = new BufferedWriter(fw2);
			BufferedWriter bwFull = new BufferedWriter(full);
			bwFull.write("// append a header to this csv and then convert this csv to arff in weka - add the header to other files\n");
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
				bwFull.write(split[3]+","+"0.0"+","+"0.0"+","+"0.0"+",");
				for(int k=7;k<split.length;k++){
					bwFull.write(split[k]+",");
				}
				bwFull.write(split[2]+"\n");
				j++;
			}
			sc.close();
			bwTrain.close();
			bwTest.close();
		}
	}
	
}
