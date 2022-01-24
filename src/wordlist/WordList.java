package wordlist;

import wordlist.LetterPopularity;
import wordlist.ReadFile;

public class WordList {
	public static void main(String[] args) {
		ReadFile fileReader = new ReadFile();
		fileReader.collectFiveLetterWords();
		LetterPopularity letterPopulator = new LetterPopularity();
		letterPopulator.calculateLetterFrequency();	
	}

}
