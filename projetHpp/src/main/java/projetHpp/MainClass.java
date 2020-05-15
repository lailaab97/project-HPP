package projetHpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import parsing.DataParsing;

public class MainClass {
	
	
	public static void main(String[] args) { 
		
		DataParsing parser = new DataParsing();
		
		// Get properties file informations
		List<String> properties = parser.getMainPath("properties/path.properties");
		String	path = properties.get(0);
		String SlashOrTwoBackSlash = properties.get(1);
		
		File myDirectory = new File(path);
		
		String[] containingFilesNames = myDirectory.list();

		
		
		for (String fileName : containingFilesNames) {
			  if (fileName.matches("(Italy|Spain|France).csv")) {

						try {
							parser.fetchCsvFileData( myDirectory, fileName, SlashOrTwoBackSlash );
							
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

			   }


		}
	}

}