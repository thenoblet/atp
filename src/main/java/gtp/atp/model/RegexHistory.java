package gtp.atp.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents the usage history of a regular expression pattern, tracking:
 * - The pattern itself
 * - How many times it has been used
 * - When it was last used
 *
 * This class uses JavaFX properties to enable easy binding with UI components.
 */
public class RegexHistory {
    private final StringProperty pattern = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> timestamp = new SimpleObjectProperty<>();
    private final IntegerProperty usageCount = new SimpleIntegerProperty();

    /**
     * Constructs a new RegexHistory with specified values.
     *
     * @param pattern the regular expression pattern (cannot be null or empty)
     * @param usageCount the initial usage count (cannot be negative)
     * @param timestamp the timestamp of last usage (cannot be null)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public RegexHistory(String pattern, int usageCount, LocalDateTime timestamp) {
        if (pattern == null || pattern.trim().isEmpty()) {
            throw new IllegalArgumentException("pattern cannot be null or empty");
        }
        if (usageCount < 0) {
            throw new IllegalArgumentException("usageCount cannot be negative");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp cannot be null");
        }

        this.pattern.set(pattern);
        this.usageCount.set(usageCount);
        this.timestamp.set(timestamp);
    }

    /**
     * Convenience constructor that initializes with:
     * - Current timestamp
     * - Usage count of 1
     *
     * @param pattern the regular expression pattern (cannot be null or empty)
     * @throws IllegalArgumentException if pattern is invalid
     */
    public RegexHistory(String pattern) {
        this(pattern, 1, LocalDateTime.now());
    }

    /**
     * Gets the pattern property for JavaFX binding.
     * @return the StringProperty for the regex pattern
     */
    public StringProperty patternProperty() {
        return pattern;
    }

    /**
     * Gets the timestamp property for JavaFX binding.
     * @return the ObjectProperty for the last usage timestamp
     */
    public ObjectProperty<LocalDateTime> timestampProperty() {
        return timestamp;
    }

    /**
     * Gets the usage count property for JavaFX binding.
     * @return the IntegerProperty for the usage count
     */
    public IntegerProperty usageCountProperty() {
        return usageCount;
    }

    // Regular getters
    /**
     * Gets the regular expression pattern.
     * @return the pattern string
     */
    public String getPattern() {
        return pattern.get();
    }

    /**
     * Gets the timestamp of last usage.
     * @return the LocalDateTime of last usage
     */
    public LocalDateTime getTimestamp() {
        return timestamp.get();
    }

    /**
     * Gets the current usage count.
     * @return the number of times this pattern has been used
     */
    public int getUsageCount() {
        return usageCount.get();
    }

    // Setters
    /**
     * Sets the regular expression pattern.
     * @param pattern the new pattern (cannot be null or empty)
     * @throws IllegalArgumentException if pattern is invalid
     */
    public void setPattern(String pattern) {
        if (pattern == null || pattern.trim().isEmpty()) {
            throw new IllegalArgumentException("pattern cannot be null or empty");
        }
        this.pattern.set(pattern);
    }

    /**
     * Sets the usage count.
     * @param usageCount the new usage count (cannot be negative)
     * @throws IllegalArgumentException if usageCount is negative
     */
    public void setUsageCount(int usageCount) {
        if (usageCount < 0) {
            throw new IllegalArgumentException("usageCount cannot be negative");
        }
        this.usageCount.set(usageCount);
    }

    /**
     * Updates the timestamp to the current time.
     */
    public void updateTimestamp() {
        this.timestamp.set(LocalDateTime.now());
    }

    /**
     * Increments the usage count by 1 and updates the timestamp.
     */
    public void incrementUsage() {
        this.usageCount.set(this.usageCount.get() + 1);
        updateTimestamp();
    }

    /**
     * Returns a string representation in format:
     * "Pattern: [pattern], UsageCount: [count], Timestamp: [timestamp]"
     * @return formatted string representation
     */
    @Override
    public String toString() {
        return String.format(
                "Pattern: %s, UsageCount: %d, Timestamp: %s",
                getPattern(), getUsageCount(), getTimestamp()
        );
    }

    /**
     * Compares this history entry with another object for equality.
     * Two entries are equal if they have the same pattern, usage count and timestamp.
     * @param o the object to compare with
     * @return true if the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegexHistory that = (RegexHistory) o;
        return getUsageCount() == that.getUsageCount() &&
                Objects.equals(getPattern(), that.getPattern()) &&
                Objects.equals(getTimestamp(), that.getTimestamp());
    }

    /**
     * Returns a hash code based on all fields.
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getPattern(), getUsageCount(), getTimestamp());
    }
}