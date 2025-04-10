package readability;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String filePath = "";
        // --- Input File Handling ---
        if (args.length == 0) {
            System.err.println("Error: No input file path as a command-line argument.");
            System.err.println("Usage: java readability.Main <path_to_file>");
            filePath = "Readability Score (Java)/task/src/readability/in.txt";
        } else {
            filePath = args[0];
        }


        // --- Readability Calculation ---
        Readability readability;
        try {
            readability = new Readability(filePath);
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            System.err.println(e.getMessage());
            return;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during readability calculation:");
            e.printStackTrace();
            return;
        }

        // --- Print Basic Stats ---
        readability.printStats();

        // --- User Input for Score Type ---
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String indexType = scanner.nextLine().trim();

        // --- Print Selected Readability Score(s) ---
        readability.printReadabilityScore(indexType);

        scanner.close();
    }
}
