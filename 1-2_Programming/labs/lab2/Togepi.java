// Togepi.java
package my_pokemons;

import ru.ifmo.se.pokemon.*;
import my_moves.*;

public class Togepi extends Pokemon {

    public Togepi(String name, int level) {
        super(name, level);
	setType(Type.FAIRY);
	setStats(35, 20, 65, 40, 65, 20);
        this.addMove(new Flamethrower());
	this.addMove(new Facade());
    }
}

