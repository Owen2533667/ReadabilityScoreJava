package readability;

/**
 * Represents the age and grade level associated with a readability score range.
 */
public class ReadabilityScoreInfo {
    private final int lowerBoundAge;
    private final int upperBoundAge;
    private final String gradeLevel;

    /**
     * Constructor for ReadabilityScoreInfo.
     * @param lowerBoundAge The lower bound of the typical age range.
     * @param upperBoundAge The upper bound of the typical age range.
     * @param gradeLevel    A description of the corresponding grade level.
     */
    public ReadabilityScoreInfo(int lowerBoundAge, int upperBoundAge, String gradeLevel) {
        this.lowerBoundAge = lowerBoundAge;
        this.upperBoundAge = upperBoundAge;
        this.gradeLevel = gradeLevel;
    }

    // --- Getters ---

    public int getLowerBoundAge() {
        return lowerBoundAge;
    }

    public int getUpperBoundAge() {
        return upperBoundAge;
    }

    /**
     * Provides a representative approximate age, typically the upper bound.
     * @return Approximate age.
     */
    public int getApproxAge() {
        // Use upper bound as the representative age for simplicity in output
        return upperBoundAge;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    @Override
    public String toString() {
        return "ReadabilityScoreInfo{" +
                "lowerBoundAge=" + lowerBoundAge +
                ", upperBoundAge=" + upperBoundAge +
                ", gradeLevel='" + gradeLevel + '\'' +
                '}';
    }
}
