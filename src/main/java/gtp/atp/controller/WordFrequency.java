package gtp.atp.controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class WordFrequency {
    private final StringProperty word;
    private final IntegerProperty count;

    public WordFrequency(String word, int count) {
        this.word = new SimpleStringProperty(word);
        this.count = new SimpleIntegerProperty(count);
    }

    public StringProperty wordProperty() { return word; }
    public IntegerProperty countProperty() { return count; }

    public String getWord() { return word.get(); }
    public int getCount() { return count.get(); }
}