# Wordle-Helper

Have you too been playing worlde every day?

I have been struggling to remember enough 5 letter words to play wordle, so I made this program to help!

## To run

### Method 1: IDE

Use your local java ide to run the program. It will open a terminal for you to input the necessary commands. I used eclipse to build and test this project, but any IDE that supports Java will work.

### Method 2: Command Line

I create a jar file that you can use to run the file in your terminal.
cd into the Wordle-Helper folder
```java -jar executable.jar```

This way it will run in your terminal, but works the same as the method in your IDE

## Using the Worlde-Helper

The instructions are shown on screen but here is a helpful guide.
1. You will be provided a list of words that contain the most common letters
2. You can pick a word from the list provided or a word of your choice
3. Enter the word when prompted
4. Confirm the word
5. Next you will be asked to enter a series of letter to represent what your response was on wordle.
    - N will represent the grey squares where you were the letter is not in the word at any spot
    - Y will represent the yellow squares where the letter is in the word but in the wrong spot
    - G will represent the greens squares where the letter is in the word and in the correct spot
    Enter the letter that represent your response.
    E.g Tower and you get back T - gray, o - Green, w - gray, e - yellow, r - yellow. 
    You would enter NGNYY
6. Confirm your response
7. You will be provided a new list of words, restricted by that feedback
8. Repeat steps 2-7 until you get back all greens
9. When you get back all greens you still need to respond with GGGGG so that the program knows you have won

## References
words.txt is the basis for the english words used in this program. 
It is taken from: https://github.com/dwyl/english-words/
Last updated on this repo: January 23rd 2022

Want to play wordle? [Play Wordle](https://www.powerlanguage.co.uk/wordle/)
