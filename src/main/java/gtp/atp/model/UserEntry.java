package gtp.atp.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a user-defined data entry with key-value pairing and optional notes.
 * Each entry is uniquely identified by a UUID and can be used to store
 * customizable data fields with additional metadata.
 */
public class UserEntry {
    private UUID id;         // Unique identifier
    private String key;      // Custom label or field name
    private String value;    // The actual data value
    private String notes;    // Optional comment or user remark

    /**
     * Constructs a new UserEntry with all fields.
     *
     * @param id    the unique identifier for this entry (cannot be null)
     * @param key   the label/name for this entry (cannot be null or empty)
     * @param value the data value for this entry (cannot be null)
     * @param notes optional comments/remarks (may be null)
     * @throws IllegalArgumentException if id, key or value are invalid
     */
    public UserEntry(UUID id, String key, String value, String notes) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        this.id = id;
        this.key = key;
        this.value = value;
        this.notes = notes;
    }

    /**
     * Gets the unique identifier for this entry.
     * @return the entry's UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the label/name for this entry.
     * @return the entry's key
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the data value for this entry.
     * @return the entry's value
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the optional notes for this entry.
     * @return the entry's notes, or null if none exist
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets a new unique identifier for this entry.
     * @param id the new UUID (cannot be null)
     * @throws IllegalArgumentException if id is null
     */
    public void setId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        this.id = id;
    }

    /**
     * Sets a new label/name for this entry.
     * @param key the new key (cannot be null or empty)
     * @throws IllegalArgumentException if key is invalid
     */
    public void setKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        this.key = key;
    }

    /**
     * Sets a new data value for this entry.
     * @param value the new value (cannot be null)
     * @throws IllegalArgumentException if value is null
     */
    public void setValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        this.value = value;
    }

    /**
     * Sets optional notes for this entry.
     * @param notes the comments/remarks (may be null)
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Compares this entry with another object for equality.
     * Two entries are considered equal if they have the same ID.
     * @param o the object to compare with
     * @return true if the objects have the same ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntry)) return false;
        UserEntry that = (UserEntry) o;
        return Objects.equals(id, that.id);
    }

    /**
     * Returns a hash code based on the entry's ID.
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns a string representation of this entry.
     * Format: "UserEntry{id='[id]', key='[key]', value='[value]', notes='[notes]'}"
     * @return string representation of this entry
     */
    @Override
    public String toString() {
        return String.format("UserEntry{id='%s', key='%s', value='%s', notes='%s'}",
                id, key, value, notes);
    }
}