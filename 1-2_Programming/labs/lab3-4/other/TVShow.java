// TVShow.java
package other;

public class TVShow {
    String name, owner;
    boolean isSuccessful;

    public TVShow(String name, String owner) {
	this.name = name;
	this.owner = owner;
	this.isSuccessful = false;
    }

    public String toString() {
        return "провал " + name + " " + owner;
    }
}

