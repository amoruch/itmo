package com.example.server.managers;

import com.example.common.Model.SpaceMarine;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Owns the collection and its operations. Based on the CollectionManager from
 * lab 5.
 */
public final class CollectionManager {

    private final DumpManager dumpManager;
    private final Map<Integer, SpaceMarine> byId = new LinkedHashMap<>();
    private LinkedList<SpaceMarine> collection = new LinkedList<>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private int currentId;

    public CollectionManager(DumpManager dumpManager) {
        this.dumpManager = dumpManager;
    }

    public synchronized boolean init() {
        LinkedList<SpaceMarine> loaded = dumpManager.readCollection();
        collection = new LinkedList<>();
        byId.clear();
        currentId = 0;
        for (SpaceMarine marine : loaded) {
            if (marine == null || marine.id() <= 0 || byId.containsKey(marine.id())) {
                collection.clear();
                byId.clear();
                return false;
            }
            collection.add(marine);
            byId.put(marine.id(), marine);
            currentId = Math.max(currentId, marine.id());
        }
        sort();
        lastInitTime = LocalDateTime.now();
        return true;
    }

    public synchronized LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    public synchronized LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    public synchronized int size() {
        return collection.size();
    }

    public synchronized String collectionType() {
        return collection.getClass().getName();
    }

    public synchronized List<SpaceMarine> snapshot() {
        return collection.stream().sorted().collect(Collectors.toList());
    }

    public synchronized SpaceMarine byId(int id) {
        return byId.get(id);
    }

    public synchronized SpaceMarine add(SpaceMarine draft) {
        SpaceMarine marine = draft.withServerFields(++currentId, ZonedDateTime.now());
        collection.add(marine);
        byId.put(marine.id(), marine);
        sort();
        return marine;
    }

    public synchronized boolean update(int id, SpaceMarine draft) {
        SpaceMarine old = byId.get(id);
        if (old == null) {
            return false;
        }
        SpaceMarine updated = old.updatedPreservingServerFields(draft);
        collection.remove(old);
        collection.add(updated);
        byId.put(id, updated);
        sort();
        return true;
    }

    public synchronized boolean remove(long id) {
        SpaceMarine marine = byId.remove((int) id);
        if (marine == null) {
            return false;
        }
        collection.remove(marine);
        return true;
    }

    public synchronized void clear() {
        collection.clear();
        byId.clear();
    }

    public synchronized SpaceMarine removeFirst() {
        if (collection.isEmpty()) {
            return null;
        }
        SpaceMarine marine = collection.removeFirst();
        byId.remove(marine.id());
        return marine;
    }

    public synchronized SpaceMarine addIfMax(SpaceMarine draft) {
        SpaceMarine candidate = draft.withServerFields(currentId + 1, ZonedDateTime.now());
        Optional<SpaceMarine> maximum = collection.stream().max(Comparator.naturalOrder());
        if (maximum.isPresent() && candidate.compareTo(maximum.get()) <= 0) {
            return null;
        }
        currentId++;
        collection.add(candidate);
        byId.put(candidate.id(), candidate);
        sort();
        return candidate;
    }

    public synchronized int removeLower(SpaceMarine draft) {
        SpaceMarine comparison = draft.withServerFields(currentId + 1, ZonedDateTime.now());
        int before = collection.size();
        collection.removeIf(marine -> marine.compareTo(comparison) < 0);
        byId.entrySet().removeIf(entry -> entry.getValue().compareTo(comparison) < 0);
        return before - collection.size();
    }

    public synchronized SpaceMarine maxByHealth() {
        return collection.stream().max(Comparator.comparingDouble(SpaceMarine::health)).orElse(null);
    }

    public synchronized Map<LocalDate, Long> countByCreationDate() {
        return collection.stream().collect(Collectors.groupingBy(
                marine -> marine.creationDate().toLocalDate(), TreeMap::new, Collectors.counting()));
    }

    public synchronized List<SpaceMarine> descending() {
        return collection.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    public synchronized void saveCollection() throws IOException {
        dumpManager.writeCollection(new LinkedList<>(collection));
        lastSaveTime = LocalDateTime.now();
    }

    private void sort() {
        collection.sort(Comparator.naturalOrder());
    }
}
