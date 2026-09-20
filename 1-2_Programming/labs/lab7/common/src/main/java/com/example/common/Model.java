package com.example.common;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Classes shared by the client and the server.
 */
public final class Model {

    private Model() {
    }

    public enum AstartesCategory {
        ASSAULT, SUPPRESSOR, TACTICAL, TERMINATOR, APOTHECARY
    }

    public enum MeleeWeapon {
        POWER_SWORD, MANREAPER, LIGHTING_CLAW, POWER_FIST
    }

    public static class Coordinates implements Serializable {

        private static final long serialVersionUID = 1L;
        private final long x;
        private final Long y;

        public Coordinates(long x, Long y) {
            if (x <= -319 || y == null || y <= -601) {
                throw new IllegalArgumentException("Coordinates must be x > -319 and y > -601");
            }
            this.x = x;
            this.y = y;
        }

        public long x() {
            return x;
        }

        public Long y() {
            return y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    public static class Chapter implements Serializable {

        private static final long serialVersionUID = 1L;
        private final String name;
        private final String world;

        public Chapter(String name, String world) {
            if (name == null || name.trim().isEmpty() || world == null) {
                throw new IllegalArgumentException("Chapter name and world are required");
            }
            this.name = name;
            this.world = world;
        }

        public String name() {
            return name;
        }

        public String world() {
            return world;
        }

        @Override
        public String toString() {
            return name + " (" + world + ")";
        }
    }

    public static class SpaceMarine implements Serializable, Comparable<SpaceMarine> {

        private static final long serialVersionUID = 1L;
        private final int id;
        private final String name;
        private final Coordinates coordinates;
        private final ZonedDateTime creationDate;
        private final double health;
        private final Boolean loyal;
        private final AstartesCategory category;
        private final MeleeWeapon meleeWeapon;
        private final Chapter chapter;

        public SpaceMarine(int id, String name, Coordinates coordinates,
                ZonedDateTime creationDate, double health, Boolean loyal,
                AstartesCategory category, MeleeWeapon meleeWeapon, Chapter chapter) {
            if (id <= 0 || name == null || name.trim().isEmpty() || coordinates == null
                    || creationDate == null || health <= 0 || loyal == null
                    || category == null || meleeWeapon == null) {
                throw new IllegalArgumentException("SpaceMarine contains invalid fields");
            }
            this.id = id;
            this.name = name;
            this.coordinates = coordinates;
            this.creationDate = creationDate;
            this.health = health;
            this.loyal = loyal;
            this.category = category;
            this.meleeWeapon = meleeWeapon;
            this.chapter = chapter;
        }

        public static SpaceMarine draft(String name, Coordinates coordinates, double health,
                Boolean loyal, AstartesCategory category,
                MeleeWeapon meleeWeapon, Chapter chapter) {
            return new SpaceMarine(1, name, coordinates, ZonedDateTime.now(), health, loyal,
                    category, meleeWeapon, chapter);
        }

        public int id() {
            return id;
        }

        public String name() {
            return name;
        }

        public Coordinates coordinates() {
            return coordinates;
        }

        public ZonedDateTime creationDate() {
            return creationDate;
        }

        public double health() {
            return health;
        }

        public Boolean loyal() {
            return loyal;
        }

        public AstartesCategory category() {
            return category;
        }

        public MeleeWeapon meleeWeapon() {
            return meleeWeapon;
        }

        public Chapter chapter() {
            return chapter;
        }

        public SpaceMarine withServerFields(int newId, ZonedDateTime newDate) {
            return new SpaceMarine(newId, name, coordinates, newDate, health, loyal,
                    category, meleeWeapon, chapter);
        }

        public SpaceMarine updatedPreservingServerFields(SpaceMarine draft) {
            return new SpaceMarine(id, draft.name, draft.coordinates, creationDate,
                    draft.health, draft.loyal, draft.category, draft.meleeWeapon, draft.chapter);
        }

        @Override
        public int compareTo(SpaceMarine other) {
            int byName = name.compareToIgnoreCase(other.name);
            return byName != 0 ? byName : Integer.compare(id, other.id);
        }

        @Override
        public String toString() {
            return "Space Marine {\n"
                    + " id: " + id + "\n"
                    + " name: " + name + "\n"
                    + " coordinates: " + coordinates + "\n"
                    + " creationDate: " + creationDate + "\n"
                    + " health: " + health + "\n"
                    + " loyal: " + loyal + "\n"
                    + " category: " + category + "\n"
                    + " meleeWeapon: " + meleeWeapon + "\n"
                    + " chapter: " + chapter + "\n}";
        }
    }
}
