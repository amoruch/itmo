// Person.java
package people;

import places.*;
import other.*;
import food.*;

public abstract class Person {
    Mood mood;
    String name;
    Place location;
    String gender;

    public Person(String name, Mood mood, Place location, String gender) {
        this.name = name;
	this.mood = mood;
	this.location = location;
	this.gender = gender;
    }

    public void eat(Food food) {
        System.out.println(this.name + " ел " + food);
    }

    public void moveTo(Place place) {
	this.location = place;
    	System.out.println(this.name + " пошел в " + this.location);
    }

    public void changeMood(Mood mood) {
        this.mood = mood;
	System.out.println(this.name + " стал " + this.mood);
    }

    public void feel() {
        System.out.println(this.name + " чувствовала себя " + this.mood);
    }

    public String toString() {
        return this.mood + this.name;
    }
}

