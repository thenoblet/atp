package gtp.atp.util;

import gtp.atp.exception.InvalidRegexException;
import java.util.regex.*;

/**
 * A utility class providing helper methods for working with regular expressions.
 */
public final class RegexUtils {

    private RegexUtils() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Validates whether a given string is a valid regular expression pattern.
     *
     * @param regex the regular expression pattern to validate
     * @return true if the pattern is syntactically valid
     * @throws InvalidRegexException if the pattern is invalid
     * @throws NullPointerException if the input regex string is null
     */
    public static boolean isValidRegex(String regex) throws InvalidRegexException {
        if (regex == null) {
            throw new NullPointerException("Regex pattern cannot be null");
        }
        try {
            Pattern.compile(regex);
            return true;
        } catch (PatternSyntaxException e) {
            throw new InvalidRegexException(regex, e);
        }
    }

    /**
     * Alternative version that returns false instead of throwing exception for invalid patterns
     * Used for simple boolean checks
     *
     * @param regex the regular expression pattern to validate
     * @return true if valid, false if invalid
     * @throws NullPointerException if the input regex string is null
     */
    public static boolean isRegexValid(String regex) {
        if (regex == null) {
            throw new NullPointerException("Regex pattern cannot be null");
        }
        try {
            Pattern.compile(regex);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }
}