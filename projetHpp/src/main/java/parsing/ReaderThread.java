package parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import beans.Person;

public class ReaderThread implements Runnable{

  protected BlockingQueue<String> blockingQueue = null;
  
  //
  
  DataParsing parser = new DataParsing();
	
	// Get properties file informations
	List<String> properties = parser.getMainPath("properties/path.properties");
	String	path = properties.get(0);
	String SlashOrTwoBackSlash = properties.get(1);
	int lasContaminationDate = 0;
	
	public int getLastContaminationDate() {
		return lasContaminationDate;
	}
	String file;
	
	Map<Person,Integer> result = new HashMap<Person,Integer>();

	public Map<Person, Integer> getResult() {
		return result;
	}



	File myDirectory = new File(path);
	
	String[] containingFilesNames = myDirectory.list();
	

  public ReaderThread(BlockingQueue<String> blockingQueue,String file){
    this.blockingQueue = blockingQueue;     
    this.file = file;
  }

  @Override
  public void run() {
     try {
//    	 for (String fileName : containingFilesNames) {
// 			if (fileName.matches("(France|Italy|Spain).csv")) {
 				result = parser.fetchCsvFileData(myDirectory, file, SlashOrTwoBackSlash);
 				lasContaminationDate = parser.getLast(result).getKey().getDiagnosed_ts();
 				
 			//	System.out.println("Top 3 ");
// 				for (Person key : result.keySet()) {
// 					System.out.println(key.getCountry()+", "+key.getPerson_id()+", "+result.get(key));
// 				}
 	            blockingQueue.put("EOF");  //When end of file has been reached

// 			}
//    	 }


        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } catch(InterruptedException e){

        
        }


  }



}