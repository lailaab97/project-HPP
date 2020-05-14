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
 * DataParsing reads csv input files and calls suitable methods to process the generated data
 * 
 * @author Team
 * 
 */
public class DataParsing {
	
	public void fetchCsvFileData(File directory,String file) throws FileNotFoundException {
		
		
		ArrayList<Tree> mainListOfResults = new ArrayList<Tree>();
 
        final String delimiter = ", ";
		
        
        FileReader fr = new FileReader(directory+"/"+file);
        BufferedReader br = new BufferedReader(fr);
		      
		String line = "";
		String[] infos;
				           
				           
		try {				           
			System.out.println(file.toString());     
			while((line = br.readLine()) != null) {
				infos = line.split(delimiter);

				if(infos[5].contentEquals("unknown")){
					Person victim = new Person(Integer.parseInt(infos[0]),Math.round(Float.parseFloat(infos[4])),-1,file.replace(".csv", ""),10);
					System.out.println(victim.toString());     
					
					//process(victim, mainListOfResults);
					//generate()
				}else {
					Person victim = new Person(Integer.parseInt(infos[0]),Math.round(Float.parseFloat(infos[4])),Integer.parseInt(infos[5]),file.replace(".csv", ""),10);
					//process(victim, mainListOfResults);
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
