package creational.prototype;

/**
 * Демонстрация глубокой копии изменяемого состояния.
 */
public final class PrototypeDemo {

    private PrototypeDemo() {
    }

    public static void run() {
        GameCharacter original = new GameCharacter("Игрок", new Position(10, 20));
        GameCharacter copy = original.copy();
        copy.moveTo(50, 70);

        System.out.println("original = " + original);
        System.out.println("copy = " + copy);
    }
}
