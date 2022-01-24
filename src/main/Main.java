package main;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Main {
	public static void main(String[] args) {
		
		System.out.println("Welcome to Wordle Helper!");
		HashMap<String, Integer> wordHash = calculateWordPoints();
		System.out.println("We recommend the following starting Words: ");
		Map<String, Integer> recommendations = wordHash.entrySet().stream()
				.sorted((o1, o2) -> o2.getValue() - o1.getValue())
				.limit(5)
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		
		System.out.println(recommendations);
		Boolean solved = false;
		
		Scanner in = new Scanner(System.in);
		HashMap<String, Integer> filteredWordHash = null;
		
		while (!solved) {
			String check = "N";
			String wordInput = "";
			while((!check.equals("Y")) && (!check.equals("y"))) {
				System.out.print("Enter Your Word to Continue: ");
				wordInput = in.nextLine();
				while (!checkWord(wordInput)) {
					// word invalid
					System.out.print("Your Word is Invalid Try again: ");
					wordInput = in.nextLine();
				}
				System.out.print("\nYou Entered: " + wordInput + "? (Y/N) ");
				check = in.nextLine();
			}
			check = "N";
			String placementInput = "";
			while((!check.equals("Y")) && (!check.equals("y"))) {
				System.out.println("What result did you get back?");
				System.out.println("\tN - for letter not in word");
				System.out.println("\tY - for letter not in correct place");
				System.out.println("\tG - for letter in correct place");
				placementInput = in.nextLine();
				while(!checkPlacement(placementInput)) {
					System.out.print("Your Result is Invalid Try again (e.g. NGYGN): ");
					placementInput = in.nextLine();
				}
				System.out.print("\nYou Got Back: " + placementInput + "? (Y/N) ");
				check = in.nextLine();
			}
			if(placementInput.equals("GGGGG")) {
				System.out.println("You Won!");
				solved = true;
				break;
			}
			filteredWordHash = generateNewWordList(placementInput.split(""), wordInput.split(""), wordHash);
			if(filteredWordHash.size() > 0) {
				System.out.println("We recommend the following words next: ");
				recommendations = filteredWordHash.entrySet().stream()
						.sorted((o1, o2) -> o2.getValue() - o1.getValue())
						.limit(5)
						.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
				
				System.out.println(recommendations);
				wordHash = filteredWordHash;
			} else {
				solved = true;
				System.out.println("Sorry, we don't have any words that fit these restrictions");
			}
		}
		
		in.close();
		System.out.println("Done");
	}
	
	public static HashMap<String, Integer> generateNewWordList(String[] placementInput, String[] wordInput, HashMap<String, Integer> wordHash) {
		
		ArrayList<String> removeLetters = new ArrayList<String>();
		HashMap<String, ArrayList<Integer>> wrongPlaceLetters = new HashMap<String, ArrayList<Integer>>();
		HashMap<String, Integer> rightPlaceLetters = new HashMap<String, Integer>();
		
		for(int i=0; i < 5; i++) {
			String placement = placementInput[i];
			String letter = wordInput[i];
			if(placement.equals("N")) {
				removeLetters.add(letter);
			} else if(placement.equals("G")) {
				rightPlaceLetters.put(letter, i);
			} else if(placement.equals("Y")) {
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
		
		HashMap<String, Integer> filteredMapRightPlacement = new HashMap<String, Integer>();
		
		for (Map.Entry<String, Integer> setWord : filteredMapNotThere.entrySet()) {
			String word = setWord.getKey();
			String[] strArray = word.split("");
			Boolean wordValid = true;
			// HashMap<String, Integer> rightPlaceLetters = new HashMap<String, Integer>();
			for (Map.Entry<String, Integer> setLetter : rightPlaceLetters.entrySet()) {
				String letter = setLetter.getKey();
				Integer position = setLetter.getValue();
				if (!strArray[position].equals(letter)) {
					// letter in right place so its valid
					wordValid = false;
				}
			}
			
			if(wordValid) {
				filteredMapRightPlacement.put(word, setWord.getValue());
			}
		}
		
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
				}
			}
			
			if(wordValid) {
				containingYellow.put(word, set.getValue());
			}
		}
		
		return containingYellow;
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
	
	public static String[] removeDuplicates(String[] strArray) {
		LinkedHashSet<String> strSet = new LinkedHashSet<String>(Arrays.asList(strArray));
		String[] newArray = strSet.toArray(new String[ strSet.size() ]);
		return newArray;
		
	}
	
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
