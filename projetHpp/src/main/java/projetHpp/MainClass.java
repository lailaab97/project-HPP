package projetHpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import beans.Person;
import parsing.DataParsing;
import parsing.ReaderThread;

public class MainClass {
	
	
	public static void main(String[] args) throws InterruptedException { 
		
		DataParsing parser = new DataParsing();
		
		// Get properties file informations
		List<String> properties = parser.getMainPath("properties/path.properties");
		String	path = properties.get(0);
		String SlashOrTwoBackSlash = properties.get(1);
		
		File myDirectory = new File(path);
		Map<Person,Integer> resultFrance = new HashMap<Person,Integer>();
		Map<Person,Integer> resultSpain = new HashMap<Person,Integer>();
		Map<Person,Integer> resultItaly = new HashMap<Person,Integer>();

		String[] containingFilesNames = myDirectory.list();
		
		/**
		 * NOT USING THREADS
		 * **/
		
		
		/*TRY THREAD*/
//		
	 	BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024);

	    ReaderThread readerFrance = new ReaderThread(queue,"France.csv");
	    Thread readerThreadFrance = new Thread(readerFrance);
	    
	    ReaderThread readerSpain = new ReaderThread(queue,"Spain.csv");
	    Thread readerThreadSpain = new Thread(readerSpain);
	    
	    ReaderThread readerItaly = new ReaderThread(queue,"Italy.csv");
	    Thread readerThreadItaly = new Thread(readerItaly);
	    
	    
			long start = System.nanoTime();//we start counting the time

			    readerThreadFrance.start();
			    readerThreadSpain.start();
			    readerThreadItaly.start();
				    
 			    readerThreadFrance.join();
 			    readerThreadSpain.join();
 			    readerThreadItaly.join();

 			    resultFrance = readerFrance.getResult();
 			    resultSpain = readerSpain.getResult();
 			    resultItaly = readerItaly.getResult();
			    
 				int lasContaminationDateFrance = 0 ;
 				int lasContaminationDateSpain = 0 ;
 				int lasContaminationDateItaly = 0 ;
 				
 				Map<Person,Integer> result = parser.generateFinalResult(resultSpain, lasContaminationDateSpain, resultFrance, lasContaminationDateFrance, resultItaly, lasContaminationDateItaly);
 				
// 				System.out.println("Top 3 global");
// 				for (Person key : result.keySet()) {
// 					System.out.println(key.getCountry()+", "+key.getPerson_id()+", "+result.get(key));
// 				}
 			    long end = System.nanoTime();// we stop counting the time
 				long elapsedTime= end-start; // time elapsed 
				System.out.println("Elapsed time using Thread is: "+Math.abs(elapsedTime));

/**
 * NOT USING THREADS
 * **/
				
				
// */
		
/*		long start1 = System.nanoTime();//we start counting the time

		Map<Person,Integer> france = new HashMap<Person,Integer>();
		Map<Person,Integer> spain = new HashMap<Person,Integer>();
		Map<Person,Integer> italy = new HashMap<Person,Integer>();
		
//		int lasContaminationDateFrance = 0 ;
//		int lasContaminationDateSpain = 0 ;
//		int lasContaminationDateItaly = 0 ;

		
		for (String fileName : containingFilesNames) {
			if (fileName.matches("France.csv")) {
			//  if (fileName.matches("Italy.csv")) {
						try {
								france = parser.fetchCsvFileData( myDirectory, fileName, SlashOrTwoBackSlash );
								lasContaminationDateFrance = parser.getLast(france).getKey().getDiagnosed_ts();
								//System.out.println(lasContaminationDateFrance);
							
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
								//System.out.println(lasContaminationDateItaly);



							
							
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
								//System.out.println(lasContaminationDateSpain);


							
							
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

			   }


		}

		
		//Map<Person,Integer> result = new LinkedHashMap<Person,Integer>();
		result = parser.generateFinalResult(spain, lasContaminationDateSpain, france, lasContaminationDateFrance, italy, lasContaminationDateItaly);
		long end1 = System.nanoTime();// we stop counting the time
		long elapsedTime1 = end1-start1; // time elapsed
		System.out.println("Elapsed time not using thread is: "+Math.abs(elapsedTime1));

//		System.out.println("Top 3 France");
//		for (Person key : france.keySet()) {
//			System.out.println(key.getCountry()+", "+key.getPerson_id()+", "+france.get(key));
//		}
//		System.out.println("Top 3 Spain");
//		for (Person key : spain.keySet()) {
//			System.out.println(key.getCountry()+", "+key.getPerson_id()+", "+spain.get(key));
//
//		}
//		System.out.println("Top 3 Italy");
//		for (Person key : italy.keySet()) {
//			System.out.println(key.getCountry()+", "+key.getPerson_id()+", "+italy.get(key));
//
//		}
//		System.out.println("Top 3 global");
//		for (Person key : result.keySet()) {
//			System.out.println(key.getCountry()+", "+key.getPerson_id()+", "+result.get(key));
//
//		}
	*/
	}


		
	

}