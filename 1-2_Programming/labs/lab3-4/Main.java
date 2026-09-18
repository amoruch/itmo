// Main.java

import people.*;
import other.*;
import places.*;
import food.*;

public class Main {
    public static void main(String[] args) {
	Kitchen kitchen = new Kitchen();
	Container box = new Container();
	Table table = new Table();

	Kid kid = new Kid();
	Adult pek = new Adult();
	Host frik = new Host();

	Food spice = frik.cookSauce();
	TVShow show = frik.tvShow;

	kid.open(box);
	kid.moveTo(kitchen);
	pek.sitAt(table);
	pek.eat(spice);
	frik.mourn(show);
	pek.praise(spice);
	frik.feel();
    }
}

