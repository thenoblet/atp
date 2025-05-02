package gtp.atp;

import gtp.atp.model.RegexHistory;
import gtp.atp.service.RegexProcessor;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Regex Processor Test Suite ===");

        try {
            // Section 1: Basic Functionality Tests
            System.out.println("\n--- SECTION 1: Basic Pattern Matching ---");
            testPatternMatching();

            // Section 2: History Tracking Tests
            System.out.println("\n--- SECTION 2: History Tracking ---");
            testHistoryTracking();

            // Section 3: Advanced Search Tests
            System.out.println("\n--- SECTION 3: Pattern Searching ---");
            testPatternSearching();

            System.out.println("\nAll tests completed successfully!");
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testPatternMatching() {
        System.out.println("\nTEST GROUP 1: Valid Pattern Matching");
        RegexProcessor digitProcessor = new RegexProcessor("\\d+");
        testMatch(digitProcessor, "abc123def456", "Should find 2 digit sequences");

        RegexProcessor emailProcessor = new RegexProcessor("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
        testMatch(emailProcessor, "Contact: test@example.com, support@company.org",
                "Should find 2 email addresses");

        System.out.println("\nTEST GROUP 2: Invalid Pattern Handling");
        RegexProcessor invalidProcessor = new RegexProcessor("[a-z");
        testMatch(invalidProcessor, "test", "Should handle invalid pattern gracefully");
    }

    private static void testMatch(RegexProcessor processor, String input, String description) {
        System.out.println("\nTest: " + description);
        System.out.println("Input: '" + input + "'");

        List<String> matches = processor.findMatchesAndRecord(input);

        if (matches.isEmpty()) {
            System.out.println("RESULT: No matches found");
        } else {
            System.out.println("RESULT: Found " + matches.size() + " matches:");
            matches.forEach(match -> System.out.println(" - " + match));
        }
    }

    private static void testHistoryTracking() {
        System.out.println("\nTEST GROUP 1: Basic History Tracking");
        RegexProcessor processor = new RegexProcessor("initial");

        String[] testPatterns = {
                "\\d+",
                "[A-Za-z]+",
                "\\w+@\\w+\\.\\w+",
                "\\d{3}-\\d{2}-\\d{4}",
                "[A-Z][a-z]+"
        };

        for (String pattern : testPatterns) {
            processor = new RegexProcessor(pattern, processor.historyManager);
            processor.findMatchesAndRecord("test input");
            System.out.println("Recorded usage of pattern: " + pattern);
        }

        System.out.println("\nTEST GROUP 2: History Retrieval");
        List<RegexHistory> recent = processor.getRecentHistory(3);
        System.out.println("Most recent 3 patterns:");
        recent.forEach(history ->
                System.out.printf(" - %s (used %d times, last at %s)%n",
                        history.getPattern(),
                        history.getUsageCount(),
                        history.getTimestamp())
        );
    }

    private static void testPatternSearching() {
        System.out.println("\nTEST GROUP 1: Basic Pattern Search");
        RegexProcessor processor = createProcessorWithHistory();

        testPatternSearch(processor, "\\d", "Should find digit-related patterns");
        testPatternSearch(processor, "word", "Should find word-related patterns");
        testPatternSearch(processor, "email", "Should find email patterns");
        testPatternSearch(processor, "a-z]+", "Should find patterns with character classes");
    }

    private static RegexProcessor createProcessorWithHistory() {
        RegexProcessor processor = new RegexProcessor("initial");
        String[] patterns = {
                "word",
                "sash73@##$#@!!!!@$%^&^&**(",
                "\\d+",
                "\\b\\w+\\b",
                "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}",
                "\\d{3}-\\d{2}-\\d{4}",
                "[A-Z][a-z]+"
        };

        for (String pattern : patterns) {
            processor = new RegexProcessor(pattern, processor.historyManager);
            for (int i = 0; i < 3; i++) {
                processor.findMatchesAndRecord("test input");
                try { Thread.sleep(10); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }
        return processor;
    }

    private static void testPatternSearch(RegexProcessor processor, String searchTerm, String description) {
        System.out.println("\nTest: " + description);
        System.out.println("Search term: '" + searchTerm + "'");

        List<RegexHistory> results = processor.searchPatternHistory(searchTerm);

        if (results.isEmpty()) {
            System.out.println("RESULT: No matching patterns found");
        } else {
            System.out.println("RESULT: Found " + results.size() + " matching patterns:");
            results.forEach(history ->
                    System.out.printf(" - %s (used %d times, last at %s)%n",
                            history.getPattern(),
                            history.getUsageCount(),
                            history.getTimestamp())
            );
        }
    }
}