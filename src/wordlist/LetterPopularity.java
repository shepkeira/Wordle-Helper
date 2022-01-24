package wordlist;

import java.io.*;
import java.util.*;

public class LetterPopularity {
	static String inputFileName = "5-letter.txt";
	static String outputFileName = "letterFrequency.txt";
			
  public void calculateLetterFrequency() {
	HashMap<String, Integer> letterFrequency = new HashMap<String, Integer>();
	try {
      File myObj = new File(inputFileName);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        for(int i=0; i < data.length(); i++) {
        	String character = Character.toString(data.charAt(i));
        	if (letterFrequency.get(character) != null) {
        		letterFrequency.put(character, letterFrequency.get(character) + 1);
        	} else {
        		letterFrequency.put(character, 1);
        	}
        }
    	
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
	
	createFile();
	writeToFile(letterFrequency);
    System.out.println("Done");
  }
  
  public static void createFile() {
	  try {
		  File myObj = new File(outputFileName);
		  if (myObj.createNewFile()) {
			  System.out.println("File created: " + myObj.getName());
		  } else {
			  System.out.println("File already exists");
		  }
	  } catch (IOException e) {
		  System.out.println("An error occurred.");
		  e.printStackTrace();
	  }
  }
  
  public static void writeToFile(HashMap<String, Integer> letterFrequency) {
	  try {
		  File file = new File(outputFileName);
		  BufferedWriter bf = null;
		  bf = new BufferedWriter(new FileWriter(file));
		  
		  for (String key : letterFrequency.keySet()) {
			  bf.write(key + ":" + letterFrequency.get(key) + "\n");
			  bf.flush();
		  }
		  bf.close();
	  } catch (IOException e) {
		  System.out.println("An error occurred.");
		  e.printStackTrace();
	  }
  }
}
