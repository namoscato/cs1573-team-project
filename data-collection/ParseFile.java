import java.io.*;
public class ParseFile {
	public static void main (String [] args) {
	 	//read in the text file
		FileReader fr = new FileReader("movie_data.txt");
		BufferedReader buf = new BufferedReader(fr);
		String line = null;

		while ((line = buf.ReadLine()) != null) {
			String[] arr = line.split("\t");
			//produce two text files.  one is clean examples only, the other one modifies the files.
			switch (arr[1]) {
				case release_day: {

				} break;
				case language: {

				} break;
			}
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

	public int divideMonth(int d) {
		if (d < 11) return 0;
		else if (d < 21) return 1;
		else return 2;
	}

	public void 
}