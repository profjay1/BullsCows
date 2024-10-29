package bullscows;

import java.util.*;

/**
 * @author Saheed Omotola
 */
public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Input the length of the secret code:\n> ");
        String input = sc.nextLine().trim();
        int length = 0;

        if (input.matches("\\d+")) {
            length = Integer.parseInt(input);

            if (length == 0) {
                System.out.println("Error: You have to enter a number greater than zero");
                return;
            }

        } else {
            System.out.println("Error: " + "\"" + input + "\"" +
                        " isn't a valid number.");
                return;
        }

        System.out.println("Input the number of possible symbols in the code:\n> ");
        if (!sc.hasNextInt()) {
            System.out.println("Error: enter a valid number");
        }
        int symbolsNum = sc.nextInt();
        sc.nextLine();

        if (symbolsNum < length) {
            System.out.println("Error: it's not possible to generate a code with a length of " +
                    length + " with " + symbolsNum + " unique symbols.");
            return;
        }

        if (symbolsNum > 36) {
            System.out.println("Error: maximum number of possible symbols in the " +
                    "code is 36 (0-9, a-z).");
            return;
        }

        //Generate the secrete code
        String secretCode = generateSecretCode(length, symbolsNum);

        String range = getSymbolRange(symbolsNum);
        System.out.println("The secret is prepared: " + "*".repeat(length) +
                " (" + range + ").");

        System.out.println("Okay, let's start the game!");

        // Step 3: Loop until the user guesses the secret code
        int turn = 1;
        while (true) {
            System.out.println("Turn " + turn + ":\n>");
            String guess = sc.nextLine();

            //Grade the guess
            int[] playerGuess = convertGuessToArray(guess);
            int[] secretCodeCard = convertSecretCodeToArray(secretCode);
            int[] result = gradeGuess(playerGuess, secretCodeCard);
            int bulls = result[0];
            int cows = result[1];

            //Print grade
            System.out.println("Grade: " + bulls + " bull" +
                    (bulls > 1 ? "s" : "") + " and " + cows +
                            " cow" + (cows > 1 ? "s" : ""));

            //Check if player wins
            if (bulls == length) {
                System.out.println("Congratulations! You guessed the secret code.");
                break;
            }
            turn++;
        }
        sc.close();
     }

     //Function to grade the player's guess
   private static int[] gradeGuess(int[] playerGuess, int[] secretCodeCard) {
       int bulls = 0;
       int cows = 0;

       Set<Integer> secretSet = new HashSet<>();

       //add secretCodeCard to a hashset for fast loop
       for (int digit : secretCodeCard) {
           secretSet.add(digit);
       }

       //Check for bulls and cows
       for (int i = 0; i < playerGuess.length; i++) {
           if (playerGuess[i] == secretCodeCard[i]) {
               bulls++; //Present in the same index
           } else if (secretSet.contains(playerGuess[i])) {
               cows++; //Present but in different index
           }
       }
       return new int[]{bulls, cows};
   }

     //Function to convert player guess(string) to int array
     private static int[] convertGuessToArray(String guess) {
        int[] guessArray = new int[guess.length()];
        for (int i = 0; i < guess.length(); i++) {
            guessArray[i] = Character.getNumericValue(guess.charAt(i));
        }
        return guessArray;
     }

    //Function to convert player secretCode(string) to int array
    private static int[] convertSecretCodeToArray(String secretCode) {
        int[] secretCodeArray = new int[secretCode.length()];
        for (int i = 0; i < secretCode.length(); i++) {
            secretCodeArray[i] = Character.getNumericValue(secretCode.charAt(i));
        }
        return secretCodeArray;
    }

     private static String generateSecretCode(int length, int symbolNum) {

         String chars = "0123456789abcdefghijklmnopqrstuvwxyz";
         String availableChars = chars.substring(0, symbolNum);

         StringBuilder code = new StringBuilder(length);
         Set<Character> uniqueChars = new HashSet<>();

         Random random = new Random();
         while (code.length() < length) {
             char ch = availableChars.charAt(random.nextInt(availableChars.length()));
             if (uniqueChars.add(ch)) {
                 code.append(ch);
             }
         }
         return code.toString();
     }

     private static String getSymbolRange(int symbolNum) {
        String chars = "0123456789abcdefghijklmnopqrstuvwxyz";
        String range = chars.substring(0, symbolNum);

        if ( symbolNum <= 10) {
            return range;
        } else {
            return "0-9, a-" + range.charAt(symbolNum - 1);
        }
     }
}
