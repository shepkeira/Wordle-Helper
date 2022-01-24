package wordlist;

public class Main {
	public static void main(String[] args) {
		ReadFile fileReader = new ReadFile();
		fileReader.collectFiveLetterWords();
		LetterPopularity letterPopulator = new LetterPopularity();
		letterPopulator.calculateLetterFrequency();	
	}

}
