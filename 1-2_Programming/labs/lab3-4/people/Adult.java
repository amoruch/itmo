// Adult.java
package people;

import places.*;
import other.*;
import food.*;

public class Adult extends Person {
    public Adult(String name, Mood mood, Place place, String gender) {
        super(name, mood, place, gender);
    }
	
    public Adult() {
        super("Мистер Пек", Mood.CALM, new Kitchen(), "Male");
    }

    public void sitAt(Object obj) {
        System.out.println(this.name + " сидел за " + obj);
    }

    public void praise(Object obj) {
        System.out.println(this.name + " хвалил " + obj);
    }

    public Food cookSauce() {
        return new Sauce("соус", this.name);
    }

    public void mourn(Object obj) {
        System.out.println(this.name + " оплакивала " + obj);
    }
}

