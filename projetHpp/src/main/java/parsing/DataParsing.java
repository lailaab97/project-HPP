package parsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import beans.Person;
import structure.Tree;
import processing.Process;


/**
 * DataParsing reads CSV input files and calls suitable methods to process the generated data
 * 
 * @author Team
 * 
 */
public class DataParsing {
	
	private static final Logger logger = Logger.getLogger(DataParsing.class.getName()); //Logger pour afficher les erreurs
	
	
	Map<Person, Integer> mapOfIdsAndScores = new HashMap<Person, Integer>(); 

	
	/**
	 * This method Reads and Parses CSV files
	 * @param directory: directory of input data, file: file name
	 * @return
	 */
	
	public void fetchCsvFileData(File directory,String file) throws FileNotFoundException {
		
		List<Tree> mainListOfResults = new ArrayList<Tree>();
		Process processLine = new Process() ;
        

		
        FileReader fr = new FileReader(directory+"/"+file);
        BufferedReader br = new BufferedReader(fr);
		      
		String line = "";
		String[] infos;
        String delimiter = ", ";		           
				           
		try {			
			
			//System.out.println(file.toString()); 
			
			int person_id;
			int diagnosed_ts;
			int contaminated_by ;
			String country;
			int score;
			int cpt = 0;
			
			    	
					FileWriter fw = new FileWriter(directory+"/"+"output.csv", true);
			        BufferedWriter bw = new BufferedWriter(fw); 


					
					while((line = br.readLine()) != null) {
						cpt++;
						infos = line.split(delimiter);
						
		
		
						// if the contaminer is unknown
						if(infos[5].contentEquals("unknown")){
							
							person_id = Integer.parseInt(infos[0]);
							diagnosed_ts = Math.round(Float.parseFloat(infos[4]));
							contaminated_by = -1;
							country = file.replace(".csv", "");
							score = 10;
							
							// Creation of a new Person whose contaminer is unknow
							Person victim = new Person(person_id,diagnosed_ts,contaminated_by,country,score);
				
							
							mainListOfResults = processLine.process(victim, mainListOfResults);
							mapOfIdsAndScores = processLine.generate(mainListOfResults,victim.getCountry());
							
							//Debug
							System.out.println("Event "+cpt);
		
							
							
						}else {
							person_id = Integer.parseInt(infos[0]);
							diagnosed_ts = Math.round(Float.parseFloat(infos[4]));
							contaminated_by = Integer.parseInt(infos[5]);
							country = file.replace(".csv", "");
							score = 10;
							
							// Creation of a new Person
							Person victim = new Person(person_id,diagnosed_ts,contaminated_by,country,score);
							
							//TODO
							mainListOfResults = processLine.process(victim, mainListOfResults);
							mapOfIdsAndScores = processLine.generate(mainListOfResults,victim.getCountry());
							
							//Debug
							System.out.println("Event "+cpt);
		
						}
						
						StoreResultData(bw, mapOfIdsAndScores);
				            
					  }

			  
			br.close();
			            
        	} catch (FileNotFoundException e) {
        		e.printStackTrace();
            } catch(IOException ioe) {
               ioe.printStackTrace();
            }
				   			   
			   
			}
		
	public void StoreResultData(BufferedWriter csvWriter,Map<Person, Integer> map){
						
				String dataLine = null;
				
				for (Map.Entry<Person, Integer> mapElement : map.entrySet()) { 
					
		            Person key = (Person)mapElement.getKey(); 
		            int value = ((int)mapElement.getValue());
		            
		            if(dataLine == null) {
		            	dataLine = key.getCountry() + " ," + key.getPerson_id()+ " ," + value;
		            }else {
			            dataLine = dataLine +"; "+ key.getCountry() + " ," + key.getPerson_id()+ " ," + value;
		            }
		            
		        } 
		
				try {
					csvWriter.write(dataLine+"\n");
		    	    csvWriter.flush();
		    	    
					//Debug
		    	    System.out.println(dataLine);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

		
	}

	
	 /** 
	  * Récupere le chemin du jeu de données
	 * @param keysLocation : le chemin vers le fichier de path.properties
	 * @return un string: le chemin recherché
	 */
	public String getMainPath(String keysLocation) {
	    
		String line = null;
		Path path;
		
		try {
			
			path = Paths.get(ClassLoader.getSystemResource(keysLocation).toURI());
			
			try(BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
			       
		      
		      // Lecture des ma première ligne (qui correspondent aux chemin des fichiers de données)
	    	  line = reader.readLine();
	    	  if (line == null) {
	    		  throw new Exception("incorrect inormations : the path is missing in the properties file");
	    	  }
		      
		   
		    }catch(Exception ex){
		    	logger.log(Level.SEVERE, "failed to read properties file : " + ex.toString());
		    }
			
		} catch (URISyntaxException e) {
			logger.log(Level.SEVERE, "failed to get properties file path : " + e.toString());
		}
	    
	    return line;
	}

}
