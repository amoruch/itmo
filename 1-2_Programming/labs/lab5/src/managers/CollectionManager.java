package managers;

import java.time.*;
import java.util.*;
import models.*;

public class CollectionManager {
    private int currentId = 1;
    private Map<Integer, SpaceMarine> squad = new HashMap<>();
    private LinkedList<SpaceMarine> collection = new LinkedList<>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final DumpManager dumpManager;

    public CollectionManager(DumpManager dumpManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.dumpManager = dumpManager;
    }

    /**
     * @return Последнее время инициализации.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Последнее время сохранения.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return коллекция.
     */
    public LinkedList<SpaceMarine> getCollection() {
        return collection;
    }

    /**
     * Получить SpaceMarine по ID
     */
    public SpaceMarine byId(int id) {
        return squad.get(id);
    }

    /**
     * Содержит ли коллекции SpaceMarine
     */
    public boolean isContain(SpaceMarine unit) {
        return unit == null || byId(unit.getId()) != null;
    }

    /**
     * Получить свободный ID
     */
    public int getFreeId() {
        while (byId(++currentId) != null);
        return currentId;
    }

    /**
     * Добавляет SpaceMarine
     */
    public boolean add(SpaceMarine unit) {
        if (isContain(unit)) return false;
        squad.put(unit.getId(), unit);
        collection.add(unit);
        update();
        return true;
    }

    /**
     * Обновляет SpaceMarine
     */
    public boolean update(SpaceMarine unit) {
        if (!isContain(unit)) return false;
        collection.remove(byId(unit.getId()));
        squad.put(unit.getId(), unit);
        collection.add(unit);
        update();
        return true;
    }

    /**
     * Удаляет SpaceMarine по ID
     */
    public boolean remove(long id) {
        var a = byId((int) id);
        if (a == null) return false;
        squad.remove(a.getId());
        collection.remove(a);
        update();
        return true;
    }

    /**
     * Вырезать первый элемент коллекции
     */
    public SpaceMarine pop() {
        SpaceMarine unit = collection.pop();
        squad.remove(unit.getId());
        return unit;
    }

    /**
     * Удаляет первый элемент collection
     */
    public boolean removeFirst() {
        if (collection.isEmpty()) return false;
        SpaceMarine unit = collection.pop();
        var id = unit.getId();
        squad.remove(id);
        return true;
    }

    /**
     * Фиксирует изменения коллекции
     */
    public void update() {
        Collections.sort(collection);
    }

    public boolean init() {
        collection.clear();
        squad.clear();
        collection = dumpManager.readCollection();
        lastInitTime = LocalDateTime.now();
        for (var e : collection) {
            if (byId(e.getId()) != null) {
                collection.clear();
                squad.clear();
                return false;
            } else {
                if (e.getId() > currentId) currentId = e.getId();
                squad.put(e.getId(), e);
            }
        }
        update();
        return true;
    }

    /**
     * Сохраняет коллекцию в файл
     */
    public void saveCollection() {
        dumpManager.writeCollection(collection);
        lastSaveTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";

        StringBuilder info = new StringBuilder();
        for (var unit : collection) {
            info.append(unit + "\n\n");
        }
        return info.toString().trim();
    }
}
