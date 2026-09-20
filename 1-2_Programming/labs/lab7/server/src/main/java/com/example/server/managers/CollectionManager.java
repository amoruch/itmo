package com.example.server.managers;

import com.example.common.Model.SpaceMarine;
import com.example.server.utility.AuthenticatedUser;
import com.example.server.utility.MarineRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Keeps the working collection in memory and persists every mutation through
 * {@link MarineRepository}. Reading commands never query the database.
 */
public final class CollectionManager {

    private final MarineRepository marineRepository;
    private final ConcurrentMap<Integer, SpaceMarine> byId = new ConcurrentHashMap<>();
    private final ConcurrentSkipListSet<SpaceMarine> collection = new ConcurrentSkipListSet<>();
    private final ReentrantLock mutationLock = new ReentrantLock();
    private volatile LocalDateTime lastInitTime;
    private volatile LocalDateTime lastModificationTime;

    public CollectionManager(MarineRepository marineRepository) {
        this.marineRepository = marineRepository;
    }

    public boolean init() throws SQLException {
        List<SpaceMarine> loaded = marineRepository.loadAll();
        mutationLock.lock();
        try {
            collection.clear();
            byId.clear();
            for (SpaceMarine marine : loaded) {
                if (marine == null || byId.putIfAbsent(marine.id(), marine) != null
                        || !collection.add(marine)) {
                    collection.clear();
                    byId.clear();
                    return false;
                }
            }
            lastInitTime = LocalDateTime.now();
            return true;
        } finally {
            mutationLock.unlock();
        }
    }

    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    public LocalDateTime getLastModificationTime() {
        return lastModificationTime;
    }

    public int size() {
        return collection.size();
    }

    public String collectionType() {
        return collection.getClass().getName();
    }

    public List<SpaceMarine> snapshot() {
        return new ArrayList<>(collection);
    }

    public SpaceMarine byId(int id) {
        return byId.get(id);
    }

    public SpaceMarine first() {
        return collection.isEmpty() ? null : collection.first();
    }

    public SpaceMarine add(AuthenticatedUser user, SpaceMarine draft) throws SQLException {
        mutationLock.lock();
        try {
            SpaceMarine marine = marineRepository.insert(user, draft);
            addToMemory(marine);
            markModified();
            return marine;
        } finally {
            mutationLock.unlock();
        }
    }

    public boolean update(AuthenticatedUser user, int id, SpaceMarine draft) throws SQLException {
        mutationLock.lock();
        try {
            SpaceMarine old = byId.get(id);
            if (old == null || !marineRepository.update(user, id, draft)) {
                return false;
            }
            SpaceMarine updated = old.updatedPreservingServerFields(draft);
            collection.remove(old);
            collection.add(updated);
            byId.put(id, updated);
            markModified();
            return true;
        } finally {
            mutationLock.unlock();
        }
    }

    public boolean remove(AuthenticatedUser user, int id) throws SQLException {
        mutationLock.lock();
        try {
            if (!marineRepository.delete(user, id)) {
                return false;
            }
            removeFromMemory(id);
            markModified();
            return true;
        } finally {
            mutationLock.unlock();
        }
    }

    public int clear(AuthenticatedUser user) throws SQLException {
        mutationLock.lock();
        try {
            Set<Integer> deletedIds = marineRepository.deleteAllOwnedBy(user);
            deletedIds.forEach(this::removeFromMemory);
            if (!deletedIds.isEmpty()) {
                markModified();
            }
            return deletedIds.size();
        } finally {
            mutationLock.unlock();
        }
    }

    public SpaceMarine addIfMax(AuthenticatedUser user, SpaceMarine draft) throws SQLException {
        mutationLock.lock();
        try {
            SpaceMarine candidate = comparisonMarine(draft);
            Optional<SpaceMarine> maximum = collection.stream().max(Comparator.naturalOrder());
            if (maximum.isPresent() && candidate.compareTo(maximum.get()) <= 0) {
                return null;
            }
            SpaceMarine marine = marineRepository.insert(user, draft);
            addToMemory(marine);
            markModified();
            return marine;
        } finally {
            mutationLock.unlock();
        }
    }

    public int removeLower(AuthenticatedUser user, SpaceMarine draft) throws SQLException {
        mutationLock.lock();
        try {
            SpaceMarine comparison = comparisonMarine(draft);
            List<Integer> ids = collection.stream()
                    .filter(marine -> marine.compareTo(comparison) < 0)
                    .map(SpaceMarine::id)
                    .toList();
            Set<Integer> deletedIds = marineRepository.deleteOwnedIds(user, ids);
            deletedIds.forEach(this::removeFromMemory);
            if (!deletedIds.isEmpty()) {
                markModified();
            }
            return deletedIds.size();
        } finally {
            mutationLock.unlock();
        }
    }

    public SpaceMarine maxByHealth() {
        return collection.stream().max(Comparator.comparingDouble(SpaceMarine::health)).orElse(null);
    }

    public Map<LocalDate, Long> countByCreationDate() {
        return collection.stream().collect(Collectors.groupingBy(
                marine -> marine.creationDate().toLocalDate(), TreeMap::new, Collectors.counting()));
    }

    public List<SpaceMarine> descending() {
        return collection.stream().sorted(Comparator.reverseOrder()).toList();
    }

    private void addToMemory(SpaceMarine marine) {
        SpaceMarine previous = byId.putIfAbsent(marine.id(), marine);
        if (previous != null || !collection.add(marine)) {
            throw new IllegalStateException("Duplicate SpaceMarine id returned by the database: " + marine.id());
        }
    }

    private void removeFromMemory(int id) {
        SpaceMarine marine = byId.remove(id);
        if (marine != null) {
            collection.remove(marine);
        }
    }

    private SpaceMarine comparisonMarine(SpaceMarine draft) {
        int comparisonId = byId.keySet().stream().max(Integer::compareTo)
                .filter(id -> id < Integer.MAX_VALUE)
                .map(id -> id + 1)
                .orElse(Integer.MAX_VALUE);
        return draft.withServerFields(comparisonId, ZonedDateTime.now());
    }

    private void markModified() {
        lastModificationTime = LocalDateTime.now();
    }
}
