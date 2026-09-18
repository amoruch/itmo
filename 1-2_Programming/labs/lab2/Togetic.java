// Togetic.java
package my_pokemons;

import ru.ifmo.se.pokemon.*;
import my_moves.*;

public class Togetic extends Pokemon {
    public Togetic(String name, int level) {
        super(name, level);
	setType(Type.FAIRY, Type.FLYING);
	setStats(55, 40, 85, 80, 105, 40);
	this.addMove(new Flamethrower());
	this.addMove(new Facade());
	this.addMove(new Charm());
    }
}

