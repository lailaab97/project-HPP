package projetHpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import beans.Person;
import parsing.DataParsing;
import parsing.ReaderThread;

/**
 * MainClass executes the process both with threads and without them
 * 
 * @author Team
 * 
 */
public class MainClass {
	
	static DataParsing parser = new DataParsing();
	
	// Get properties file informations
	static List<String> properties = parser.getMainPath("properties/path.properties");
	static String	path = properties.get(0);
	static String SlashOrTwoBackSlash = properties.get(1);
	
	
	static File myDirectory = new File(path);
	static String[] containingFilesNames = myDirectory.list();
	//The result map to store the top 3 global
	static Map<Person,Integer> result;
	// Map for the top 3 in France
	static Map<Person,Integer> resultFrance = new LinkedHashMap<Person,Integer>();
	// Map for the top 3 in Spain
	static Map<Person,Integer> resultSpain = new LinkedHashMap<Person,Integer>();
	// Map for the top 3 in Italy
	static Map<Person,Integer> resultItaly = new LinkedHashMap<Person,Integer>();
	
	//Last date of contamination of each country
	static int lasContaminationDateFrance = 0 ;
	static int lasContaminationDateSpain = 0 ;
	static int lasContaminationDateItaly = 0 ;
	
	/**
	 * Executes process using Threads
	 * **/
	public static void executeThreads() throws InterruptedException {
		/*TRY THREAD*/
//		
		//Create a blocking a queue
	 	BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024);
	 	//Instanciate ReaderThread for France and create a thread
	    ReaderThread readerFrance = new ReaderThread(queue,"France.csv");
	    Thread readerThreadFrance = new Thread(readerFrance);
	 	//Instanciate ReaderThread for Spain and create a thread
	    ReaderThread readerSpain = new ReaderThread(queue,"Spain.csv");
	    Thread readerThreadSpain = new Thread(readerSpain);
	 	//Instanciate ReaderThread for Italy and create a thread
	    ReaderThread readerItaly = new ReaderThread(queue,"Italy.csv");
	    Thread readerThreadItaly = new Thread(readerItaly);
	    
	    
			long start = System.nanoTime();//we start counting the time of execution from here
			//The starts all three threads
			    readerThreadFrance.start();
			    readerThreadSpain.start();
			    readerThreadItaly.start();
			//Wait for every thread to finish	    
 			    readerThreadFrance.join();
 			    readerThreadSpain.join();
 			    readerThreadItaly.join();
 		    //recovering the top 3 of each country
 			    resultFrance = readerFrance.getResult();
 			    resultSpain = readerSpain.getResult();
 			    resultItaly = readerItaly.getResult();
			//recovering the last date of contamination for each country    
 				lasContaminationDateFrance =  readerFrance.getLastContaminationDate() ;
 				lasContaminationDateSpain = readerSpain.getLastContaminationDate() ;
 				lasContaminationDateItaly = readerItaly.getLastContaminationDate() ;
 			//generating the global top 3	
 				result = parser.generateFinalResult(resultSpain, lasContaminationDateSpain, resultFrance, lasContaminationDateFrance, resultItaly, lasContaminationDateItaly, myDirectory, SlashOrTwoBackSlash );
 				
// 				System.out.println("Top 3 global");
// 				for (Person key : result.keySet()) {
// 					System.out.println(key.getCountry()+", "+key.getPerson_id()+", "+result.get(key));
// 				}
 			    long end = System.nanoTime();// we stop counting the time of execution
 				long elapsedTime= end-start; // time elapsed 
				System.out.println("Elapsed time using Thread is: "+Math.abs(elapsedTime));
		
	}
	/**
	 * Executes process without using Threads
	 * **/
	public static void executeWithoutThreads() {
		
		long start1 = System.nanoTime();//we start counting the time
		if(containingFilesNames != null) {
		for (String fileName : containingFilesNames) {
			try {
			if (fileName.matches("France.csv")) {
			//  if (fileName.matches("Italy.csv")) {
						try {
								resultFrance = parser.fetchCsvFileData( myDirectory, fileName, SlashOrTwoBackSlash );
								lasContaminationDateFrance = parser.getLast(resultFrance).getKey().getDiagnosed_ts();
								//System.out.println(lasContaminationDateFrance);
							
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

			   }
			if (fileName.matches("Italy.csv")) {
			//  if (fileName.matches("Italy.csv")) {
						try {
								resultItaly = parser.fetchCsvFileData( myDirectory, fileName, SlashOrTwoBackSlash );
								lasContaminationDateItaly = parser.getLast(resultItaly).getKey().getDiagnosed_ts();
								//System.out.println(lasContaminationDateItaly);



							
							
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

			   }
			if (fileName.matches("Spain.csv")) {
			//  if (fileName.matches("Italy.csv")) {
						try {
								resultSpain = parser.fetchCsvFileData( myDirectory, fileName, SlashOrTwoBackSlash );
								lasContaminationDateSpain = parser.getLast(resultSpain).getKey().getDiagnosed_ts();
								//System.out.println(lasContaminationDateSpain);


							
							
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

			   }
			}catch(Exception e)
			{
				System.out.println("No file available");
			}

		}

		
		result = parser.generateFinalResult(resultSpain, lasContaminationDateSpain, resultFrance, lasContaminationDateFrance, resultItaly, lasContaminationDateItaly,myDirectory, SlashOrTwoBackSlash);
		long end1 = System.nanoTime();// we stop counting the time
		long elapsedTime1 = end1-start1; // time elapsed
		System.out.println("Elapsed time not using thread is: "+Math.abs(elapsedTime1));
		}
		
	}
	public static void main(String[] args) throws InterruptedException { 
		
		
	//Execute process with threads
		//executeThreads();
	//Execute process without threads
		executeWithoutThreads();
		
	}


		
	

}