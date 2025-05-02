package gtp.atp.service;

import gtp.atp.exception.InvalidRegexException;
import gtp.atp.model.RegexHistory;
import gtp.atp.util.RegexUtils;

import java.util.*;

/**
 * Processes regular expression operations including pattern matching, replacement,
 * and history management. Maintains a history of used patterns and their usage statistics.
 */
public class RegexProcessor {
    private final String regex;
    public final RegexHistoryManager historyManager;

    /**
     * Constructs a RegexProcessor with default history manager.
     *
     * @param regex the regular expression pattern to use (cannot be null or empty)
     * @throws IllegalArgumentException if regex is null or empty
     */
    public RegexProcessor(String regex) {
        this(regex, new RegexHistoryManager());
    }

    /**
     * Constructs a RegexProcessor with specified history manager.
     *
     * @param pattern the regular expression pattern to use (cannot be null or empty)
     * @param historyManager the history manager to track pattern usage (cannot be null)
     * @throws IllegalArgumentException if pattern is null or empty
     * @throws NullPointerException if historyManager is null
     */
    public RegexProcessor(String pattern, RegexHistoryManager historyManager) {
        if (pattern == null || pattern.trim().isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be null or empty");
        }
        this.regex = pattern;
        this.historyManager = Objects.requireNonNull(historyManager, "History manager cannot be null");
    }

    /**
     * Finds all matches of the regex pattern in the input string and records pattern usage.
     *
     * @param input the string to search in (cannot be null)
     * @return list of matching strings, empty list if no matches found or pattern is invalid
     * @throws NullPointerException if input is null
     *
     * @example
     * RegexProcessor processor = new RegexProcessor("\\d+");
     * List<String> matches = processor.findMatchesAndRecord("abc123def456");
     * // Returns ["123", "456"]
     */
    public List<String> findMatchesAndRecord(String input) {
        Objects.requireNonNull(input, "Input string cannot be null");

        try {
            historyManager.recordPatternUsage(this.regex);
            return RegexUtils.findAllMatches(this.regex, input);
        } catch (InvalidRegexException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Replaces all occurrences of the regex pattern in the input string with replacement text.
     *
     * @param input the string to perform replacement on (cannot be null)
     * @param replacement the replacement string (cannot be null)
     * @return the resulting string after replacement, or null if pattern is invalid
     * @throws NullPointerException if input or replacement is null
     */
    public String findAndReplace(String input, String replacement) {
        Objects.requireNonNull(input, "Input string cannot be null");
        Objects.requireNonNull(replacement, "Replacement string cannot be null");

        try {
            historyManager.recordPatternUsage(regex);
            return RegexUtils.replaceAll(regex, input, replacement);
        } catch (InvalidRegexException e) {
            return null;
        }
    }

    /**
     * Searches pattern history for entries containing the specified substring.
     *
     * @param substring the substring to search for (cannot be null or empty)
     * @return list of matching RegexHistory objects
     * @throws IllegalArgumentException if substring is null or empty
     */
    public List<RegexHistory> searchPatternHistory(String substring) {
        if (substring == null || substring.trim().isEmpty()) {
            throw new IllegalArgumentException("Substring cannot be null or empty");
        }
        return historyManager.searchPatterns(substring);
    }

    /**
     * Gets the most recently used regex patterns.
     *
     * @param maxResults maximum number of results to return (must be positive)
     * @return list of recent RegexHistory objects, ordered by most recent first
     * @throws IllegalArgumentException if maxResults is not positive
     */
    public List<RegexHistory> getRecentHistory(int maxResults) {
        if (maxResults <= 0) {
            throw new IllegalArgumentException("Max results must be positive");
        }
        return historyManager.getRecentHistory(maxResults);
    }
}