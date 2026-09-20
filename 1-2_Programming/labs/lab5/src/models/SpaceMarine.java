package models;

import java.io.Serializable;
import java.util.Objects;
import utility.*;

public class SpaceMarine extends Element implements Validatable, Serializable {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private double health; //Значение поля должно быть больше 0
    private Boolean loyal; //Поле не может быть null
    private AstartesCategory category; //Поле не может быть null
    private MeleeWeapon meleeWeapon; //Поле не может быть null
    private Chapter chapter; //Поле может быть null

    public SpaceMarine(int id, String name, Coordinates coordinates, java.time.ZonedDateTime creationDate, double health, Boolean loyal,
                        AstartesCategory category, MeleeWeapon meleeWeapon, Chapter chapter) {
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

    public boolean validate() {
        if (id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null) return false;
        if (creationDate == null) return false;
        if (health <= 0) return false;
        if (loyal == null) return false;
        if (category == null) return false;
        if (meleeWeapon == null) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Space Marine {\n id: " + id + "\n name: " + name + "\n coordinates: " + coordinates + "\n creationDate: " + creationDate +
        "\n health: " + health + "\n loyal: " + loyal + "\n category: " + category + "\n meleeWeapon: " + meleeWeapon + "\n chapter: " + chapter + "\n}\n";
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public java.time.ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(java.time.ZonedDateTime newCreationDate) {
        creationDate = newCreationDate; // костыль для update, кроме него нигде не используется
    }

    public double getHealth() {
        return health;
    }

    public Boolean getLoyal() {
        return loyal;
    }

    public AstartesCategory getCategory() {
        return category;
    }

    public MeleeWeapon getMeleeWeapon() {
        return meleeWeapon;
    }

    public Chapter getChapter() {
        return chapter;
    }

    @Override
    public int compareTo(Element element) {
        return (int)(this.id - element.getId());
    }

    public int compareTo(SpaceMarine element) {
        return (this.health > element.getHealth()) ? 1 : -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpaceMarine that = (SpaceMarine) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, health, loyal, category, meleeWeapon, chapter);
    }
}
