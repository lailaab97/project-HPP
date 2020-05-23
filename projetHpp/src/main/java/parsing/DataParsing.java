package parsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
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
	
	// Writer to write in the csv output file
	/**
	 * fw : to write in the output file
	 * */
	FileWriter fw;
	/**
	 * bw : the buffer writer for the output file
	 */
	BufferedWriter bw;

	
	/**
	 * mapOdIdsAndScores :  map that contains the results of each proccessed input data line
	 * */
	Map<Person, Integer> mapOfIdsAndScores = new HashMap<Person, Integer>();
	
	/**
	 * result : map that contains the top 3 of the current country
	 * */
	Map<Person,Integer> result = new HashMap<Person,Integer>();


	public Map<Person, Integer> getMapOfIdsAndScores() {
		return mapOfIdsAndScores;
	}


	public void setMapOfIdsAndScores(Map<Person, Integer> mapOfIdsAndScores) {
		this.mapOfIdsAndScores = mapOfIdsAndScores;
	}


	


	/**
	 * This method Reads and Parses CSV files
	 * @param directory: directory of input data, file: file name
	 * @return void
	 */
	
	public Map<Person, Integer> fetchCsvFileData(File directory, String file, String SlashOrTwoBackSlash) throws FileNotFoundException {
		
		//the list of all the trees available
		List<Tree> mainListOfResults = new ArrayList<Tree>();
		//we instantiate the class Process
		Process processLine = new Process() ;
		//For the output :
		String line = "";
		String[] lineInfos;
        String delimiter = ", ";		           
				           
		try {			
			// Reader to read the csv inout file 
			//The filereader and the buffer reader for reading the csv of the current country
	        FileReader fr = new FileReader( directory+SlashOrTwoBackSlash+file );
	        BufferedReader br = new BufferedReader( fr );
	        //The person's info
			int person_id;
			int diagnosed_ts;
			int contaminated_by ;
			String country;
			
			int score;
			//for the debug
			//int cpt = 0;
			
			    	// Writer to write in the csv output file
					fw = new FileWriter( directory+SlashOrTwoBackSlash+"output.csv", true );
			        bw = new BufferedWriter(fw); 


					// Access to input file lines and process them line by line
					while(( line = br.readLine()) != null ) {
						//for the debug
						//cpt++;
						lineInfos = line.split(delimiter);
						person_id = Integer.parseInt(lineInfos[0]);
						diagnosed_ts = Math.round(Float.parseFloat(lineInfos[4]));
						country = file.replace(".csv", "");
						score = 10; // every new person has a score of 10

						// if it is unknown
						if(lineInfos[5].contentEquals("unknown")){
							//parsing the person's info
							contaminated_by = -1; // if it is contaminated by "unknown"			
							
						}
						//if it is known
						else {
							contaminated_by = Integer.parseInt(lineInfos[5]);
							}
							// Creation of a new Person
							Person victim = new Person(person_id,diagnosed_ts,contaminated_by,country,score);
							// Processing data
							mainListOfResults = processLine.process(victim, mainListOfResults);
							//we generate the map of Ids and scores of all the trees existing
							mapOfIdsAndScores = processLine.generate(mainListOfResults);
							//we get the top 3 result by country
							result = processLine.generateResultByCountry(mapOfIdsAndScores);

							//Debug
							//System.out.println("********Event "+cpt);
							
							// Storing processed data fort the current line in the current country
							StoreResultData(bw, result);						

					}
					// Closing access to input file  
							br.close();
           
        	} catch (FileNotFoundException e) {
        		//e.printStackTrace();
        		System.out.println("File not found!");
            } catch(IOException ioe) {
               ioe.printStackTrace();
               
            }
			   return result;
			
	}
	
	
	 /** 
	  * Stores final output data for each line in a csv file
	 * @param csvWriter: the buffer writer
	 * @return void
	 */	
	public void StoreResultData(BufferedWriter csvWriter, Map<Person, Integer> mapResults){
						
				String dataLine = null;
				
				// Get the the processed informations in a suitable format
				for (Map.Entry<Person, Integer> mapElement : mapResults.entrySet()) { 
					
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
					//System.out.println(("yes"));
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
	
	
	 /** 
	  * Fetches the data file path
	 * @param top3Spanish : top 3 results for Spain,
	 * @param spanishContaminationDate : last date of contamination registered in Spain,
	 * @param top3French : top 3 results for France,
	 * @param frenchContaminationDate :last date of contamination registered in France,
	 * @param top3Italian : top 3 results for Italy,
	 * @param italianContaminationDate : last fate of contamination registered in Italy,
	 * @param directory: directory of input data,
	 * @param SlashOrTwoBackSlash : / or \\ depending on OS
	 * @return List<String>: path, and / or \\ depending on OS
	 */
	public Map<Person,Integer> generateFinalResult(Map<Person,Integer> top3Spanish, int spanishContaminationDate, Map<Person,Integer> top3French, int frenchContaminationDate, Map<Person,Integer> top3Italian, int italianContaminationDate,File directory, String SlashOrTwoBackSlash ){
		 
		 Map<Person,Integer> globalResult= new LinkedHashMap<Person,Integer>();
		 
		 // in everything that follows we always have :
		 // index 0 : france , index 1 : spain, index 2 : italy results
		 
		 //create list of lists of scores and ids
		// index 0 : french results , index 1 : spanish results, index 2 : italian results
		 
		 List<ArrayList<Integer>> allScores=new ArrayList<ArrayList<Integer>>();
		 allScores.add(new ArrayList<>(top3French.values()));
		 allScores.add(new ArrayList<>(top3Spanish.values()));
		 allScores.add(new ArrayList<>(top3Italian.values()));
		 
		 List<ArrayList<Person>> id=new ArrayList<ArrayList<Person>>();
		 id.add(new ArrayList<>(top3French.keySet()));
		 id.add(new ArrayList<>(top3Spanish.keySet()));
		 id.add(new ArrayList<>(top3Italian.keySet()));
		 
		 //create list of indexes to browse lists of score
		 List<Integer> indexes=new ArrayList<>();
		 indexes.add(0);
		 indexes.add(0);
		 indexes.add(0);
		 
		 int index;
		 
		 //initialize counter of elements in result
		 int cmpt=0;
		 
		 //create a list of scores where we add the first result of each country
		 // if list of scores is empty add 0
		 List<Integer> scores=new ArrayList<>();
		 if (allScores.get(0).size()!=0) {
			 scores.add(allScores.get(0).get(0));
		 } else {
			 scores.add(0);
		 }
		 if (allScores.get(1).size()!=0) {
			 scores.add(allScores.get(1).get(0));
		 } else {
			 scores.add(0);
		 }if (allScores.get(2).size()!=0) {
			 scores.add(allScores.get(2).get(0));
		 } else {
			 scores.add(0);
		 }
		 
		 //add contamination dates to a list in the right order of countries
		 List<Integer> contaminationDates=new ArrayList<>();
		 contaminationDates.add(frenchContaminationDate);
		 contaminationDates.add(spanishContaminationDate);
		 contaminationDates.add(italianContaminationDate);
		 
		 //look for max score
		 Integer max=Collections.max(scores);
		 
		 //while counter<3 (less than three elements in result)
		 //and all scores>0
		 while(cmpt<3 && max!=0) {
			 
			 // if max value occurs more than once
			 if (Collections.frequency(scores, max)>1) {
				// if max value occurs three times
				 if (Collections.frequency(scores, max)==3) {
					 
					 //get the index of min contamination date
					 int indMin=contaminationDates.indexOf(Collections.min(contaminationDates));
					 
					 //add the score 
					 globalResult.put((id.get(indMin)).get(indexes.get(indMin)),(allScores.get(indMin)).get(indexes.get(indMin)));

					 //update index in list of scores of the country
					 indexes.set(indMin,indexes.get(indMin)+1);
					 
					 //update counter
					 cmpt++;
					 
					 // if there's still elements in the list of scores
					 if (indexes.get(indMin)<allScores.get(indMin).size()) {
						 
						 //add next best score to list of scores
						 scores.set(indMin,(allScores.get(indMin)).get(indexes.get(indMin)));
					 }
					 else {
						 
						 //else put 0 at the the index of the country
						 //no more scores
						 scores.set(indMin,0);
					 }
					 
				 }
			 // if max value occurs twice
			     else {
				 
					 // find the two indexes of max 
					 int ind1=scores.indexOf(max);
					 int ind2=scores.lastIndexOf(max);
					 
					 //compare contamination dates of two countries 
					 if(contaminationDates.get(ind1)<contaminationDates.get(ind2)){
						 
						 //add ind1 to result 
						 globalResult.put((id.get(ind1)).get(indexes.get(ind1)),(allScores.get(ind1)).get(indexes.get(ind1)));
						 
						 //update index
						 indexes.set(ind1,indexes.get(ind1)+1);
						 
						 if (indexes.get(ind1)<allScores.get(ind1).size()) {
							 //add next best score to list of scores
							 scores.set(ind1,(allScores.get(ind1)).get(indexes.get(ind1)));
						 }
						 else {
							 //put 0 at index of country
							 scores.set(ind1,0);
						 }
						 
						 //update counter
						 cmpt++; 
						 
					 }
					 else{
						 
						 //add ind2 to result
						 globalResult.put((id.get(ind2)).get(indexes.get(ind2)),(allScores.get(ind2)).get(indexes.get(ind2)));
						 
						 //update index 
						 indexes.set(ind2,indexes.get(ind2)+1);
						 
						 if (indexes.get(ind2)<allScores.get(ind2).size()) {
							 //add next best score to list of scores
							 scores.set(ind2,(allScores.get(ind2)).get(indexes.get(ind2)));
						 }
						 else {
							 //put 0 at index of country
							 scores.set(ind2,0);
						 }
						 
						 //update counter
						 cmpt++;
					 }
				}
			 // if max value occurs once
			 } else {
			 	
				 //get index of max value
				 index=scores.indexOf(max);
				 
				 //add result 
				 globalResult.put((id.get(index)).get(indexes.get(index)),max);
				 
				 //update index in indexes list
				 indexes.set(index,indexes.get(index)+1);
				 
				// if there's still elements in the list of scores
				 if (indexes.get(index)<allScores.get(index).size()) {
					 
					 //add the next best element to list of scores
					 scores.set(index,(allScores.get(index)).get(indexes.get(index)));
				 }
				 else {
					 
					//else put 0 at the the index of the country
					 //no more scores
					 scores.set(index,0);
				 }
				 
				 //update counter 
				 cmpt++;
				 
			 }
			 
			 //update new max value
			 max=Collections.max(scores);
		 }
				 
		 
		// Store global result in the last line of the ouput file
		try {
				fw = new FileWriter( directory+SlashOrTwoBackSlash+"output.csv", true );
			    bw = new BufferedWriter(fw); 
			    
			    // write result
				StoreResultData(bw, globalResult);
				
				bw.close();

				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				System.out.println("Path not find where to store !");
			}
	  

		 return globalResult;
		 
	} 

/** getLast of a LinkedHashMap
 * @param map : the map of which we want he last entry
 * @return <K, V> Entry<K, V> : a map entry
 * */
	public static <K, V> Entry<K, V> getLast(Map<K, V> map) {
		  try {
		    if (map instanceof LinkedHashMap) return getLastViaReflection(map);
		  } catch (Exception ignore) { }
		  return getLastByIterating(map);
		}
	private static <K, V> Entry<K, V> getLastByIterating(Map<K, V> map) {
		  Entry<K, V> last = null;
		  for (Entry<K, V> e : map.entrySet()) last = e;
		  return last;
		}
	
	private static <K, V> Entry<K, V> getLastViaReflection(Map<K, V> map) throws NoSuchFieldException, IllegalAccessException {
		  Field tail = map.getClass().getDeclaredField("tail");
		  tail.setAccessible(true);
		  return (Entry<K, V>) tail.get(map);
		}
}