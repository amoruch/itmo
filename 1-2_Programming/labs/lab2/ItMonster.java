// ItMonster.java
package pokemon;

import ru.ifmo.se.pokemon.*;
import move.*;

public class ItMonster extends Pokemon {

    public ItMonster(String name, int level) {
        super(name, level);
        setType(Type.PSYCHIC);
        setStats(50, 50, 50, 50, 50, 50);
        this.addMove(new NonClassicMove());
    }
}
