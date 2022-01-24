package main;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Main {
	public static void main(String[] args) {
		
		System.out.println("Welcome to Wordle Helper!");
		// collect all our words, and their values
		HashMap<String, Integer> wordHash = calculateWordPoints();
		// recommend the top 5 words to give the player options
		System.out.println("We recommend the following starting Words: ");
		Map<String, Integer> recommendations = wordHash.entrySet().stream()
				.sorted((o1, o2) -> o2.getValue() - o1.getValue())
				.limit(5)
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		
		for(String rec : recommendations.keySet()) {
			System.out.println("\t" + rec);
		}
		
		// determine if the game is over
		Boolean gameOver = false;
		
		// allow for user input
		Scanner in = new Scanner(System.in);
		
		// game loop
		while (!gameOver) {
			
			// initialize some variables for our while loops
			String check = "N";
			String wordInput = "";
			
			// user enters the word they played in the game
			while((!check.equals("Y")) && (!check.equals("y"))) {
				System.out.print("Enter Your Word to Continue: ");
				wordInput = in.nextLine();
				while (!checkWord(wordInput)) {
					// word invalid
					System.out.print("Your Word is Invalid Try again: ");
					wordInput = in.nextLine().toLowerCase();
				}
				System.out.print("\nYou Entered: " + wordInput + "? (Y/N) ");
				check = in.nextLine();
			}
			// resetting variables so we can use them again
			check = "N";
			String placementInput = "";
			
			// user enters the patter they got back
			while((!check.equals("Y")) && (!check.equals("y"))) {
				System.out.println("What result did you get back?");
				System.out.println("\tN - for letter not in word");
				System.out.println("\tY - for letter not in correct place");
				System.out.println("\tG - for letter in correct place");
				placementInput = in.nextLine().toUpperCase();
				while(!checkPlacement(placementInput)) {
					System.out.print("Your Result is Invalid Try again (e.g. NGYGN): ");
					placementInput = in.nextLine();
				}
				System.out.print("\nYou Got Back: " + placementInput + "? (Y/N) ");
				check = in.nextLine();
			}
			
			// if the player gets back all green they won, and its game over
			if(placementInput.equals("GGGGG")) {
				System.out.println("You Won!");
				gameOver = true;
				break;
			}
			
			// filter our word list based on the the feedback from the game
			wordHash = generateNewWordList(placementInput.split(""), wordInput.split(""), wordHash);
			
			// make new recommendations based on our new list (if we have words still)
			if(wordHash.size() > 0) {
				System.out.println("We recommend the following words next: ");
				recommendations = wordHash.entrySet().stream()
						.sorted((o1, o2) -> o2.getValue() - o1.getValue())
						.limit(5)
						.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
				
				for(String rec : recommendations.keySet()) {
					System.out.println("\t" + rec);
				}
			} else {
				// our word list isn't perfect so if we run out of words to suggest its game over
				gameOver = true;
				System.out.println("Sorry, we don't have any words that fit these restrictions");
			}
		}
		// close out input stream
		in.close();
	}
	
	// generate a new word list, based on input stream, word input, and previous word list
	public static HashMap<String, Integer> generateNewWordList(String[] placementInput, String[] wordInput, HashMap<String, Integer> wordHash) {
		
		ArrayList<String> removeLetters = new ArrayList<String>();
		HashMap<String, ArrayList<Integer>> wrongPlaceLetters = new HashMap<String, ArrayList<Integer>>();
		HashMap<Integer, String> rightPlaceLetters = new HashMap<Integer, String>();
		
		
		// iterating over out two arrays (placementInput and wordInput)
		for(int i=0; i < 5; i++) {
			String placement = placementInput[i];
			String letter = wordInput[i];
			
			// if we get a letter that isn't in our final word
			// we want to remove words with that letter
			if(placement.equals("N")) {
				removeLetters.add(letter);
				
			// if we get letters in the correct place
			// we want to keep only words with that letter in that place
			// same letter could be in two place
			// same place will only have one correct letter
			} else if(placement.equals("G")) {
				rightPlaceLetters.put(i, letter);
				
			// if we get letters that are in the wrong place
			// we want to keep words with those letters in them
			} else if(placement.equals("Y")) {
				// we want to keep track of were the letters
				// don't suggest letters in the same place
				// we could have two of the same letter in our word
				// and have them both be in the wrong place so we have an ArrayList
				if(wrongPlaceLetters.get(letter) == null) {
					ArrayList<Integer> placementList = new ArrayList<Integer>();
					placementList.add(i);
					wrongPlaceLetters.put(letter, placementList);
				} else {
					ArrayList<Integer> placementList = wrongPlaceLetters.get(letter);
					placementList.add(i);
					wrongPlaceLetters.put(letter, placementList);
				}
			}
		}
		
		// Step 1: Filter out all the letters that aren't there
		HashMap<String, Integer> filteredMapNotThere = new HashMap<String, Integer>();
		
		for (Map.Entry<String, Integer> set : wordHash.entrySet()) {
			String word = set.getKey();
			String[] strArray = word.split("");
			Boolean wordValid = true;
			for(int i=0; i < 5; i++) {
				String letter = strArray[i];
				
				// remove words with letter we know aren't in it
				if (removeLetters.contains(letter)) {
					wordValid = false;
				}
			}
			
			if(wordValid) {
				filteredMapNotThere.put(word, set.getValue());
			}
		}
		
		// Step 2: Keep only words with correct letter placement
		HashMap<String, Integer> filteredMapRightPlacement = new HashMap<String, Integer>();
		
		for (Map.Entry<String, Integer> setWord : filteredMapNotThere.entrySet()) {
			String word = setWord.getKey();
			String[] strArray = word.split("");
			Boolean wordValid = true;
			for (Map.Entry<Integer, String> setLetter : rightPlaceLetters.entrySet()) {
				String letter = setLetter.getValue();
				Integer position = setLetter.getKey();
				if (!strArray[position].equals(letter)) {
					// letter in right place so its valid
					wordValid = false;
				}
			}
			
			if(wordValid) {
				filteredMapRightPlacement.put(word, setWord.getValue());
			}
		}
		
		// Step 3: Keep only words with our wrong place letters in them
		HashMap<String, Integer> containingYellow = new HashMap<String, Integer>();
		
		Set<String> keepLetters = wrongPlaceLetters.keySet();
		
		for (Map.Entry<String, Integer> set : filteredMapRightPlacement.entrySet()) {
			String word = set.getKey();
			String[] strArray = word.split("");
			List<String> strList = Arrays.asList(strArray);
			Boolean wordValid = true;
			
			for(String letter : keepLetters) {
				if(!strList.contains(letter)) {
					wordValid = false;
				} else {
					// word does contain letter
					ArrayList<Integer> incorrectIndexes = wrongPlaceLetters.get(letter);
					Integer index = strList.indexOf(letter);
					if(incorrectIndexes.contains(index)) {
						// index we have already tried and know is incorrect
						wordValid = false;
					}
				}
			}
			
			if(wordValid) {
				containingYellow.put(word, set.getValue());
			}
		}
		
		// return our final HashMap
		return containingYellow;
	}
	
	// check if a word is valid (5 letters)
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
	  
	  // check if placement is valid (5 characters, all characters must be either N,Y,or G
	  public static Boolean checkPlacement(String input) {
		  List<String> listvalidChar = Arrays.asList("N", "Y", "G");
		  input = input.trim();
		  if(input.length() != 5) {
			  return false;
		  }
		  String[] strArray = input.split("");
		  for (String letter : strArray) {
			if (!listvalidChar.contains(letter)) {
				return false;
			}
		  }
		  return true;
		  
	  }
	
	  // calculate the points for each word based on the frequency of each letter
	public static HashMap<String, Integer> calculateWordPoints() {
		HashMap<String, Integer> letterFrequency = readInLetterFrequency();
		ArrayList<String> words = readInWords();
		HashMap<String, Integer> wordValues = new HashMap<String, Integer>();
		
		for (String word : words) {
			Integer wordPoints = 0;
			String[] strArray = word.split("");
			// duplicates don't help us eliminate possible letters so they count for nothing
			String[] noDupsArray = removeDuplicates(strArray);
			for (String letter : noDupsArray) {
				Integer points = letterFrequency.get(letter);
				wordPoints += points;
			}
			
			wordValues.put(word, wordPoints);
		}
		
		return wordValues;
		
	}
	
	// turn our array into a set then back to an array to remove duplicates
	public static String[] removeDuplicates(String[] strArray) {
		LinkedHashSet<String> strSet = new LinkedHashSet<String>(Arrays.asList(strArray));
		String[] newArray = strSet.toArray(new String[ strSet.size() ]);
		return newArray;
		
	}
	
	// read the words in from the 5-letter word file
	public static ArrayList<String> readInWords() {
		ArrayList<String> wordList = new ArrayList<String>();
	    try {
	      File myObj = new File("5-letter.txt");
	      Scanner myReader = new Scanner(myObj);
	      while (myReader.hasNextLine()) {
	        String data = myReader.nextLine();
	    	wordList.add(data);
	      }
	      myReader.close();
	    } catch (FileNotFoundException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
		
		return wordList;
	}
	
	// read the letter frequency in from the letterFrequency file
	public static HashMap<String, Integer> readInLetterFrequency() {
		HashMap<String, Integer> letterFrequency = new HashMap<String, Integer>();
		BufferedReader br = null;
		try {
	      File myObj = new File("letterFrequency.txt");
	      br = new BufferedReader(new FileReader(myObj));
	      String line = null;
	      
	      while ((line = br.readLine()) != null) {
	        String[] parts = line.split(":");
	        String letter = parts[0].trim();
	        String count = parts[1].trim();
	        
	        if (!letter.equals("") && !count.equals("")) {
	        	letterFrequency.put(letter, Integer.parseInt(count));
	        }
	    	
	      }
	    } catch (Exception e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    } finally {
	    	if (br != null) {
	    		try {
	    			br.close();
	    		} catch (Exception e) {
	    			
	    		};
	    	}
	    }
		
		return letterFrequency;
	}

}
