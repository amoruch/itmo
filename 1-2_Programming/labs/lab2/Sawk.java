// Sawk.java
package my_pokemons;

import ru.ifmo.se.pokemon.*;
import my_moves.*;

public class Sawk extends Pokemon {
    public Sawk(String name, int level) {
	super(name, level);
	setType(Type.FIGHTING);
	setStats(75, 125, 75, 30, 75, 85);
	Swagger atk1 = new Swagger();
	setMove(new Swagger(), new Bulldoze(), new FocusEnergy(), new LowSweep());
    }
}

