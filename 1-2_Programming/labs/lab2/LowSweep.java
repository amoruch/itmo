// LowSweep.java
package my_moves;

import ru.ifmo.se.pokemon.*;

public class LowSweep extends PhysicalMove {
    public LowSweep() {
        super(Type.FIGHTING, 65, 1.0, 0, 2);
    }

    @Override
    public void applyOppEffects(Pokemon p) {
        p.setMod(Stat.SPEED, -1);
    }

    @Override
    public String describe() {
        return "uses Low sweep";
    }
}

