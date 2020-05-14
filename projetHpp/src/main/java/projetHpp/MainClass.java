package projetHpp;

import java.io.File;
import java.io.FileNotFoundException;

import parsing.DataParsing;

public class MainClass {
	
	
	public static void main(String[] args) { 
		
		DataParsing parser = new DataParsing();
		
		File myDirectory = new File("/Users/macpro/Downloads/data_hpp/20");
		//File[] containingFiles = myDirectory.listFiles();
		String[] containingFilesNames = myDirectory.list();
		
		for (String fileName : containingFilesNames) {
			  if (fileName.matches("(France|Italy|Spain)+.csv")) {
				   try {

					parser.fetchCsvFileData(myDirectory,fileName);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   }


		}
	}


}
