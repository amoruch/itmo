// Cinccino.java
package my_pokemons;

import ru.ifmo.se.pokemon.*;
import my_moves.*;

public class Cinccino extends Pokemon {
    public Cinccino(String name, int level) {
        super(name, level);
	setType(Type.NORMAL);
	setStats(75, 95, 60, 65, 60, 115);
	this.addMove(new Thunderbolt());
	this.addMove(new TailSlap());
	this.addMove(new DoubleTeam());
	this.addMove(new FocusBlast());
    }
}

