import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class HangmanDriver {

	public static final String[] body = {"  0", " /", "\\", "|\n  |", " /", " \\"};
	public static final int mistakes_MAX = body.length;
	public static String[] hangmen;
	
	public static void main(String[] args) throws FileNotFoundException {
		String cd = System.getProperty("user.dir");
		File keyboard = new File(cd + "/src/ascii/keyboard.txt");
		String myText = "", myHangman = "";
		Scanner in = new Scanner(keyboard);
		while(in.hasNextLine())
			myText += (in.nextLine() + "\n");
		in.close();
		
		hangmen = new String [mistakes_MAX+1];
		int index = 0;
		String path;
		while(index < mistakes_MAX + 1) {
			path = cd + "/src/ascii/hangman" + index + ".txt";
			File hangman = new File(path);
			in = new Scanner(hangman);
			while(in.hasNextLine())
				myHangman += (in.nextLine() + "\n");
			in.close();
			hangmen[index] = myHangman;
			index++;
			myHangman = "";
		}
		
		
		File title = new File(cd + "/src/ascii/title.txt");
		String myWord = "";
		boolean stillPlaying = true;
		
		while(stillPlaying) {
			in = new Scanner(title);
			while(in.hasNextLine()) 
				System.out.println(in.nextLine());
			in.close();
			in = new Scanner(System.in);
			System.out.println("\nEnter the word for new game: ");
			myWord = in.nextLine().toUpperCase();
			String[] resolved = new String[myWord.length()]; 
			playGame(in, false, 0, 0, myWord, myText, resolved, hangmen[0]);
			System.out.println("Would you like to play again? [Y/N]");
			char ans = in.next().charAt(0);
			if(ans != 'y' && ans != 'Y')
				stillPlaying = false;
		}
		in.close();
		System.out.println("Goodbye.");
			
	}
	
	public static void playGame(Scanner in, boolean isOver, int mistakes, int correct, String word, String text, String[] resolved, String hangman) throws FileNotFoundException {
		System.out.println("\n" + hangman);
		in = new Scanner(System.in);
		System.out.print("\nWORD: ");	
		for(String s : resolved) {
			if(s == null) System.out.print(" __ ");
			else System.out.print(s);
		}
		System.out.println("\n\n" + text +  "\n" + (mistakes_MAX-mistakes) + " guess(es) left.");
		if(isOver == false) {	
			System.out.println("\nEnter a Letter");
			char letter = in.next().toUpperCase().charAt(0);		
			if(text.indexOf(letter) != -1) {
				text = text.replace(letter, '-');
				if(word.indexOf(letter) != -1) {
					int index = 0;
					for(char c : word.toCharArray()) {
						if(letter == c) {
							correct++;
							resolved[index] = " " + c + " ";
						}
						index++;
					}
					System.out.print("Good guess!");
					if(correct == word.length()) {
						System.out.print(" You won!\n");
						playGame(in, true, mistakes, correct, word, text, resolved, hangman);
					} else {
						System.out.println("");
						playGame(in, false, mistakes, correct, word, text, resolved, hangman);
					}
				} else {
					mistakes++;
					hangman = hangmen[mistakes];
					if(mistakes == mistakes_MAX) {
						System.out.println("Game Over!");
						playGame(in, true, mistakes, correct, word, text, resolved, hangman);
					} else {
						playGame(in, false, mistakes, correct, word, text, resolved, hangman);
					}
				}
			} else {
				System.out.println("That is not an option. Try Again.\n\n");
				playGame(in, false, mistakes, correct, word, text, resolved, hangman);
			}
		}
	}

}
