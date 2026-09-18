// Togekiss.java
package my_pokemons;

import ru.ifmo.se.pokemon.*;
import my_moves.*;

public class Togekiss extends Pokemon {
    public Togekiss(String name, int level) {
        super(name, level);
	setType(Type.FAIRY, Type.FLYING);
	setStats(85, 50, 95, 120, 115, 80);
	this.addMove(new Flamethrower());
	this.addMove(new Facade());
	this.addMove(new Charm());
	this.addMove(new Psychic());
    }
}

