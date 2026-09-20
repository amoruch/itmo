package models;

import utility.*;

public class Coordinates implements Validatable {
    private long x; //Значение поля должно быть больше -319
    private Long y; //Значение поля должно быть больше -601, Поле не может быть null
    
    public Coordinates(long x, Long y) {
        this.x = x;
        this.y = y;
    }

    public boolean validate() {
        if (x <= -319) return false;
        if (y <= -601 || y == null) return false;
        return true;
    }

    @Override
    public String toString() {
        return x + ";" + y;
    }
}