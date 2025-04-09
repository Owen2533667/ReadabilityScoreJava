package readability;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Manages the mapping between readability scores (ARI, FK, SMOG, CL)
 * and their corresponding age/grade levels using ReadabilityScoreInfo.
 */
public class ReadabilityScoreMapping {

    // Using NavigableMap to easily find the appropriate range based on score
    private final NavigableMap<Double, ReadabilityScoreInfo> ariMap;
    private final NavigableMap<Double, ReadabilityScoreInfo> fkMap;
    private final NavigableMap<Double, ReadabilityScoreInfo> smogMap;
    private final NavigableMap<Double, ReadabilityScoreInfo> clMap;

    /**
     * Enum to define the different index types.
     */
    public enum IndexType {
        ARI, FK, SMOG, CL
    }

    /**
     * Constructor: Initialises the score-to-age/grade mappings for all indices.
     */
    public ReadabilityScoreMapping() {
        ariMap = new TreeMap<>();
        fkMap = new TreeMap<>();
        smogMap = new TreeMap<>();
        clMap = new TreeMap<>();

        initialiseAriMap();
        initialiseFkMap();
        initialiseSmogMap();
        initialiseClMap();
    }

    // --- Initialisation Methods ---

    private void initialiseAriMap() {
        // Score thresholds represent the *start* of the range for that age/grade
        // Based on typical ARI score interpretations
        ariMap.put(1.0, new ReadabilityScoreInfo(5, 6, "Kindergarten"));
        ariMap.put(2.0, new ReadabilityScoreInfo(6, 7, "First Grade"));
        ariMap.put(3.0, new ReadabilityScoreInfo(7, 8, "Second Grade"));
        ariMap.put(4.0, new ReadabilityScoreInfo(8, 9, "Third Grade"));
        ariMap.put(5.0, new ReadabilityScoreInfo(9, 10, "Fourth Grade"));
        ariMap.put(6.0, new ReadabilityScoreInfo(10, 11, "Fifth Grade"));
        ariMap.put(7.0, new ReadabilityScoreInfo(11, 12, "Sixth Grade"));
        ariMap.put(8.0, new ReadabilityScoreInfo(12, 13, "Seventh Grade"));
        ariMap.put(9.0, new ReadabilityScoreInfo(13, 14, "Eighth Grade"));
        ariMap.put(10.0, new ReadabilityScoreInfo(14, 15, "Ninth Grade"));
        ariMap.put(11.0, new ReadabilityScoreInfo(15, 16, "Tenth Grade"));
        ariMap.put(12.0, new ReadabilityScoreInfo(16, 17, "Eleventh Grade"));
        ariMap.put(13.0, new ReadabilityScoreInfo(17, 18, "Twelfth Grade"));
        ariMap.put(14.0, new ReadabilityScoreInfo(18, 24, "College student"));
        ariMap.put(15.0, new ReadabilityScoreInfo(24, 99, "Professor"));
    }

    private void initialiseFkMap() {
        // Flesch-Kincaid Grade Level mapping (Score roughly corresponds to US grade level)
        // We map grade level to age range
        fkMap.put(1.0, new ReadabilityScoreInfo(6, 7, "First Grade"));
        fkMap.put(2.0, new ReadabilityScoreInfo(7, 8, "Second Grade"));
        fkMap.put(3.0, new ReadabilityScoreInfo(8, 9, "Third Grade"));
        fkMap.put(4.0, new ReadabilityScoreInfo(9, 10, "Fourth Grade"));
        fkMap.put(5.0, new ReadabilityScoreInfo(10, 11, "Fifth Grade"));
        fkMap.put(6.0, new ReadabilityScoreInfo(11, 12, "Sixth Grade"));
        fkMap.put(7.0, new ReadabilityScoreInfo(12, 13, "Seventh Grade"));
        fkMap.put(8.0, new ReadabilityScoreInfo(13, 14, "Eighth Grade"));
        fkMap.put(9.0, new ReadabilityScoreInfo(14, 15, "Ninth Grade"));
        fkMap.put(10.0, new ReadabilityScoreInfo(15, 16, "Tenth Grade"));
        fkMap.put(11.0, new ReadabilityScoreInfo(16, 17, "Eleventh Grade"));
        fkMap.put(12.0, new ReadabilityScoreInfo(17, 18, "Twelfth Grade"));
        fkMap.put(13.0, new ReadabilityScoreInfo(18, 24, "College student"));
        // Add an entry for scores above 13 (or adjust range)
        fkMap.put(16.0, new ReadabilityScoreInfo(24, 99, "Professor")); // Scores can go higher
    }

