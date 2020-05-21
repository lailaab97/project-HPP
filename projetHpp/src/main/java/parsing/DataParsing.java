package parsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import beans.Person;
import structure.Tree;
import processing.Process;


/**
 * DataParsing reads CSV input files, calls suitable methods to process the generated data and stores the result in a csv ouput file
 * 
 * @author Team
 * 
 */
public class DataParsing {
	
	private static final Logger logger = Logger.getLogger(DataParsing.class.getName()); //Logger pour afficher les erreurs
	
	// Map that contains the results of each proccessed input data line
	Map<Person, Integer> mapOfIdsAndScores = new HashMap<Person, Integer>(); 

	
	/**
	 * This method Reads and Parses CSV files
	 * @param directory: directory of input data, file: file name
	 * @return void
	 */
	
	public void fetchCsvFileData(File directory, String file, String SlashOrTwoBackSlash) throws FileNotFoundException {
		
		List<Tree> mainListOfResults = new ArrayList<Tree>();
		Process processLine = new Process() ;
        

		// Reader to read the csv inout file 
        FileReader fr = new FileReader( directory+SlashOrTwoBackSlash+file );
        BufferedReader br = new BufferedReader( fr );
		      
 
		String line = "";
		String[] lineInfos;
        String delimiter = ", ";		           
				           
		try {			
			
			
			int person_id;
			int diagnosed_ts;
			int contaminated_by ;
			String country;
			int score;
			int cpt = 0;
			
			    	// Writer to write in the csv output file
					FileWriter fw = new FileWriter( directory+SlashOrTwoBackSlash+"output.csv", true );
			        BufferedWriter bw = new BufferedWriter(fw); 


					// Access to input file lines and process them line by line
					while(( line = br.readLine()) != null ) {
						
						cpt++;
						lineInfos = line.split(delimiter);
		
						// if the contaminer is unknown
						if(lineInfos[5].contentEquals("unknown")){
							
							person_id = Integer.parseInt(lineInfos[0]);
							diagnosed_ts = Math.round(Float.parseFloat(lineInfos[4]));
							contaminated_by = -1;
							country = file.replace(".csv", "");
							score = 10;
							
							// Creation of a new Person whose contaminer is unknow
							Person victim = new Person(person_id,diagnosed_ts,contaminated_by ,country,score);
				
							// Processing data
							mainListOfResults = processLine.process(victim, mainListOfResults);
							mapOfIdsAndScores = processLine.generate(mainListOfResults,victim.getCountry());
							
							//Debug
							System.out.println("*********Event "+cpt);						
							
						}else {
							
							person_id = Integer.parseInt(lineInfos[0]);
							diagnosed_ts = Math.round(Float.parseFloat(lineInfos[4]));
							contaminated_by = Integer.parseInt(lineInfos[5]);
							country = file.replace(".csv", "");
							score = 10;
							
							// Creation of a new Person
							Person victim = new Person(person_id,diagnosed_ts,contaminated_by,country,score);
							
							// Processing data
							mainListOfResults = processLine.process(victim, mainListOfResults);
							mapOfIdsAndScores = processLine.generate(mainListOfResults,victim.getCountry());
							
							//Debug
							System.out.println("********Event "+cpt);
		
						}
						
						
						// Storing proccesd data fort the current line in the current country
					//	synchronized (bw) {
							StoreResultData(bw);
						//}
				            
					  }

			// Closing access to files  
			br.close();
			bw.close();
			            
        	} catch (FileNotFoundException e) {
        		e.printStackTrace();
            } catch(IOException ioe) {
               ioe.printStackTrace();
            }
				   			   
			   
			
	}
	
	
	 /** 
	  * Stores final output data for each line in a csv file
	 * @param csvWriter: the buffer writer
	 * @return void
	 */	
	public void StoreResultData(BufferedWriter csvWriter){
						
				String dataLine = null;
				
				// Get the the processed inormations in a suitable format
				for (Map.Entry<Person, Integer> mapElement : mapOfIdsAndScores.entrySet()) { 
					
		            Person key = (Person)mapElement.getKey(); 
		            int value = ((int)mapElement.getValue());
		            
		            if(dataLine == null) {
		            	dataLine = key.getCountry() + " ," + key.getPerson_id()+ " ," + value;
		            }else {
			            dataLine = dataLine +"; "+ key.getCountry() + " ," + key.getPerson_id()+ " ," + value;
		            }
		            
		        } 
		
				try {
					// Write the line result in the output file
					csvWriter.write(dataLine+"\n");
		    	    csvWriter.flush();
		    	    
					//Debug
		    	    System.out.println(dataLine);
		    	    
				} catch (IOException e1) {
					e1.printStackTrace();
				}

		
	}

		
	 /** 
	  * Fetches the data file path
	 * @param Location : path to path.properties file in resources
	 * @return List<String>: path, and / or \\ depending on OS
	 */
	public List<String> getMainPath(String Location) {
		    
			String line = null;
			Path path;
			List<String> properties = new ArrayList<>();

			
			try {
				
				path = Paths.get(ClassLoader.getSystemResource(Location).toURI());
				
				try(BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
				       
			      
			      // Reading the first two lines to get the path
				      for(int i = 0; i < 2; i++) {
				    	  line = reader.readLine();
				    	  if (line == null) {
				    		  throw new Exception("incorrect credentials file : consumer keys are missing");
				    	  }
				    	  properties.add(line);
				      }
			      
			   
			    }catch(Exception ex){
			    	logger.log(Level.SEVERE, "failed to read properties file : " + ex.toString());
			    }
				
			} catch (URISyntaxException e) {
				logger.log(Level.SEVERE, "failed to get properties file path : " + e.toString());
			}
		    
		    return properties;
		}

}
