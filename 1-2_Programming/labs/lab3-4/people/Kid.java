// Kid.java
package people;

import places.*;
import other.*;
import food.*;

public class Kid extends Person {
    public Kid() {
        super("Малыш", Mood.CURIOUS, new Kitchen(), "Male");
    }

    public void watch(Person person) {
        System.out.println(this.name + " наблюдал за " + person.name);
    }

    public void open(Openable obj) {
	try {
	    obj.open();
	    System.out.println(this.name + " открыл " + obj);
	} catch (Exception e) {
	    System.out.println(this.name + " не смог открыть " + obj);
	}
    }
}

