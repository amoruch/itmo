// Facade.java
package my_moves;

import ru.ifmo.se.pokemon.*;

public class Facade extends PhysicalMove {
    public Facade() {
        super(Type.NORMAL, 70, 1.0, 0, 2);
    }

    @Override
    public void applyOppDamage(Pokemon p, double damage) {
	Status cond = p.getCondition();
        if (cond == Status.BURN || cond == Status.POISON || cond == Status.PARALYZE) {
	    p.setMod(Stat.HP, (int) Math.round(damage));
	}
	p.setMod(Stat.HP, (int) Math.round(damage));
    }

    @Override
    public String describe() {
        return "uses Facade";
    }
}

