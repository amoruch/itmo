// Minccino.java
package my_pokemons;

import ru.ifmo.se.pokemon.*;
import my_moves.*;

public class Minccino extends Pokemon {
    public Minccino(String name, int level) {
        super(name, level);
	setType(Type.NORMAL);
	setStats(55, 50, 40, 40, 40, 75);
	this.addMove(new Thunderbolt());
	this.addMove(new TailSlap());
	this.addMove(new DoubleTeam());
    }
}

