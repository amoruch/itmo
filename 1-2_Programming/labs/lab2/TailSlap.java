// TailSlap.java
package my_moves;

import ru.ifmo.se.pokemon.*;

public class TailSlap extends PhysicalMove {
    public TailSlap() {
        super(Type.NORMAL, 25, 0.85, 0, 1);
    }

    @Override
    public void applyOppDamage(Pokemon p, double damage) {
        for (int i = 0; i < 5; i++) {
	    if (Math.random() < 0.5) {
	        p.setMod(Stat.HP, (int) Math.round(damage));
	    }
	    else {
	        continue;
	    }
	}
    }

    @Override
    public String describe() {
        return "gives opponent tail slap";
    }
}

