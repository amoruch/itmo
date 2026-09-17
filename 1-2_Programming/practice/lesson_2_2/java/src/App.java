import java.util.ArrayList;
import java.util.List;

/**
 * Урок 2.2 — обобщения (generics).
 */
public class App {

    public static void main(String[] args) {
        // Тип передаётся классу как параметр: Box<String> и Box<Integer> — разные типы.
        Box<String> textBox = new Box<>();
        textBox.put("Hello, generics!");
        String text = textBox.get(); // Приведение (String) не требуется.
        System.out.println(text);

        Box<Integer> numberBox = new Box<>();
        numberBox.put(123);
        Integer number = numberBox.get();
        System.out.println(number);
        // textBox.put(123); // Ошибка компиляции — защита от неверного типа.

        Pair<String, Integer> course = new Pair<>("Java", 2);
        System.out.printf("%s, урок %d%n", course.key(), course.value());

        String firstWord = getFirst(List.of("один", "два", "три"));
        Integer firstNumber = getFirst(List.of(1, 2, 3));
        System.out.printf("Первые элементы: %s и %d%n", firstWord, firstNumber);

        NumberBox<Double> average = new NumberBox<>(4.5);
        System.out.println("Число: " + average.doubleValue());

        List<Integer> integers = List.of(1, 2, 3);
        System.out.println("Сумма: " + sum(integers));

        // PECS: Producer Extends, Consumer Super.
        List<Number> destination = new ArrayList<>();
        copy(destination, integers);
        System.out.println("Скопированные числа: " + destination);

        // При стирании типов информация о T удаляется после компиляции.
        // Поэтому нельзя написать new T(), new T[10] или obj instanceof Box<String>.
    }

    /** Контейнер, который хранит значение одного указанного типа. */
    public static class Box<T> {
        private T value;

        public void put(T value) {
            this.value = value;
        }

        public T get() {
            return value;
        }
    }

    /** Обобщённая пара; K — ключ, V — значение. */
    public record Pair<K, V>(K key, V value) {
    }

    /** Обобщённый метод работает со списком любого типа. */
    public static <T> T getFirst(List<T> list) {
        return list.get(0);
    }

    /** Ограничение extends Number открывает методы Number, например doubleValue(). */
    public static class NumberBox<T extends Number> {
        private final T value;

        public NumberBox(T value) {
            this.value = value;
        }

        public double doubleValue() {
            return value.doubleValue();
        }
    }

    /** extends — producer: из списка можно безопасно читать Number. */
    public static double sum(List<? extends Number> values) {
        double total = 0;
        for (Number value : values) {
            total += value.doubleValue();
        }
        return total;
    }

    /** super — consumer: в destination можно безопасно добавлять T. */
    public static <T> void copy(List<? super T> destination, List<? extends T> source) {
        destination.addAll(source);
    }
}
