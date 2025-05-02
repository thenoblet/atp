package gtp.atp.service;

import gtp.atp.exception.InvalidRegexException;
import gtp.atp.model.RegexHistory;
import gtp.atp.util.RegexUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages a collection of regular expression patterns and their usage history.
 * Provides functionality to track, retrieve, and analyze pattern usage statistics.
 */
public class RegexHistoryManager {
    private final Map<String, RegexHistory> regexHistoryMap = new HashMap<>();


    public Map<String, RegexHistory> getHistoryMap() {
        return Collections.unmodifiableMap(regexHistoryMap);
    }
    /**
     * Retrieves the usage history for a specific regular expression pattern.
     *
     * @param regex the regular expression pattern to look up
     * @return the RegexHistory object for the pattern, or null if not found
     */
    public RegexHistory getRegexHistory(String regex) {
        return regexHistoryMap.get(regex);
    }

    /**
     * Records usage of a regular expression pattern, creating new history if needed.
     * Validates the pattern syntax before recording.
     *
     * @param regex the regular expression pattern to record
     * @throws InvalidRegexException if the pattern syntax is invalid
     */
    public void recordPatternUsage(String regex) throws InvalidRegexException {
        RegexUtils.isValidRegex(regex);

        RegexHistory history = regexHistoryMap.get(regex);
        if (history == null) {
            history = new RegexHistory(regex);
            regexHistoryMap.put(regex, history);
        } else {
            history.incrementUsage();
        }
    }

    /**
     * Attempts to record pattern usage without throwing exceptions for invalid patterns.
     *
     * @param regex the regular expression pattern to record
     * @return the RegexHistory object if pattern is valid, null otherwise
     */
    public RegexHistory tryRecordPatternUsage(String regex) {
        System.out.println("Attempting to record pattern: " + regex); // Debug


        if (!RegexUtils.isRegexValid(regex)) {
            return null;
        }

        RegexHistory history = regexHistoryMap.get(regex);
        if (history == null) {
            System.out.println("Creating new history entry for: " + regex);
            history = new RegexHistory(regex);
            regexHistoryMap.put(regex, history);
        } else {
            System.out.println("Updating existing entry for: " + regex);
            history.incrementUsage();
        }

        return history;
    }

    /**
     * Adds a pre-constructed RegexHistory object to the manager.
     *
     * @param regex the regular expression pattern as key
     * @param regexHistory the RegexHistory object to add
     * @throws InvalidRegexException if the pattern syntax is invalid
     * @throws IllegalArgumentException if regexHistory is null
     */
    public void addRegexHistory(String regex, RegexHistory regexHistory) throws InvalidRegexException {
        if (regexHistory == null) {
            throw new IllegalArgumentException("regexHistory cannot be null");
        }

        RegexUtils.isValidRegex(regex);

        regexHistoryMap.put(regex, regexHistory);
    }

    /**
     * Removes a pattern's history from tracking.
     *
     * @param regex the regular expression pattern to remove
     */
    public void removeRegexHistory(String regex) {
        regexHistoryMap.remove(regex);
    }

    /**
     * Gets all tracked regex patterns and their histories.
     *
     * @return a list of all RegexHistory objects
     */
    public List<RegexHistory> getRegexHistoryList() {
        return new ArrayList<>(regexHistoryMap.values());
    }

    /**
     * Clears all pattern usage history.
     */
    public void clearRegexHistoryList() {
        regexHistoryMap.clear();
    }

    /**
     * Gets the most recently used patterns, ordered by most recent first.
     *
     * @param limit maximum number of recent patterns to return
     * @return list of recent RegexHistory objects
     * @throws IllegalArgumentException if limit is not positive
     */
    public List<RegexHistory> getRecentHistory(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("limit must be greater than 0");
        }

        if (limit > regexHistoryMap.size()) {
            limit = regexHistoryMap.size();
        }

        return regexHistoryMap.values()
                .stream()
                .sorted(Comparator.comparing(RegexHistory::getTimestamp).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public RegexHistory getLastUsed() {
        if (regexHistoryMap.isEmpty()) {
            return null;
        }
        return regexHistoryMap.values()
                .stream()
                .max(Comparator.comparing(RegexHistory::getTimestamp))
                .orElse(null);
    }

    /**
     * Gets all patterns containing the given substring
     * @param substring the text to search for in patterns
     * @return list of matching patterns sorted by most recent use
     * @throws IllegalArgumentException if substring is null or empty
     */
    public List<RegexHistory> searchPatterns(String substring) {
        if (substring == null || substring.trim().isEmpty()) {
            throw new IllegalArgumentException("Substring cannot be null or empty");
        }

        String searchTerm = substring.toLowerCase();
        return regexHistoryMap.values().stream()
                .filter(history -> history.getPattern().toLowerCase().contains(searchTerm))
                .sorted(Comparator.comparing(RegexHistory::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Gets most frequently used patterns.
     * - Returns empty list if no history exists.
     * - Returns a single-element list if one pattern has the highest count.
     * - Returns multiple patterns if they share the highest count.
     */
    public List<RegexHistory> getTopUsedHistory() {
        if (regexHistoryMap.isEmpty()) {
            return Collections.emptyList();
        }

        int maxUsage = regexHistoryMap.values()
                .stream()
                .mapToInt(RegexHistory::getUsageCount)
                .max()
                .orElse(0);

        return regexHistoryMap.values()
                .stream()
                .filter(history -> history.getUsageCount() == maxUsage)
                .collect(Collectors.toList());
    }
}
