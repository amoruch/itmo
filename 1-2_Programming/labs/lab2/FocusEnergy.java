// FocusEnergy.java
package my_moves;

import ru.ifmo.se.pokemon.*;

public class FocusEnergy extends StatusMove {
    public FocusEnergy() {
        super(Type.NORMAL, 0, 1.0, 0, 3);
    }

    @Override
    public void applySelfEffects(Pokemon p) {
        p.setMod(Stat.ACCURACY, 2);
    }

    @Override
    public String describe() {
        return "focuses energy";
    }
}

