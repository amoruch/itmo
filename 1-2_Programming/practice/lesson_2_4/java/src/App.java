import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Урок 2.4 — сравнение элементов и сортировка.
 */
public class App {

    public static void main(String[] args) {
        // Оба интерфейса возвращают int: <0 (меньше), 0 (равно), >0 (больше).

        // Comparable<T> — естественный порядок, реализуется самим классом.
        // Метод: int compareTo(T other)
        List<String> words = new ArrayList<>(List.of("banana", "apple", "cherry"));
        words.sort(null); // null => использовать Comparable
        System.out.println(words); // [apple, banana, cherry]

        // Свой класс реализует Comparable<Person>.
        List<Person> people = new ArrayList<>(List.of(
                new Person("Bob"), new Person("Alice"), new Person("Carol")));
        people.sort(null);
        System.out.println(people); // по name

        // Comparator<T> — внешний порядок, класс менять не нужно.
        // Метод: int compare(T a, T b)
        words.sort(new LengthComparator());
        System.out.println(words); // по длине

        // Способы задать компаратор:
        // 1) отдельный класс
        // 2) static nested class
        // 3) inner class (outer.new Inner())
        // 4) local class внутри метода
        // 5) анонимный класс
        words.sort(new Comparator<String>() {
            @Override public int compare(String a, String b) {
                return a.length() - b.length();
            }
        });
        // 6) лямбда — самый короткий вариант
        words.sort((a, b) -> a.length() - b.length());

        // Стандартные методы сортировки:
        //   List.sort(Comparator)         — для списков, если null — Comparable;
        //   Arrays.sort(T[])              — для массивов объектов;
        //   Arrays.sort(int[])            — для примитивов.
        // Для объектов используется TimSort — сортировка устойчивая (stable):
        // равные элементы сохраняют относительный порядок.
        Integer[] ints = { 3, 1, 2 };
        Arrays.sort(ints);
        System.out.println(Arrays.toString(ints));
    }

    /** Класс с естественным порядком. */
    static class Person implements Comparable<Person> {
        private final String name;
        Person(String name) { this.name = name; }
        @Override public int compareTo(Person other) {
            return name.compareTo(other.name);
        }
        @Override public String toString() { return name; }
    }

    /** Внешний компаратор по длине строки. */
    static class LengthComparator implements Comparator<String> {
        @Override public int compare(String a, String b) {
            return a.length() - b.length();
        }
    }
}
