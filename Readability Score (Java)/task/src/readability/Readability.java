package readability;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Readability {

    private final String text;
    private final List<String> sentences;
    private final List<String> words;
    private final int characterCount;
    private final int syllableCount;
    private final int polysyllableCount;

    // Scores
    private double ariScore;
    private double fkScore;
    private double smogScore;
    private double clScore;

    // Age/Grade Mappings
    private final ReadabilityScoreMapping scoreMapping;

    /**
     * Constructor: Reads text from file and calculates all necessary metrics.
     * @param filePath Path to the input text file.
     * @throws IOException If there's an error reading the file.
     */
    public Readability(String filePath) throws IOException {
        this.text = readTextFromFile(filePath);
        this.sentences = calculateSentences(this.text);
        this.words = calculateWords(this.sentences);
        this.characterCount = calculateCharacters(this.text);
        int[] syllableCounts = calculateSyllablesAndPolysyllables(this.words);
        this.syllableCount = syllableCounts[0];
        this.polysyllableCount = syllableCounts[1];
        this.scoreMapping = new ReadabilityScoreMapping(); // Initialise mappings

        // Calculate all scores upon initialisation
        calculateAllScores();
    }

    // --- Getters for Basic Metrics ---

    public String getText() {
        return text;
    }

    public int getSentenceCount() {
        return sentences.size();
    }

    public int getWordCount() {
        return words.size();
    }

    public int getCharacterCount() {
        return characterCount;
    }

    public int getSyllableCount() {
        return syllableCount;
    }

    public int getPolysyllableCount() {
        return polysyllableCount;
    }

    // --- Getters for Readability Scores ---

    public double getAriScore() {
        return ariScore;
    }

    public double getFkScore() {
        return fkScore;
    }

    public double getSmogScore() {
        return smogScore;
    }

    public double getClScore() {
        return clScore;
    }

    // --- Private Helper Methods for Calculation ---

    /**
     * Reads the entire content of a file into a string.
     * @param filePath Path to the file.
     * @return String content of the file.
     * @throws IOException If reading fails.
     */
    private String readTextFromFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(" "); // Append line and a space
            }
        }
        // Trim trailing space and normalise whitespace
        return sb.toString().trim().replaceAll("\\s+", " ");
    }

    /**
     * Splits text into sentences based on terminal punctuation.
     * @param text The input text.
     * @return A list of sentences.
     */
    private List<String> calculateSentences(String text) {
        List<String> sentenceList = new ArrayList<>();
        // Split by '.', '!', '?' followed by whitespace or end of string
        String[] potentialSentences = text.split("[.!?]+\\s*");
        for (String sentence : potentialSentences) {
            // Ensure sentence is not just whitespace
            if (!sentence.trim().isEmpty()) {
                sentenceList.add(sentence.trim());
            }
        }
        // Handle case where text might not end with punctuation
        if (sentenceList.isEmpty() && !text.trim().isEmpty()) {
            sentenceList.add(text.trim());
        }
        return sentenceList;
    }

    /**
     * Splits sentences into words based on whitespace.
     * @param sentences List of sentences.
     * @return A list of words.
     */
    private List<String> calculateWords(List<String> sentences) {
        List<String> wordList = new ArrayList<>();
        for (String sentence : sentences) {
            // Split by one or more whitespace characters
            String[] sentenceWords = sentence.trim().split("\\s+");
            for (String word : sentenceWords) {
                // Basic cleaning: remove leading/trailing non-alphanumeric if needed,
                // but keep punctuation within words for now (e.g., hyphenated)
                // A more robust approach might handle punctuation better.
                if (!word.isEmpty()) {
                    wordList.add(word);
                }
            }
        }
        return wordList;
    }

    /**
     * Counts non-whitespace characters in the text.
     * @param text The input text.
     * @return The total number of non-whitespace characters.
     */
    private int calculateCharacters(String text) {
        return text.replaceAll("\\s+", "").length();
    }

    /**
     * Counts syllables and polysyllables (3+ syllables) in a list of words.
     * Uses a more refined syllable counting logic.
     * @param words List of words.
     * @return An array where index 0 is total syllables, index 1 is total polysyllables.
     */
    private int[] calculateSyllablesAndPolysyllables(List<String> words) {
        int totalSyllables = 0;
        int totalPolysyllables = 0;
        for (String word : words) {
            int syllables = countSyllablesInWord(word);
            totalSyllables += syllables;
            if (syllables > 2) {
                totalPolysyllables++;
            }
        }
        return new int[]{totalSyllables, totalPolysyllables};
    }

    /**
     * Counts syllables in a single word using a common heuristic algorithm.
     * Handles common cases like silent 'e', vowel groups, etc.
     * @param word The word to count syllables in.
     * @return The estimated number of syllables.
     */
    private int countSyllablesInWord(String word) {
        if (word == null || word.isEmpty()) {
            return 0;
        }

        // 1. Lowercase and remove non-alphabetic characters at the end (like commas, periods)
        //    Keep internal hyphens for now. A more complex approach might split hyphenated words.
        word = word.toLowerCase().replaceAll("[^a-z]$", "");

        // 2. Handle special cases and common words (optional but can improve accuracy)
        //    e.g., if (word.equals(" Mmes.")) return 1;

        // 3. Count vowel groups (a, e, i, o, u, y)
        int syllableCount = 0;
        boolean lastWasVowel = false;
        Pattern vowelPattern = Pattern.compile("[aeiouy]");

        for (int i = 0; i < word.length(); i++) {
            Matcher matcher = vowelPattern.matcher(String.valueOf(word.charAt(i)));
            if (matcher.find()) {
                if (!lastWasVowel) {
                    syllableCount++;
                }
                lastWasVowel = true;
            } else {
                lastWasVowel = false;
            }
        }

        // 4. Adjustments
        //    - Subtract 1 if the word ends in "e" (silent e), unless it's the only vowel (like "the")
        if (word.endsWith("e") && syllableCount > 1 && !word.endsWith("le")) { // Avoid subtracting for words like 'apple' if 'le' forms a syllable
            // Check if the second to last char is not a vowel (handles 'ae', 'oe' etc. ending in 'e')
            if (word.length() > 1 && !vowelPattern.matcher(String.valueOf(word.charAt(word.length() - 2))).find()) {
                syllableCount--;
            }
        }
        //    - Handle words ending in "le" where 'l' acts as a vowel sound if preceded by a consonant.
        if (word.endsWith("le") && word.length() > 2) {
            char precedingChar = word.charAt(word.length() - 3);
            Matcher matcher = vowelPattern.matcher(String.valueOf(precedingChar));
            if (!matcher.find()) { // If preceded by a consonant
                // Check if the 'e' wasn't already counted as part of a vowel group
                if (!vowelPattern.matcher(String.valueOf(word.charAt(word.length()-2))).find()) {
                    // This logic can get complex; simple heuristic: add 1 if 'le' follows consonant and wasn't counted.
                    // A simpler rule might be needed, or rely on the initial vowel count.
                    // Let's stick to the initial count + silent 'e' rule for simplicity here.
                }
            }
        }


        // 5. Ensure at least one syllable for any word with letters.
        if (syllableCount == 0 && word.length() > 0) {
            syllableCount = 1;
        }

        return syllableCount;
    }


    /**
     * Calculates all readability scores.
     */
    private void calculateAllScores() {
        // Ensure division by zero doesn't happen
        int wordC = Math.max(1, getWordCount());
        int sentenceC = Math.max(1, getSentenceCount());

        // Automated Readability Index (ARI)
        this.ariScore = 4.71 * ((double) getCharacterCount() / wordC) +
                0.5 * ((double) wordC / sentenceC) - 21.43;

        // Flesch–Kincaid Readability Tests (FK)
        this.fkScore = 0.39 * ((double) wordC / sentenceC) +
                11.8 * ((double) getSyllableCount() / wordC) - 15.59;

        // Simple Measure of Gobbledygook (SMOG)
        // Note: SMOG requires at least 30 sentences for standard calculation.
        // This implementation applies the formula regardless, but accuracy may vary for short texts.
        if (getPolysyllableCount() > 0 && sentenceC > 0) {
            this.smogScore = 1.043 * Math.sqrt((double) getPolysyllableCount() * (30.0 / sentenceC)) + 3.1291;
        } else {
            this.smogScore = 0.0; // Or handle as undefined/error
        }


        // Coleman–Liau Index (CL)
        double avgLettersPer100Words = ((double) getCharacterCount() / wordC) * 100.0;
        double avgSentencesPer100Words = ((double) sentenceC / wordC) * 100.0;
        this.clScore = 0.0588 * avgLettersPer100Words - 0.296 * avgSentencesPer100Words - 15.8;
    }

    // --- Public Methods for Output ---

    /**
     * Prints the basic text statistics.
     */
    public void printStats() {
        System.out.println("The text is:");
        System.out.println(this.text);
        System.out.println("\nWords: " + getWordCount());
        System.out.println("Sentences: " + getSentenceCount());
        System.out.println("Characters: " + getCharacterCount());
        System.out.println("Syllables: " + getSyllableCount());
        System.out.println("Polysyllables: " + getPolysyllableCount());
    }

    /**
     * Calculates and prints the readability score and corresponding age for the chosen index.
     * @param indexType The type of index to calculate ("ARI", "FK", "SMOG", "CL", "all").
     */
    public void printReadabilityScore(String indexType) {
        System.out.println(); // Add a newline for better formatting

        double totalAge = 0;
        int scoreCount = 0;

        if ("ARI".equalsIgnoreCase(indexType) || "all".equalsIgnoreCase(indexType)) {
            ReadabilityScoreInfo ariInfo = scoreMapping.getInfoFromScore(ReadabilityScoreMapping.IndexType.ARI, this.ariScore);
            System.out.printf("Automated Readability Index: %.2f (about %d year-olds).%n", this.ariScore, ariInfo.getApproxAge());
            totalAge += ariInfo.getApproxAge();
            scoreCount++;
        }

        if ("FK".equalsIgnoreCase(indexType) || "all".equalsIgnoreCase(indexType)) {
            ReadabilityScoreInfo fkInfo = scoreMapping.getInfoFromScore(ReadabilityScoreMapping.IndexType.FK, this.fkScore);
            System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d year-olds).%n", this.fkScore, fkInfo.getApproxAge());
            totalAge += fkInfo.getApproxAge();
            scoreCount++;
        }

        if ("SMOG".equalsIgnoreCase(indexType) || "all".equalsIgnoreCase(indexType)) {
            ReadabilityScoreInfo smogInfo = scoreMapping.getInfoFromScore(ReadabilityScoreMapping.IndexType.SMOG, this.smogScore);
            System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d year-olds).%n", this.smogScore, smogInfo.getApproxAge());
            totalAge += smogInfo.getApproxAge();
            scoreCount++;
        }

        if ("CL".equalsIgnoreCase(indexType) || "all".equalsIgnoreCase(indexType)) {
            ReadabilityScoreInfo clInfo = scoreMapping.getInfoFromScore(ReadabilityScoreMapping.IndexType.CL, this.clScore);
            System.out.printf("Coleman–Liau index: %.2f (about %d year-olds).%n", this.clScore, clInfo.getApproxAge());
            totalAge += clInfo.getApproxAge();
            scoreCount++;
        }

        if ("all".equalsIgnoreCase(indexType) && scoreCount > 0) {
            double averageAge = totalAge / scoreCount;
            System.out.printf("%nThis text should be understood on average by %.2f year-olds.%n", averageAge);
        } else if (!"ARI".equalsIgnoreCase(indexType) && !"FK".equalsIgnoreCase(indexType) &&
                !"SMOG".equalsIgnoreCase(indexType) && !"CL".equalsIgnoreCase(indexType) &&
                !"all".equalsIgnoreCase(indexType)) {
            System.out.println("Error: Invalid index type specified.");
        }
    }
}