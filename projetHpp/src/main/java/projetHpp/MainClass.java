package projetHpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import parsing.DataParsing;
import structure.Tree;

public class MainClass {
	
	
	public static void main(String[] args) { 
		
		DataParsing parser = new DataParsing();
		List<Tree> mainListOfResultsFrance = null;
		
		File myDirectory = new File("C:\\Users\\abdel\\OneDrive\\Bureau\\TSE\\Semestre 8\\HPP\\PROJET HPP\\data (2)\\20");
		//File[] containingFiles = myDirectory.listFiles();
		String[] containingFilesNames = myDirectory.list();
		
		for (String fileName : containingFilesNames) {
			  if (fileName.matches("Italy.csv")) {
				   try {

					parser.fetchCsvFileData(myDirectory,fileName,mainListOfResultsFrance);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   }


		}
	}

}
