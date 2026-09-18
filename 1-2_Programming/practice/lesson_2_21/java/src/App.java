import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongUnaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntBiFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;

/**
 * Урок 2.21 — функциональные интерфейсы в Java API.
 */
public class App {

    public static void main(String[] args) {
        knownFromBefore();
        supplierDemo();
        consumerDemo();
        predicateDemo();
        functionDemo();
        unaryOperatorDemo();
        biVariantsDemo();
        binaryOperatorDemo();
    }

    /** Уже встречались: Runnable и Comparator. */
    static void knownFromBefore() {
        // java.lang.Runnable { void run(); }
        new Thread(() -> System.out.println("thread")).start();

        // java.util.Comparator<T> { int compare(T a, T b); }
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4));
        Collections.sort(list, (x, y) -> y - 2 * x);
        System.out.println(list);

        // java.util.function.* — набор стандартных функциональных интерфейсов
        // для типовых случаев: получить, принять, проверить, преобразовать.
    }

    /** Supplier<R> — «поставщик»: ничего не принимает, возвращает R. */
    static void supplierDemo() {
        Supplier<String> sup = () -> "hi";
        System.out.println(sup.get());

        // Применение: ленивое вычисление, фабрики, Stream.generate.
        //   Logger.log(Level, Supplier<String> msgSupplier) — сообщение
        //   вычисляется только если лог реально пишется.

        // Специализации:
        IntSupplier    is = () -> 42;
        LongSupplier   ls = () -> 42L;
        DoubleSupplier ds = () -> 3.14;
        BooleanSupplier bs = () -> true;
        System.out.println(is.getAsInt() + ls.getAsLong() + ds.getAsDouble() + (bs.getAsBoolean() ? 1 : 0));
    }

    /** Consumer<T> — «потребитель»: принимает T, ничего не возвращает. */
    static void consumerDemo() {
        Consumer<String> print = s -> System.out.print(s + " ");
        print.accept("a"); print.accept("b");
        System.out.println();

        // Цепочка через andThen: c1, потом c2.
        Consumer<String> c1 = s -> System.out.println("Processing: " + s);
        Consumer<String> c2 = s -> System.out.println("Length: " + s.length());
        Consumer<String> c3 = c1.andThen(c2);
        c3.accept("Java");

        // Применение: Collection.forEach, Stream.peek/forEach.
        List.of("x", "y", "z").forEach(print);

        // Специализации по примитивам:
        IntConsumer ic = v -> {};
        LongConsumer lc = v -> {};
        DoubleConsumer dc = v -> {};
    }

    /** Predicate<T> — «предикат»: принимает T, возвращает boolean. */
    static void predicateDemo() {
        Predicate<Integer> isEven = x -> x % 2 == 0;
        System.out.println(isEven.test(4) + " " + isEven.test(3));

        // Композиция: and / or / negate.
        Predicate<Integer> gt0 = x -> x > 0;
        Predicate<Integer> evenAndPositive = isEven.and(gt0);
        Predicate<Integer> evenOrPositive = isEven.or(gt0);
        System.out.println(evenAndPositive.test(4) + " " + evenOrPositive.test(-3)
                + " " + isEven.negate().test(3));

        // Применение: Collection.removeIf, Stream.filter, allMatch/anyMatch/noneMatch.
        var list = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        list.removeIf(isEven);
        System.out.println(list);

        // Специализации:
        IntPredicate    ip = v -> v > 0;
        LongPredicate   lp = v -> v > 0;
        DoublePredicate dp = v -> v > 0;
    }

    /** Function<T,R> — «функция»: T → R. */
    static void functionDemo() {
        Function<String, Integer> len = s -> s.length();
        System.out.println(len.apply("hello"));

        // Композиция:
        //   andThen: сначала this, потом after  → after(this(x));
        //   compose: сначала before, потом this → this(before(x));
        //   identity: возвращает аргумент как есть.
        Function<Integer, Integer> inc = x -> x + 1;
        Function<Integer, Integer> dbl = x -> x * 2;
        System.out.println(inc.andThen(dbl).apply(3));  // (3+1)*2 = 8
        System.out.println(inc.compose(dbl).apply(3));  // (3*2)+1 = 7
        System.out.println(Function.<String>identity().apply("same"));

        // Применение: Stream.map, String.transform (Java 12+).
        System.out.println("hi".transform(len));

        // Специализации:
        IntFunction<String>    ifn = i -> "n" + i;
        DoubleFunction<String> dfn = d -> "d" + d;
        ToLongFunction<String> tlf = s -> s.length();
        IntToLongFunction      itl = i -> (long) i;
    }

    /** UnaryOperator<T> extends Function<T,T> — тот же тип на входе и выходе. */
    static void unaryOperatorDemo() {
        UnaryOperator<Integer> sq = x -> x * x;
        System.out.println(sq.apply(9));

        // Тот же набор и andThen/compose/identity, но без второго параметра-типа.
        // Применение: Stream.iterate, IntStream.map.

        // Специализации:
        IntUnaryOperator    iu = x -> x + 1;
        LongUnaryOperator   lu = x -> x + 1L;
        DoubleUnaryOperator du = x -> x + 1.0;
    }

    /** Bi-варианты: две входных переменных. */
    static void biVariantsDemo() {
        // BiConsumer<T,U> — принять два значения.
        BiConsumer<String, Integer> printKV = (k, v) -> System.out.println(k + "=" + v);
        printKV.accept("x", 1);

        // BiPredicate<T,U> — проверить два значения.
        BiPredicate<String, Integer> check = (s, i) -> s.length() == i;
        System.out.println(check.test("abc", 3));

        // BiFunction<T,U,R> — преобразовать пару.
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        System.out.println(add.apply(2, 3));

        // У BiFunction только andThen — compose/identity бессмысленны:
        //   compose требует, чтобы одна функция давала два аргумента;
        //   identity определена только для одной переменной.
        System.out.println(add.andThen(x -> x * 10).apply(2, 3));

        // Специализации:
        ToIntBiFunction<String, String> tif = (a, b) -> a.length() + b.length();
        ObjIntConsumer<String>          oic = (s, i) -> {};
        // и т. п.
    }

    /** BinaryOperator<T> extends BiFunction<T,T,T> — два T → T. */
    static void binaryOperatorDemo() {
        BinaryOperator<Integer> max = (a, b) -> a > b ? a : b;
        System.out.println(max.apply(3, 7));

        // Фабрики:
        //   BinaryOperator.minBy(Comparator) / maxBy(Comparator) —
        //   готовые операторы для reduce по заданному порядку.
        Comparator<Integer> nat = Comparator.naturalOrder();
        System.out.println(BinaryOperator.<Integer>minBy(nat).apply(3, 7));
        System.out.println(BinaryOperator.<Integer>maxBy(nat).apply(3, 7));

        // Применение: Stream.reduce(identity, accumulator).

        // Специализации:
        IntBinaryOperator    ib = (a, b) -> a + b;
        LongBinaryOperator   lb = (a, b) -> a + b;
        DoubleBinaryOperator db = (a, b) -> a + b;
        System.out.println(ib.applyAsInt(2, 3) + lb.applyAsLong(2, 3) + db.applyAsDouble(2, 3));
    }
}
