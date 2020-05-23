package projetHpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import parsing.DataParsing;
import parsing.DataParsingThread;
import processing.Process;
import beans.Person;
public class MainClass {
	
	
	public static void main(String[] args) throws InterruptedException { 
		/*		
		
		DataParsing parser = new DataParsing();
		
		// Get properties file informations
		List<String> properties = parser.getMainPath("properties/path.properties");
		String	path = properties.get(0);
		String SlashOrTwoBackSlash = properties.get(1);
		
		File myDirectory = new File(path);
		
		String[] containingFilesNames = myDirectory.list();
		*/
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
		/*
		long start = System.nanoTime();//we start counting the time

		
		for (String fileName : containingFilesNames) {
			//if (fileName.matches("(Italy|France|Spain).csv")) {
			  if (fileName.matches("Italy.csv")) {
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
		*/
		
		// tests des méthodes generateResultByCountry et generateFinalResult
		Date date=new Date();
		Process proc = new Process();
		
		Map<Person,Integer> france=new HashMap<Person,Integer>();
		Map<Person,Integer> resultFrance=new LinkedHashMap<Person,Integer>();
		Person pers1=new Person(4, "a", "b", date,1, -1, "bla", "france", 10);
		Person pers2=new Person(5, "a", "b", date,3, -1, "bla", "france", 10);
		france.put(pers1,20);
		france.put(pers2,10);
		resultFrance=proc.generateResultByCountry(france);
		
		Map<Person,Integer> spain=new HashMap<Person,Integer>();
		Map<Person,Integer> resultSpain=new LinkedHashMap<Person,Integer>();
		Person pers3=new Person(1, "a", "b", date,5, -1, "bla", "france", 10);
		Person pers4=new Person(2, "a", "b", date,4, -1, "bla", "france", 10);
		Person pers5=new Person(3,"a", "b", date,7, -1, "bla", "france", 10);
		spain.put(pers3,20);
		spain.put(pers4,20);
		spain.put(pers5,10);
		resultSpain=proc.generateResultByCountry(spain);
		
		Map<Person,Integer> italy=new HashMap<Person,Integer>();
		Map<Person,Integer> resultItaly=new LinkedHashMap<Person,Integer>();
		Person pers9=new Person(6, "a", "b", date,1, -1, "bla", "france", 10);
		Person pers7=new Person(7, "a", "b", date,2, -1, "bla", "france", 10);
		italy.put(pers9,20);
		italy.put(pers7,7);
		resultItaly=proc.generateResultByCountry(italy);
		Map<Person,Integer> result=new LinkedHashMap<Person,Integer>();
		result=proc.generateFinalResult(resultSpain, 10, resultFrance, 11, resultItaly, 12);
		
		System.out.println("resultFrance");
		for (Person key : resultFrance.keySet()) {
			System.out.println(key.getPerson_id());
			System.out.println(resultFrance.get(key));
		}
		System.out.println("resultSpain");
		for (Person key : resultSpain.keySet()) {
			System.out.println(key.getPerson_id());
			System.out.println(resultSpain.get(key));
		}
		System.out.println("resultItaly");
		for (Person key : resultItaly.keySet()) {
			System.out.println(key.getPerson_id());
			System.out.println(resultItaly.get(key));
		}
		System.out.println("result");
		for (Person key : result.keySet()) {
			System.out.println(key.getPerson_id());
			System.out.println(result.get(key));
		}
		
	}

}