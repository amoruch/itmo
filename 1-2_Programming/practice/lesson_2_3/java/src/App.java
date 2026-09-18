import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Урок 2.3 — шаблоны «Итератор» и «Строитель».
 */
public class App {

    public static void main(String[] args) {
        iteratorDemo();
        builderDemo();
    }

    // --- Шаблон «Итератор» ----------------------------------------------

    static void iteratorDemo() {
        // Проблема: нужен единообразный обход элементов коллекции,
        // не раскрывая её внутреннюю структуру (список / дерево / граф).

        // Iterable<T> — «можно перебирать». Метод: Iterator<T> iterator().
        Iterable<String> collection = List.of("1", "2", "3");

        // for-each работает благодаря Iterable:
        // компилятор разворачивает его в вызовы hasNext() / next().
        for (String s : collection) {
            System.out.println(s);
        }

        // Iterator<E> — сам объект-обходчик:
        //   boolean hasNext() — есть ли следующий элемент
        //   E next()          — вернуть следующий элемент
        //   void remove()     — удалить последний возвращённый (необязательно)
        Iterator<String> it = collection.iterator();
        while (it.hasNext()) {
            String s = it.next();
            System.out.println(s);
        }

        // remove() поддерживают не все коллекции;
        // у List.of() итератор immutable — remove() кинет UnsupportedOperationException.
        List<String> mutable = new ArrayList<>(List.of("a", null, "b"));
        Iterator<String> mit = mutable.iterator();
        while (mit.hasNext()) {
            if (mit.next() == null) mit.remove();
        }
        System.out.println(mutable); // [a, b]
    }

    // --- Шаблон «Строитель» (Builder) -----------------------------------

    static void builderDemo() {
        // Проблема: у объекта много полей, часть опциональна.
        // Телескопические конструкторы и сеттеры приводят к плохому коду.

        // Builder: строим объект пошагово, методы возвращают самого строителя,
        // в конце — терминальный метод, создающий продукт.
        Trip trip = new Trip.Builder()
                .setWhere("Moscow")
                .setTransport("train")
                .setDate(1, 7, 2023)
                .build();
        System.out.println(trip);

        // Классический пример из JDK — StringBuilder:
        String s = new StringBuilder()
                .append("Hello")
                .append(' ')
                .append("world")
                .toString();
        System.out.println(s);

        // Director (необязательный): инкапсулирует типовые сценарии сборки.
        Director d = new Director();
        System.out.println(d.makeTrainTrip("Moscow", "01.07.2023"));
    }

    /** Продукт — сложный объект со множеством полей. */
    static class Trip {
        private final String where;
        private final String transport;
        private final String date;

        private Trip(Builder b) {
            this.where = b.where;
            this.transport = b.transport;
            this.date = b.date;
        }

        @Override public String toString() {
            return "Trip(" + where + ", " + transport + ", " + date + ")";
        }

        /** Строитель: каждый set возвращает this для цепочки вызовов. */
        static class Builder {
            private String where;
            private String transport;
            private String date;

            Builder setWhere(String where)         { this.where = where; return this; }
            Builder setTransport(String t)         { this.transport = t; return this; }
            Builder setDate(int d, int m, int y)   {
                this.date = String.format("%02d.%02d.%04d", d, m, y);
                return this;
            }
            Trip build() { return new Trip(this); }
        }
    }

    /** Director управляет строителем — типовой сценарий сборки. */
    static class Director {
        Trip makeTrainTrip(String where, String date) {
            return new Trip.Builder()
                    .setWhere(where)
                    .setTransport("train")
                    .setDate(1, 7, 2023)
                    .build();
        }
    }
}
