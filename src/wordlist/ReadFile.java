package wordlist;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files

public class ReadFile {
	
	static String inputFileName = "words.txt";
	static String outputFileName = "5-letter.txt";
			
  public void collectFiveLetterWords() {
	createFile();
	ArrayList<String> fiveLetterWords = new ArrayList<String>();
    try {
      File myObj = new File(inputFileName);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
    	if (checkWord(data) == true) {
    		fiveLetterWords.add(data.toLowerCase());
    	}
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    writeToFile(fiveLetterWords);
    System.out.println("Done");
  }
  
  public static Boolean checkWord(String word) {
	  if(word.length() != 5) {
		  return false;
	  }
	  for(int i=0; i < word.length(); i++) {
	         Boolean flag = Character.isLetter(word.charAt(i));
	         if (flag == false) {
	        	 return false;
	         }
	  }
	  return true;
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
  
  public static void writeToFile(ArrayList<String> words) {
	  try {
		  FileWriter myWriter = new FileWriter(outputFileName);
		  for(String str: words) {
			  myWriter.write(str + System.lineSeparator());
		  }
		  myWriter.close();
	  } catch (IOException e) {
		  System.out.println("An error occurred.");
		  e.printStackTrace();
	  }
  }
}