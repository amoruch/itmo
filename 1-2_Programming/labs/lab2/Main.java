// Main.java
package lab2;

import ru.ifmo.se.pokemon.*;
import pokemon.*;
import move.*;

public class Main {

    public static void main(String[] args) {
        Battle b = new Battle();
        Pokemon p1 = new Pokemon("Чужой", 1);
        Pokemon p2 = new Pokemon("Хищник", 1);
        b.addAlly(new ItMonster("Монстрик", 5));
        b.addFoe(p1);
        b.addFoe(p2);
        b.go();
    }
}
