package projetHpp;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import beans.Person;
import parsing.DataParsing;




// This Unit Test Is based On TEST CASE 3, TEST CASE 4, TEST CASE 5 mentioned on the shared document (drive).
public class TestCases {
	
	DataParsing parser;
	Person person1,person2,person3,person4,person6,person7,person9 ;
	Map<Person,Integer> resultGlobal, resultFrance, resultItaly, resultSpain;
	int lasContaminationDateFrance, lasContaminationDateSpain, lasContaminationDateItaly; 
	String SlashOrTwoBackSlash;
	List<String> properties;
	File myDirectory;
	String	path;

	
	

	
	@Before
	public void setUp() {
		
		parser = new DataParsing();
		
		properties = parser.getMainPath("properties/path.properties");
		path = properties.get(0);
		SlashOrTwoBackSlash = properties.get(1);
		
		lasContaminationDateFrance= 0;
		lasContaminationDateSpain = 0;
		lasContaminationDateItaly = 0;


		resultFrance = new HashMap<Person,Integer>();
		resultItaly = new HashMap<Person,Integer>();
		resultSpain = new HashMap<Person,Integer>();
		resultGlobal = new HashMap<Person,Integer>();

		myDirectory = new File(path);

		
		//FRANCE
		person1=new Person(46, 1589228295, 45,"France",4);
		person2=new Person(47, 1589314695, -1,"France",14);
		//ITALY
		person3=new Person(23, 1586204295, 22,"Italy",10);
		person4=new Person(24, 1587068295, 20,"Italy",10);
		//SPAIN
		person7=new Person(7, 1590956300, 3,"Spain",10);
		person6=new Person(6, 1590956299, 5,"Spain",10);
		person9=new Person(9, 1591906699 ,5,"Spain",10);

	}

	@Test
	public void testFetchCsvFileData() throws FileNotFoundException {
		
		String expectedFrance, expectedItaly, expectedSpain, expectedGlobal;
		String outputFrance = "", outputItaly = "", outputSpain = "", outputGlobal= "";

		expectedFrance = "France, 46, 14\nFrance, 47, 14\n";
		expectedItaly = "Italy, 24, 20\nItaly, 23, 14\n";
		expectedSpain = "Spain, 7, 14\nSpain, 9, 10\nSpain, 6, 4\n";
		expectedGlobal = "Italy, 24, 20\nItaly, 23, 14\nFrance, 46, 14\n";



		
		
		
		//France Process
		resultFrance = parser.fetchCsvFileData( myDirectory, "France.csv", SlashOrTwoBackSlash );
		lasContaminationDateFrance = parser.getLast(resultFrance).getKey().getDiagnosed_ts();
		for (Map.Entry<Person, Integer> mapElement : resultFrance.entrySet()) { 
			
            Person key = (Person)mapElement.getKey(); 
            int value = ((int)mapElement.getValue());
            
            outputFrance += key.getCountry()+", "+key.getPerson_id()+", "+ value + "\n";
            
        } 

		
		//Italy Process
		resultItaly = parser.fetchCsvFileData( myDirectory, "Italy.csv", SlashOrTwoBackSlash );
		lasContaminationDateItaly = parser.getLast(resultItaly).getKey().getDiagnosed_ts();
		for (Map.Entry<Person, Integer> mapElement : resultItaly.entrySet()) { 
			
            Person key = (Person)mapElement.getKey(); 
            int value = ((int)mapElement.getValue());
            
            outputItaly += key.getCountry()+", "+key.getPerson_id()+", "+ value + "\n";

            
        } 
		
		//Spain Process
		resultSpain = parser.fetchCsvFileData( myDirectory, "Spain.csv", SlashOrTwoBackSlash );
		lasContaminationDateSpain = parser.getLast(resultSpain).getKey().getDiagnosed_ts();
		for (Map.Entry<Person, Integer> mapElement : resultSpain.entrySet()) { 
			
            Person key = (Person)mapElement.getKey(); 
            int value = ((int)mapElement.getValue());
            
            outputSpain += key.getCountry()+", "+key.getPerson_id()+", "+ value + "\n";

            
        } 
		
		
		//Global Process
		resultGlobal = parser.generateFinalResult(resultFrance, lasContaminationDateFrance, resultItaly, lasContaminationDateItaly, resultSpain, lasContaminationDateSpain, myDirectory, SlashOrTwoBackSlash );
		for (Map.Entry<Person, Integer> mapElement : resultGlobal.entrySet()) { 
			
            Person key = (Person)mapElement.getKey(); 
            int value = ((int)mapElement.getValue());
            
            outputGlobal += key.getCountry()+", "+key.getPerson_id()+", "+ value + "\n";

            
        } 
		
		// Test on France output result
		Assert.assertEquals(outputFrance.equals(expectedFrance), true);
		// Test on Italy output result
		Assert.assertEquals(outputItaly.equals(expectedItaly), true);
		// Test on Spain output result
		Assert.assertEquals(outputSpain.equals(expectedSpain), true);
		// Test on Global output result
		Assert.assertEquals(outputGlobal.equals(expectedGlobal), true);

	}

}
