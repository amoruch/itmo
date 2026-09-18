// Container.java
package other;

public class Container implements Openable {
    public boolean isOpen;

    public Container() {
        isOpen = false;
    }

    public void open() {
        isOpen = true;
    }

    public void close() {
        isOpen = false;
    }

    public String toString() {
        return "коробка";
    }
}
