package gtp.atp.model;

import gtp.atp.exception.InvalidRegexException;
import gtp.atp.util.RegexUtils;

import java.util.Objects;

/**
 * Represents the result of a pattern matching operation, containing information about
 * the matched text, its location in the input, and the pattern used for matching.
 */
public class MatchResult {
    private String matchedText;
    private int startIndex;
    private int endIndex;
    private String patternUsed;

    /**
     * Constructs a new MatchResult with the specified matching details.
     *
     * @param matchedText the text that was matched
     * @param startIndex the starting index (inclusive) of the matched text in the input
     * @param endIndex the ending index (exclusive) of the matched text in the input
     * @param patternUsed the pattern that was used to find this match
     * @throws IllegalArgumentException if startIndex is negative or endIndex is less than startIndex
     */
    public MatchResult(String matchedText, int startIndex, int endIndex, String patternUsed) {
        this.matchedText = matchedText;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.patternUsed = patternUsed;
    }

    /**
     * Returns the matched text.
     *
     * @return the text that was matched
     */
    public String getMatchedText() {
        return matchedText;
    }

    /**
     * Sets the matched text.
     *
     * @param matchedText the new matched text to set
     */
    public void setMatchedText(String matchedText) {
        this.matchedText = matchedText;
    }

    /**
     * Returns the starting index of the match in the input text.
     *
     * @return the zero-based starting index (inclusive) of the match
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * Sets the starting index of the match.
     *
     * @param startIndex the new starting index to set
     * @throws IllegalArgumentException if startIndex is negative
     */
    public void setStartIndex(int startIndex) {
        if (startIndex < 0) {
            throw new IndexOutOfBoundsException("startIndex cannot be negative");
        }

        this.startIndex = startIndex;
    }

    /**
     * Returns the ending index of the match in the input text.
     *
     * @return the zero-based ending index (exclusive) of the match
     */
    public int getEndIndex() {
        return endIndex;
    }

    /**
     * Sets the ending index of the match.
     *
     * @param endIndex the new ending index to set
     * @throws IllegalArgumentException if endIndex is less than startIndex
     */
    public void setEndIndex(int endIndex) throws IndexOutOfBoundsException {
        if (endIndex > matchedText.length()) {
            throw new IndexOutOfBoundsException("End index out of bounds");
        }

        this.endIndex = endIndex;
    }

    /**
     * Returns the pattern that was used to find this match.
     *
     * @return the pattern string used for matching
     */
    public String getPatternUsed() {
        return patternUsed;
    }

    /**
     * Sets the pattern used for this match.
     *
     * @param patternUsed the new pattern string to set
     */
    public void setPatternUsed(String patternUsed) {
        try {
            if (RegexUtils.isValidRegex(patternUsed)) {
                this.patternUsed = patternUsed;
            }
        } catch (InvalidRegexException e) {
            System.err.println("Regex validation failed: " + e.getMessage());
        }
    }

    /**
     * Returns a string representation of this match result.
     * The format is: "Matched '[matchedText]' at [startIndex-endIndex] using pattern: [patternUsed]"
     *
     * @return a string representation of this match result
     */
    @Override
    public String toString() {
        return String.format(
                "Matched '%s' at [%d-%d] using pattern: %s", matchedText, startIndex, endIndex, patternUsed
        );
    }

    /**
     * Compares this match result to another object for equality.
     * Two match results are considered equal if they have the same matched text,
     * start index, end index, and pattern used.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MatchResult other = (MatchResult) o;

        return startIndex == other.startIndex
                && endIndex == other.endIndex
                && Objects.equals(matchedText, other.matchedText)
                && Objects.equals(patternUsed, other.patternUsed);
    }

    /**
     * Returns a hash code value for this match result based on all its fields.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(matchedText, startIndex, endIndex, patternUsed);
    }
}