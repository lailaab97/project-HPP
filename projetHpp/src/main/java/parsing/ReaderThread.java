package parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import beans.Person;

/**
 * ReaderThread implements Runnable and treat the file in question
 * 
 * @author Team
 * 
 */
public class ReaderThread implements Runnable{

  protected BlockingQueue<String> blockingQueue = null;
  
  
  	// we instanciate parser to process the files
  	DataParsing parser = new DataParsing();
	
	// Get properties file informations
	List<String> properties = parser.getMainPath("properties/path.properties");
	String	path = properties.get(0);
	String SlashOrTwoBackSlash = properties.get(1);
	//last contamination date registered in the whole file
	int lasContaminationDate = 0;
	
	//the name of the file (here the country)
	String file;
	
	//the map of the results of this file
	Map<Person,Integer> result = new HashMap<Person,Integer>();	
	File myDirectory = new File(path);
	String[] containingFilesNames = myDirectory.list();
	
	public int getLastContaminationDate() {
		return lasContaminationDate;

	}
	
	public Map<Person, Integer> getResult() {
		return result;
	}
	
	//Constructor 
	  public ReaderThread(BlockingQueue<String> blockingQueue,String file){
	    this.blockingQueue = blockingQueue;     
	    this.file = file;
	  }

	  
  @Override
  public void run() {
	     try {
	    	 		//we stock the result of the process of the file in result
	 				result = parser.fetchCsvFileData(myDirectory, file, SlashOrTwoBackSlash);
	 				
	 						//we register the last date of contamination registered
	 						//since result is a linkedHashMap and the results are stored from  old to new we need to get the new one
	 				lasContaminationDate = parser.getLast(result).getKey().getDiagnosed_ts();
	 				
	 	            blockingQueue.put("EOF");  //When end of file has been reached
	
	
	        } catch (FileNotFoundException e) {
	
	            System.out.println("Error in ReaderThread : could not find the path");
	        } catch (IOException e) {
	
	            e.printStackTrace();
	        } catch(InterruptedException e){
	        	e.printStackTrace();
	        }


  }



}