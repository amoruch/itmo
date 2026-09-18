import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Урок 2.22 — Stream API.
 */
public class App {

    public static void main(String[] args) {
        pipelineAndSources();
        intermediateOps();
        terminalOps();
        optionalDemo();
        collectorsDemo();
        matchesDemo();
        lazyDemo();
    }

    /** Конвейер и источники. */
    static void pipelineAndSources() {
        // Конвейер = источник → промежуточные операции (0+) → завершающая (1).
        // Отличия от коллекции: элементы не хранятся; неявная итерация;
        // ленивость; источник не меняется; может быть бесконечным.

        // Источники:
        Stream<String> s1 = List.of("a", "b").stream();
        Stream<String> s2 = Stream.of("a", "b");
        IntStream ints = Arrays.stream(new int[]{ 1, 2, 3 });
        IntStream range = IntStream.range(0, 10);
        Stream<String> lines = Stream.empty();
        Stream<Double> gen = Stream.generate(Math::random);        // бесконечный
        Stream<Integer> iter = Stream.iterate(1, x -> x + 1);      // бесконечный
        var builder = Stream.<String>builder().add("a").add("b").build();

        // Промежуточные — возвращают новый Stream, ленивы.
        // Завершающие — запускают конвейер, возвращают результат или void.
    }

    /** Промежуточные операции. */
    static void intermediateOps() {
        var list = List.of(1, 2, 3, 4, 5, 6);

        // filter — Predicate, оставить подходящие.
        // map — Function, преобразовать элементы.
        // flatMap — Function<T, Stream<R>>, «развернуть» вложенное.
        // peek — Consumer, побочное действие (для отладки).
        // distinct / sorted / limit / skip — состояние (stateful).
        var result = list.stream()
                .filter(x -> x % 2 == 0)
                .map(x -> x * 10)
                .peek(x -> {})         // debug
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(0)
                .limit(10)
                .toList();

        System.out.println(result);

        // flatMap на вложенных списках:
        var nested = List.of(List.of(1, 2), List.of(3, 4));
        var flat = nested.stream().flatMap(List::stream).toList();
        System.out.println(flat);

        // Stateless: filter, map, flatMap, peek.
        // Stateful: distinct, sorted, limit, skip — зависят от других элементов.
    }

    /** Завершающие операции. */
    static void terminalOps() {
        var nums = IntStream.of(3, 1, 4, 1, 5);

        nums.forEach(System.out::println);                 // порядок не гарантирован
        // nums.forEachOrdered(...) — гарантирует порядок.
        // count(), sum(), min(), max(), average().

        // reduce — свернуть в одно значение.
        int prod = IntStream.rangeClosed(1, 5).reduce(1, (a, b) -> a * b);
        System.out.println("prod=" + prod);

        // collect — собрать в коллекцию / строку и т. п.
        List<Integer> list = Stream.of(1, 2, 3).collect(Collectors.toList());
        // Java 16+: toList() прямо на Stream.

        // toArray — в массив.
        Integer[] arr = Stream.of(1, 2, 3).toArray(Integer[]::new);
    }

    /** Optional<T> — оболочка «есть / нет значение». */
    static void optionalDemo() {
        Optional<String> empty = Optional.empty();
        Optional<String> value = Optional.of("x");

        System.out.println(empty.isPresent() + " " + value.isPresent());
        System.out.println(value.get());
        System.out.println(empty.orElse("default"));
        value.ifPresent(System.out::println);

        // В Stream: findFirst / findAny / min / max возвращают Optional.
        // В IntStream — OptionalInt с getAsInt() и т. п.
        OptionalInt min = IntStream.of(3, 1, 4).min();
        System.out.println(min.getAsInt());
    }

    /** Collectors — стандартные сборщики. */
    static void collectorsDemo() {
        var words = List.of("apple", "banana", "cherry", "avocado");

        // toList / toSet / toCollection.
        // toMap(k, v, merge, factory), joining(delim, pref, suff).
        // counting / summingInt / averagingInt / minBy / maxBy / reducing.
        // groupingBy(classifier), partitioningBy(predicate).

        System.out.println(words.stream().collect(Collectors.joining(", ", "[", "]")));

        Map<Integer, List<String>> byLen = words.stream()
                .collect(Collectors.groupingBy(String::length));
        System.out.println(byLen);

        Map<Boolean, List<String>> partitioned = words.stream()
                .collect(Collectors.partitioningBy(w -> w.startsWith("a")));
        System.out.println(partitioned);

        long count = words.stream().collect(Collectors.counting());
        System.out.println("count=" + count);
    }

    /** Короткие замыкания: anyMatch / allMatch / noneMatch. */
    static void matchesDemo() {
        var nums = List.of(1, 2, 3, 4, 5);
        System.out.println(nums.stream().anyMatch(x -> x > 4));   // true, стоп на первом
        System.out.println(nums.stream().allMatch(x -> x > 0));   // true, стоп на первом false
        System.out.println(nums.stream().noneMatch(x -> x < 0));  // true
    }

    /** Ленивость: конвейер не запускается до завершающей операции. */
    static void lazyDemo() {
        var list = new ArrayList<>(List.of(1, 2, 3));
        var stream = list.stream()
                .peek(System.out::println)             // при создании НЕ выполняется
                .filter(i -> i % 2 == 0)
                .peek(i -> System.out.println("> " + i));

        System.out.println("Ready");                   // сюда попадаем сразу
        list.add(4);                                   // источники видят изменения
        long c = stream.count();                       // здесь запускается конвейер
        System.out.println("Count: " + c);
        // Порядок: Ready, 1, 2, > 2, 3, 4, > 4, Count: 2
    }
}
