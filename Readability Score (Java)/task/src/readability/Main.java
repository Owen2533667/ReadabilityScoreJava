package readability;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // --- Input File Handling ---
        if (args.length == 0) {
            System.err.println("Error: Please provide the input file path as a command-line argument.");
            System.err.println("Usage: java readability.Main <path_to_file>");
            return; // Exit if no file path is given
        }
        String filePath = args[0];

        // --- Readability Calculation ---
        Readability readability;
        try {
            readability = new Readability(filePath);
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            System.err.println(e.getMessage());
            return; // Exit on file reading error
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during readability calculation:");
            e.printStackTrace(); // Print stack trace for debugging
            return;
        }

        // --- Print Basic Stats ---
        readability.printStats();

        // --- User Input for Score Type ---
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String indexType = scanner.nextLine().trim(); // Use nextLine and trim

        // --- Print Selected Readability Score(s) ---
        readability.printReadabilityScore(indexType);

        scanner.close(); // Close the scanner
    }
}
