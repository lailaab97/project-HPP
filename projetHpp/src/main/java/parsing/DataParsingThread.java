package parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import beans.Person;

public class DataParsingThread implements Runnable{
	
	Map<Person, Integer> mapOfIdsAndScores = new HashMap<Person, Integer>(); 
	File directory;
	String file;
	String SlashOrTwoBackSlash;
	DataParsing parser = new DataParsing();

	

	public DataParsingThread(File directory, String file, String slashOrTwoBackSlash) {
		super();
		this.directory = directory;
		this.file = file;
		SlashOrTwoBackSlash = slashOrTwoBackSlash;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			parser.fetchCsvFileData(this.directory, this.file, this.SlashOrTwoBackSlash);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
