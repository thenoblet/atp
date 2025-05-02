package gtp.atp.service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class TextAnalyzer {
    public Map<String, Long> wordFrequency(String input) {
        return Arrays.stream(input.toLowerCase().split("\\W+"))
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
    }

    public String summarizeText(String input, int wordLimit) {
        String[] words = input.split("\\s+");
        return Arrays.stream(words).limit(wordLimit).collect(Collectors.joining(" ")) + "...";
    }
}
