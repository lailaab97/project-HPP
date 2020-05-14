package parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import beans.Person;
import structure.Tree;


/**
 * DataParsing reads CSV input files and calls suitable methods to process the generated data
 * 
 * @author Team
 * 
 */
public class DataParsing {

	
	/**
	 * This method Reads and Parses CSV files
	 * @param directory: directory of input data, file: file name
	 * @return
	 */
	public void fetchCsvFileData(File directory,String file) throws FileNotFoundException {
		
		
		ArrayList<Tree> mainListOfResults = new ArrayList<Tree>();
 	
        
        FileReader fr = new FileReader(directory+"/"+file);
        BufferedReader br = new BufferedReader(fr);
		      
		String line = "";
		String[] infos;
        String delimiter = ", ";		           
				           
		try {				           
			System.out.println(file.toString());     
			while((line = br.readLine()) != null) {
				infos = line.split(delimiter);
				
				int person_id;
				int diagnosed_ts;
				int contaminated_by ;
				String country;
				int score;

				// if the contaminer is unknown
				if(infos[5].contentEquals("unknown")){
					
					person_id = Integer.parseInt(infos[0]);
					diagnosed_ts = Math.round(Float.parseFloat(infos[4]));
					contaminated_by = -1;
					country = file.replace(".csv", "");
					score = 10;
					Person victim = new Person(person_id,diagnosed_ts,contaminated_by,country,score);
					
					//System.out.println(victim.toString());     
					//TODO
					//process(victim, mainListOfResults);
					//generate()
					
					
				}else {
					person_id = Integer.parseInt(infos[0]);
					diagnosed_ts = Math.round(Float.parseFloat(infos[4]));
					contaminated_by = Integer.parseInt(infos[5]);
					country = file.replace(".csv", "");
					score = 10;
					Person victim = new Person(person_id,diagnosed_ts,contaminated_by,country,score);
					
					//TODO
					//process(victim, mainListOfResults);
					//generate()
				}
		            
			  }
			  
			br.close();
			            
        	} catch (FileNotFoundException e) {
        		e.printStackTrace();
            } catch(IOException ioe) {
               ioe.printStackTrace();
            }
				   			   
			   
			}
		
	public void StoreResultData(File directory,String file) throws FileNotFoundException {
		
	}
	

}
