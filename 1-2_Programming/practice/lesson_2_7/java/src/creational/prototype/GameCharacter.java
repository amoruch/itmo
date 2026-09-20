package creational.prototype;

/**
 * Прототип игрового персонажа.
 */
public final class GameCharacter {

    private final String name;
    private final Position position;

    public GameCharacter(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    public GameCharacter copy() {
        return new GameCharacter(name, position.copy());
    }

    public void moveTo(int x, int y) {
        position.moveTo(x, y);
    }

    @Override
    public String toString() {
        return name + " at " + position;
    }
}