    private void initialiseSmogMap() {
        // SMOG Index mapping (Score roughly corresponds to US grade level)
        // Similar mapping to FK
        smogMap.put(1.0, new ReadabilityScoreInfo(6, 7, "First Grade"));
        smogMap.put(2.0, new ReadabilityScoreInfo(7, 8, "Second Grade"));
        smogMap.put(3.0, new ReadabilityScoreInfo(8, 9, "Third Grade"));
        smogMap.put(4.0, new ReadabilityScoreInfo(9, 10, "Fourth Grade"));
        smogMap.put(5.0, new ReadabilityScoreInfo(10, 11, "Fifth Grade"));
        smogMap.put(6.0, new ReadabilityScoreInfo(11, 12, "Sixth Grade"));
        smogMap.put(7.0, new ReadabilityScoreInfo(12, 13, "Seventh Grade"));
        smogMap.put(8.0, new ReadabilityScoreInfo(13, 14, "Eighth Grade"));
        smogMap.put(9.0, new ReadabilityScoreInfo(14, 15, "Ninth Grade"));
        smogMap.put(10.0, new ReadabilityScoreInfo(15, 16, "Tenth Grade"));
        smogMap.put(11.0, new ReadabilityScoreInfo(16, 17, "Eleventh Grade"));
        smogMap.put(12.0, new ReadabilityScoreInfo(17, 18, "Twelfth Grade"));
        smogMap.put(13.0, new ReadabilityScoreInfo(18, 24, "College student"));
        smogMap.put(16.0, new ReadabilityScoreInfo(24, 99, "Professor")); // Or Graduate level
    }

    private void initialiseClMap() {
        // Coleman-Liau Index mapping (Score roughly corresponds to US grade level)
        // Similar mapping to FK and SMOG
        clMap.put(1.0, new ReadabilityScoreInfo(6, 7, "First Grade"));
        clMap.put(2.0, new ReadabilityScoreInfo(7, 8, "Second Grade"));
        clMap.put(3.0, new ReadabilityScoreInfo(8, 9, "Third Grade"));
        clMap.put(4.0, new ReadabilityScoreInfo(9, 10, "Fourth Grade"));
        clMap.put(5.0, new ReadabilityScoreInfo(10, 11, "Fifth Grade"));
        clMap.put(6.0, new ReadabilityScoreInfo(11, 12, "Sixth Grade"));
        clMap.put(7.0, new ReadabilityScoreInfo(12, 13, "Seventh Grade"));
        clMap.put(8.0, new ReadabilityScoreInfo(13, 14, "Eighth Grade"));
        clMap.put(9.0, new ReadabilityScoreInfo(14, 15, "Ninth Grade"));
        clMap.put(10.0, new ReadabilityScoreInfo(15, 16, "Tenth Grade"));
        clMap.put(11.0, new ReadabilityScoreInfo(16, 17, "Eleventh Grade"));
        clMap.put(12.0, new ReadabilityScoreInfo(17, 18, "Twelfth Grade"));
        clMap.put(13.0, new ReadabilityScoreInfo(18, 24, "College student"));
        clMap.put(16.0, new ReadabilityScoreInfo(24, 99, "Professor")); // Or Graduate level
    }


    // --- Public Access Method ---

    /**
     * Gets the ReadabilityScoreInfo (age/grade) corresponding to a given score
     * for a specific index type.
     *
     * @param type  The IndexType (ARI, FK, SMOG, CL).
     * @param score The calculated readability score.
     * @return The corresponding ReadabilityScoreInfo, or a default/highest if score is out of range.
     */
    public ReadabilityScoreInfo getInfoFromScore(IndexType type, double score) {
        NavigableMap<Double, ReadabilityScoreInfo> map = switch (type) {
            case ARI -> ariMap;
            case FK -> fkMap;
            case SMOG -> smogMap;
            case CL -> clMap;
            default -> throw new IllegalArgumentException("Invalid IndexType specified.");
        };

        // Find the entry whose key is less than or equal to the score
        var entry = map.floorEntry(score);

        if (entry != null) {
            return entry.getValue();
        } else {
            // If score is below the lowest threshold, return the lowest entry's info
            // Or handle as undefined/error depending on requirements
            var firstEntry = map.firstEntry();
            return (firstEntry != null) ? firstEntry.getValue() : new ReadabilityScoreInfo(0, 0, "Undefined"); // Fallback
        }
    }
}
