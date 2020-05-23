package projetHpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import beans.Person;
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

		Map<Person,Integer> france = new HashMap<Person,Integer>();
		Map<Person,Integer> spain = new HashMap<Person,Integer>();
		Map<Person,Integer> italy = new HashMap<Person,Integer>();
		
		int lasContaminationDateFrance = 0 ;
		int lasContaminationDateSpain = 0 ;
		int lasContaminationDateItaly = 0 ;

		
		for (String fileName : containingFilesNames) {
			if (fileName.matches("France.csv")) {
			//  if (fileName.matches("Italy.csv")) {
						try {
								france = parser.fetchCsvFileData( myDirectory, fileName, SlashOrTwoBackSlash );
								lasContaminationDateFrance = parser.getLast(france).getKey().getDiagnosed_ts();
								System.out.println(lasContaminationDateFrance);
							
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

			   }
			if (fileName.matches("Italy.csv")) {
			//  if (fileName.matches("Italy.csv")) {
						try {
								italy = parser.fetchCsvFileData( myDirectory, fileName, SlashOrTwoBackSlash );
								lasContaminationDateItaly = parser.getLast(italy).getKey().getDiagnosed_ts();
								System.out.println(lasContaminationDateItaly);



							
							
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

			   }
			if (fileName.matches("Spain.csv")) {
			//  if (fileName.matches("Italy.csv")) {
						try {
								spain = parser.fetchCsvFileData( myDirectory, fileName, SlashOrTwoBackSlash );
								lasContaminationDateSpain = parser.getLast(spain).getKey().getDiagnosed_ts();
								System.out.println(lasContaminationDateSpain);


							
							
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

			   }


		}
		long end = System.nanoTime();// we stop counting the time
		long elapsedTime= end-start; // time elapsed 
		System.out.println("Elapsed time is: "+Math.abs(elapsedTime));
		
		Map<Person,Integer> result = new LinkedHashMap<Person,Integer>();
		result = parser.generateFinalResult(spain, lasContaminationDateSpain, france, lasContaminationDateFrance, italy, lasContaminationDateItaly);
		
		System.out.println("Top 3 France");
		for (Person key : france.keySet()) {
			System.out.println(key.getCountry()+", "+key.getPerson_id()+", "+france.get(key));
		}
		System.out.println("Top 3 Spain");
		for (Person key : spain.keySet()) {
			System.out.println(key.getCountry()+", "+key.getPerson_id()+", "+spain.get(key));

		}
		System.out.println("Top 3 Italy");
		for (Person key : italy.keySet()) {
			System.out.println(key.getCountry()+", "+key.getPerson_id()+", "+italy.get(key));

		}
		System.out.println("Top 3 global");
		for (Person key : result.keySet()) {
			System.out.println(key.getCountry()+", "+key.getPerson_id()+", "+result.get(key));

		}
		
	}


		
	

}