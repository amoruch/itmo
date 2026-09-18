interface Flyable {

    void fly();

    default void land() {
        System.out.println("Посадка");
    }

    static void rules() {
        System.out.println("Flyable — контракт для летающих объектов");
    }
}

interface Swimmable {

    void swim();
}

class Duck implements Flyable, Swimmable {

    @Override
    public void fly() {
        System.out.println("Утка летит");
    }

    @Override
    public void swim() {
        System.out.println("Утка плывёт");
    }
}

class Airplane implements Flyable {

    @Override
    public void fly() {
        System.out.println("Самолёт летит");
    }
}