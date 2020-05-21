package projetHpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import parsing.DataParsing;
import parsing.DataParsingThread;

public class MainClass {
	
	
	public static void main(String[] args) throws InterruptedException { 
		
		DataParsing parser = new DataParsing();
		
		// Get properties file informations
		List<String> properties = parser.getMainPath("properties/path.properties");
		String	path = properties.get(0);
		String SlashOrTwoBackSlash = properties.get(1);
		
		File myDirectory = new File(path);
		
		String[] containingFilesNames = myDirectory.list();
		/*Thread t1 = new Thread(new DataParsingThread(myDirectory, "Italy.csv", SlashOrTwoBackSlash));
		Thread t2 = new Thread(new DataParsingThread(myDirectory, "Spain.csv", SlashOrTwoBackSlash));
		Thread t3 = new Thread(new DataParsingThread(myDirectory, "France.csv", SlashOrTwoBackSlash));
		long start = System.nanoTime();//we start counting the time

		t1.start();
		t2.start();
		t3.start();
		t1.join();
		t2.join();
		t3.join();
		long end = System.nanoTime();// we stop counting the time
		long elapsedTime= end-start; // time elapsed 
		System.out.println("Elapsed time is: "+Math.abs(elapsedTime));*/
		
		long start = System.nanoTime();//we start counting the time

		
		for (String fileName : containingFilesNames) {
			  if (fileName.matches("Italy1.csv")) {
						try {
								parser.fetchCsvFileData( myDirectory, fileName, SlashOrTwoBackSlash );

							
							
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

			   }


		}
		long end = System.nanoTime();// we stop counting the time
		long elapsedTime= end-start; // time elapsed 
		System.out.println("Elapsed time is: "+Math.abs(elapsedTime));
		
	}

}
