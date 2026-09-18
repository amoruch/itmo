// Swagger.java
package my_moves;

import ru.ifmo.se.pokemon.*;

public class Swagger extends StatusMove {
    public Swagger() {
        super(Type.NORMAL, 0, 0.85, 0, 1);
    }

    @Override
    public void applyOppEffects(Pokemon p) {	
        p.confuse();
	p.setMod(Stat.ATTACK, 2);
    }

    @Override
    public String describe() {
        return "applies Swagger";
    }
}

