// Thunderbolt.java
package my_moves;

import ru.ifmo.se.pokemon.*;

public class Thunderbolt extends SpecialMove {
    public Thunderbolt() {
        super(Type.ELECTRIC, 90, 1.0, 0, 1);
    }

    @Override
    public void applyOppEffects(Pokemon p) {
        Effect paralyze = new Effect().chance(0.1).turns(0).condition(Status.PARALYZE);
	p.setCondition(paralyze);
    }

    @Override
    public String describe() {
        return "creates Thunderbolt";
    }
}

