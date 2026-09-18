// Mood.java
package other;

public enum Mood {
    SAD("грустный"),
    CALM("спокойный"),
    CURIOUS("любопытный"),
    MISERY("несчастный");
    
    public String name;
    Mood(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}

