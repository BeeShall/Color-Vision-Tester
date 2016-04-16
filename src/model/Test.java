package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PrintWriter os;
		try {
			os = new PrintWriter(new File("test"));
			for(int i = 0; i<24; i++){
				for(int j =0; j<24; j++){
					os.print("0 ");
				}
				os.println();
			}
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		

	}

}
