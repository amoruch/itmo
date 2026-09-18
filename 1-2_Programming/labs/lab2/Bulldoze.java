// Bulldoze.java
package my_moves;

import ru.ifmo.se.pokemon.*;

public class Bulldoze extends PhysicalMove {
    public Bulldoze() {
        super(Type.GROUND, 60, 1.0, 0, 2);
    }

    @Override
    public void applyOppEffects(Pokemon p) {	
        p.setMod(Stat.SPEED, -1);
    }

    @Override
    public String describe() {
        return "applies Bulldoze";
    }
}

