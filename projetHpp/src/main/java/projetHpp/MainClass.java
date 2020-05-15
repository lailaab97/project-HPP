package projetHpp;

import java.io.File;
import java.io.FileNotFoundException;

import parsing.DataParsing;

public class MainClass {
	
	
	public static void main(String[] args) { 
		
		DataParsing parser = new DataParsing();
		String path = parser.getMainPath("properties/path.properties");

		File myDirectory = new File(path);
		String[] containingFilesNames = myDirectory.list();
		
		for (String fileName : containingFilesNames) {
			  if (fileName.matches("(Italy|Spain|France).csv")) {
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
