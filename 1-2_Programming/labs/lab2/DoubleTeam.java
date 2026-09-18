// DoubleTeam.java
package my_moves;

import ru.ifmo.se.pokemon.*;

public class DoubleTeam extends StatusMove {
    public DoubleTeam() {
        super(Type.NORMAL, 0, 1.0, 0, 1);
    }

    @Override
    public void applySelfEffects(Pokemon p) {
        p.setMod(Stat.EVASION, 1);
    }

    @Override
    public String describe() {
        return "applies Double Team";
    }
}

