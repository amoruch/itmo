// Flamethrower.java
package my_moves;

import ru.ifmo.se.pokemon.*;

public class Flamethrower extends SpecialMove {
    public Flamethrower() {
        super(Type.FIRE, 90, 1.0, 0, 1);
    }

    @Override
    public void applyOppEffects(Pokemon p) {
        Effect fire = new Effect().chance(0.1).turns(0).condition(Status.BURN);
	p.setCondition(fire);
    }

    @Override
    public String describe() {
        return "uses Flamethrower";
    }
}

