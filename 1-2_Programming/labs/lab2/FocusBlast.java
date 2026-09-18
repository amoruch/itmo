// FocusBlast.java
package my_moves;

import ru.ifmo.se.pokemon.*;

public class FocusBlast extends SpecialMove {
    public FocusBlast() {
        super(Type.FIGHTING, 120, 0.7, 0, 1);
    }

    @Override
    public void applyOppEffects(Pokemon p) {
        if (Math.random() < 0.1) {
	    p.setMod(Stat.SPECIAL_DEFENSE, -1);
	}
    }

    @Override
    public String describe() {
        return "creates Focus Blast";
    }
}

