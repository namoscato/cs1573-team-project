import java.io.*;
public class ParseFile {
	public static void main (String [] args) {
	 	//read in the text file
		FileReader fr = new FileReader("movie_data.txt");
		BufferedReader buf = new BufferedReader(fr);
		String line = null;

		while ((line = buf.ReadLine()) != null) {
			String[] arr = line.split("\t");
			
		}
	}

	public int dayOfWeek (int d, int m, int y) {
		int c = getCentury(y);
		y = y % 100;
		return (d + m + y + (float)y/4 + c) % 7;
	}

	public int getCentury (int year) {
		int c = year / 100;
		if (c % 4 == 0) return 6;
		else if (c % 4 == 1) return 4;
		else if (c % 4 == 2) return 2;
		else return 0;
	}
}