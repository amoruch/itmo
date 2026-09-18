// Main.java
package lab;

import ru.ifmo.se.pokemon.*;
import my_pokemons.*;
import my_moves.*;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();

	Sawk pok1 = new Sawk("Hawk Moth", 1);
	Minccino pok2 = new Minccino("Ladybug", 1);
	Cinccino pok3 = new Cinccino("Cat noir", 1);

        Togepi pok4 = new Togepi("Skywalker", 1);
	Togetic pok5 = new Togetic("Ghost", 1);
	Togekiss pok6 = new Togekiss("Kratos", 1);
	
	b.addAlly(pok1);
	b.addAlly(pok2);
	b.addAlly(pok3);

	b.addFoe(pok4);
	b.addFoe(pok5);
	b.addFoe(pok6);

	b.go();
    }
}

