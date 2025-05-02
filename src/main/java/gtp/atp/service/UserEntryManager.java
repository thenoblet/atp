package gtp.atp.service;

import gtp.atp.model.UserEntry;

import java.util.*;

public class UserEntryManager {
    private final Map<UUID, UserEntry> entryMap;

    public UserEntryManager() {
        this.entryMap = new LinkedHashMap<>();
    }

    public boolean addEntry(UserEntry entry) {
        if (entryMap.containsKey(entry.getId())) {
            return false;
        }

        entryMap.put(entry.getId(), entry);
        return true;
    }

    public UserEntry getEntry(UUID id) {
        return entryMap.get(id);
    }

    public boolean updateEntry(UUID id, String newKey, String newValue, String newNotes) {
        UserEntry existing = entryMap.get(id);
        if (existing == null)
            return false;

        existing.setKey(newKey);
        existing.setValue(newValue);
        existing.setNotes(newNotes);
        return true;
    }

    public boolean deleteEntry(UUID id) {
        return entryMap.remove(id) != null;
    }

    public List<UserEntry> getAllEntries() {
        return new ArrayList<>(entryMap.values());
    }

    public List<UserEntry> search(String keyword) {
        List<UserEntry> results = new ArrayList<>();
        for (UserEntry entry : entryMap.values()) {
            if (entry.getKey().toLowerCase().contains(keyword.toLowerCase())
                    || entry.getValue().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(entry);
            }
        }
        return results;
    }

    public int count() {
        return entryMap.size();
    }

    public void clear() {
        entryMap.clear();
    }
}