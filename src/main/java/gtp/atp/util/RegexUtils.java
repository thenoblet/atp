package gtp.atp.util;

import gtp.atp.exception.InvalidRegexException;

import java.util.ArrayList;
import java.util.List;
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
        if (regex == null || regex.trim().isEmpty()) {
            throw new NullPointerException("Regex pattern cannot be null or empty");
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
        if (regex == null || regex.trim().isEmpty()) {
            return false;
        }

        try {
            Pattern.compile(regex);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    /**
     * Tests if a regex pattern matches a given input string.
     *
     * @param regex the regular expression pattern
     * @param input the string to test against the pattern
     * @return true if the input matches the pattern
     * @throws InvalidRegexException if the pattern is invalid
     * @throws NullPointerException if either parameter is null
     */
    public static boolean matches(String regex, String input) throws InvalidRegexException {
        if (input == null) {
            throw new NullPointerException("Input string cannot be null");
        }

        if (isValidRegex(regex)) {
            return Pattern.matches(regex, input);
        }

        return false;
    }

    /**
     * Extracts all matches of a pattern from an input string.
     *
     * @param regex the regular expression pattern
     * @param input the string to extract matches from
     * @return a list of all matching strings
     * @throws InvalidRegexException if the pattern is invalid
     * @throws NullPointerException if either parameter is null
     */
    public static List<String> findAllMatches(String regex, String input) throws InvalidRegexException {
        if (input == null) {
            throw new NullPointerException("Input string cannot be null");
        }

        List<String> matches = new ArrayList<>();

        if (isValidRegex(regex)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);

            while (matcher.find()) {
                matches.add(matcher.group());
            }
        }

        return matches;
    }

    /**
     * Extracts the first match of a pattern from an input string.
     *
     * @param regex the regular expression pattern
     * @param input the string to extract a match from
     * @return the first matching string, or null if no match found
     * @throws InvalidRegexException if the pattern is invalid
     * @throws NullPointerException if either parameter is null
     */
    public static String findFirstMatch(String regex, String input) throws InvalidRegexException {
        if (input == null) {
            throw new NullPointerException("Input string cannot be null");
        }

        if (isValidRegex(regex)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                return matcher.group();
            }
        }

        return null;
    }

    /**
     * Replaces all occurrences of a pattern with a replacement string.
     *
     * @param regex the regular expression pattern
     * @param input the input string in which to perform replacement
     * @param replacement the replacement string
     * @return the resulting string after replacement
     * @throws InvalidRegexException if the pattern is invalid
     * @throws NullPointerException if any parameter is null
     */
    public static String replaceAll(String regex, String input, String replacement) throws InvalidRegexException {
        if (input == null) {
            throw new NullPointerException("Input string cannot be null");
        }
        if (replacement == null) {
            throw new NullPointerException("Replacement string cannot be null");
        }

        if (isValidRegex(regex)) {
            return Pattern.compile(regex).matcher(input).replaceAll(replacement);
        }

        return input; // Should never reach here because isValidRegex throws exception
    }


    /**
     * Splits a string around matches of the given pattern.
     *
     * @param regex the regular expression pattern to split by
     * @param input the string to split
     * @return an array of strings computed by splitting the input around matches of the pattern
     * @throws InvalidRegexException if the pattern is invalid
     * @throws NullPointerException if either parameter is null
     */
    public static String[] split(String regex, String input) throws InvalidRegexException {
        if (input == null) {
            throw new NullPointerException("Input string cannot be null");
        }

        if (isValidRegex(regex)) {
            return input.split(regex);
        }

        return new String[] { input }; // Should never reach here because isValidRegex throws exception
    }

    /**
     * Counts the number of matches of a pattern in an input string.
     *
     * @param regex the regular expression pattern
     * @param input the string to count matches in
     * @return the number of matches found
     * @throws InvalidRegexException if the pattern is invalid
     * @throws NullPointerException if either parameter is null
     */
    public static int countMatches(String regex, String input) throws InvalidRegexException {
        if (input == null) {
            throw new NullPointerException("Input string cannot be null");
        }

        int count = 0;

        if (isValidRegex(regex)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);

            while (matcher.find()) {
                count++;
            }
        }

        return count;
    }

}