package models;

import utility.*;

public class Chapter implements Validatable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private String world; //Поле не может быть null

    public Chapter(String name, String world) {
        this.name = name;
        this.world = world;
    }

    public boolean validate() {
        if (name == null || name.isEmpty()) return false;
        if (world == null) return false;
        return true;
    }

    @Override
    public String toString() {
        return name + ";" + world;
    }
}