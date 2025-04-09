package readability;

/**
 * Represents the age and grade level associated with a readability score range.
 */
public record ReadabilityScoreInfo(int lowerBoundAge, int upperBoundAge, String gradeLevel) {
    /**
     * Constructor for ReadabilityScoreInfo.
     *
     * @param lowerBoundAge The lower bound of the typical age range.
     * @param upperBoundAge The upper bound of the typical age range.
     * @param gradeLevel    A description of the corresponding grade level.
     */
    public ReadabilityScoreInfo {
    }

    // --- Getters ---

    /**
     * Provides a representative approximate age, typically the upper bound.
     *
     * @return Approximate age.
     */
    public int getApproxAge() {
        // Use upper bound as the representative age for simplicity in output
        return upperBoundAge;
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
