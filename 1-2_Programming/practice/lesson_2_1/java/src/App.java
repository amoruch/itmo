
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Урок 2.1 — типы, generics и Collections Framework.
 */
public class App {

    public static void main(String[] args) {
        part1();
        part2();
        part3();
        part4();
        part5();
        part6();
    }

    private static void part1() {
        System.out.println("1. Типы и классы-обёртки");

        Integer boxed = 42;
        int unboxed = boxed;
        double asDouble = boxed.doubleValue();
        int year = Integer.parseInt("2026");

        char highSurrogate = '\uD83D';
        char lowSurrogate = '\uDE00';
        int codePoint = Character.toCodePoint(highSurrogate, lowSurrogate);

        System.out.printf("Integer: %d, int: %d, double: %.1f, год: %d%n",
                boxed, unboxed, asDouble, year);
        System.out.printf("Кодовая точка: U+%X, символ: %s%n",
                codePoint, new String(Character.toChars(codePoint)));
    }

    private static void part2() {
        System.out.println("\n2. Generic-классы и generic-методы");

        Box<String> textBox = new Box<>();
        textBox.put("Hello, generics!");
        String text = textBox.get();

        Pair<String, Integer> course = new Pair<>("Java", 2);
        int firstNumber = getFirst(List.of(10, 20, 30));

        System.out.println(text);
        System.out.printf("%s, урок %d, первый элемент: %d%n",
                course.first(), course.second(), firstNumber);
    }

    private static void part3() {
        System.out.println("\n3. Ограничения и PECS");

        List<Integer> integers = List.of(1, 2, 3);
        List<Number> destination = new ArrayList<>();
        copy(destination, integers);

        System.out.println("Сумма: " + sum(integers));
        System.out.println("Скопированные числа: " + destination);
    }

    private static void part4() {
        System.out.println("\n4. List и Iterator");

        List<String> subjects = new ArrayList<>(List.of("Java", "ООП", "Алгоритмы"));
        subjects.add(1, "Дискретная математика");
        subjects.set(0, "Программирование");

        Iterator<String> iterator = subjects.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().length() < 4) {
                iterator.remove();
            }
        }

        List<String> fixed = List.of("a", "b", "c");
        List<String> mutable = new ArrayList<>(fixed);
        mutable.add("d");
        System.out.println("List: " + subjects);
        System.out.println("Неизменяемый: " + fixed + "; изменяемый: " + mutable);
    }

    private static void part5() {
        System.out.println("\n5. Queue и Deque");

        Deque<String> deque = new ArrayDeque<>();
        deque.addFirst("начало");
        deque.addLast("конец");
        deque.push("стек");
        System.out.println("Deque: " + deque);
        System.out.println("poll: " + deque.poll() + "; pollLast: " + deque.pollLast());

        Queue<Integer> priorityQueue = new PriorityQueue<>();
        priorityQueue.offer(5);
        priorityQueue.offer(1);
        priorityQueue.offer(3);
        System.out.println("PriorityQueue.poll(): " + priorityQueue.poll());
    }

    private static void part6() {
        System.out.println("\n6. Set и Map");

        Set<String> hashSet = new HashSet<>(List.of("b", "a", "b"));
        Set<String> linkedSet = new LinkedHashSet<>(List.of("b", "a", "c"));
        Set<String> treeSet = new TreeSet<>(List.of("b", "a", "c"));
        System.out.println("HashSet: " + hashSet);
        System.out.println("LinkedHashSet: " + linkedSet);
        System.out.println("TreeSet: " + treeSet);

        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put("Java", 1);
        hashMap.putIfAbsent("ООП", 2);

        Map<String, Integer> linkedMap = new LinkedHashMap<>(hashMap);
        Map<String, Integer> treeMap = new TreeMap<>(hashMap);
        System.out.println("HashMap: " + hashMap);
        System.out.println("LinkedHashMap: " + linkedMap);
        System.out.println("TreeMap: " + treeMap);
    }

    private static <T> T getFirst(List<T> values) {
        return values.get(0);
    }

    private static <N extends Number> double sum(List<N> values) {
        double total = 0;
        for (Number value : values) {
            total += value.doubleValue();
        }
        return total;
    }

    private static <T> void copy(List<? super T> destination, List<? extends T> source) {
        destination.addAll(source);
    }

    private record Pair<T, U>(T first, U second) {

    }
}
