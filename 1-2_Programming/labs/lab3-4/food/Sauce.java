// Sauce.java
package food;

public class Sauce extends Food {
    String creator;
    double spiciness;

    public Sauce(String name, String creator) {
        this.name = name;
	this.creator = creator;
	this.spiciness = 0.8;
	this.calories = 200;
    }

    public String toString() {
	if (spiciness > 0.5)
            return "острый " + this.name + " " + this.creator;
	return this.name + " " + this.creator;
    }
}

