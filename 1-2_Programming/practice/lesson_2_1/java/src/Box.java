/** Контейнер для одного значения заданного типа. */
class Box<T> {

    private T value;

    void put(T value) {
        this.value = value;
    }

    T get() {
        return value;
    }
}