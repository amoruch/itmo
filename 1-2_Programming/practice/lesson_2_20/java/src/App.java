import java.util.function.*;

/**
 * Урок 2.20 — функциональное программирование: теория и лямбды.
 */
public class App {

    public static void main(String[] args) {
        fpPrinciples();
        iterationVsRecursion();
        factorialDemo();
        lambdaCalculus();
        callbacks();
        functionalInterfaceDemo();
        lambdaSyntax();
        closuresDemo();
    }

    /** Принципы ФП и функции высшего порядка. */
    static void fpPrinciples() {
        // Функции высшего порядка — принимают и/или возвращают функции.
        // Ленивые вычисления — значение считается только при необходимости.
        // Нет побочных эффектов — вызов не меняет внешнее состояние.
        // Нет состояния — функция зависит только от аргументов.
        //
        // Достоинства:
        //   - проще тестировать (детерминизм);
        //   - проще распараллеливать (нет общих данных);
        //   - оптимизация (кэширование, ленивость).
        //
        // В Java — это функциональные интерфейсы + лямбды + Stream API.

        // Пример: apply — «функция высшего порядка» — принимает другую функцию.
        Function<Integer, Integer> inc = x -> x + 1;
        Function<Integer, Integer> twice = x -> x * 2;
        System.out.println(inc.apply(5) + " / " + twice.apply(5));
    }

    /** Итерация ↔ рекурсия: стек вызовов. */
    static void iterationVsRecursion() {
        // Итерация (цикл) — данные в куче, стек O(1).
        // Рекурсия — каждый вызов кладёт в стек: параметры + адрес возврата
        //   + локальные переменные. При глубокой рекурсии → StackOverflowError.
        //
        // Хвостовая рекурсия — рекурсивный вызов стоит последней командой.
        //   Можно оптимизировать до итерации: заменить параметры и перейти
        //   к началу (компилятор Java этого НЕ делает — вручную).
    }

    /** Факториал: итерация / рекурсия / хвостовая рекурсия. */
    static void factorialDemo() {
        System.out.println("iter="   + factIter(5));
        System.out.println("rec="    + factRec(5));
        System.out.println("tail="   + factTail(5, 1));

        // factRec(5) → 5 * factRec(4) → 5 * 4 * factRec(3) → ... → 120
        // factTail(5,1) → factTail(4,5) → factTail(3,20) → ... → 120
    }

    static int factIter(int n) {
        int result = 1;
        for (int i = 1; i <= n; i++) result *= i;
        return result;
    }
    static int factRec(int n) { return n <= 1 ? 1 : factRec(n - 1) * n; }
    static int factTail(int n, int acc) { return n <= 1 ? acc : factTail(n - 1, acc * n); }

    /** λ-исчисление Чёрча: абстракция и аппликация. */
    static void lambdaCalculus() {
        // Переменная x.
        // Абстракция:    λx.f — «функция от x со значением f».
        // Аппликация:    f g — «применить f к аргументу g».
        //
        // Пример: inc(x) = x + 1
        //   f(x) = x + 1
        //   (x) → x + 1
        //   λx.x+1
        //
        //   inc(3) ⇔ f(3) ⇔ (λx.x+1)(3) ⇔ 4
        //
        // Свободные и связанные переменные:
        //   λx. x + y    — x связана, y свободна.
    }

    /** Callback — передача функции для последующего вызова. */
    static void callbacks() {
        // Зачем: разные стратегии поведения, асинхронная реакция на события.
        // Реализации: указатели на функцию (C, C++), делегаты (C#),
        //   объект интерфейса / анонимный класс (Java < 8), λ-выражения (Java 8+).
        System.out.println("squared=" + apply(5, x -> x * x));
        System.out.println("halved="  + apply(10, x -> x / 2));
    }
    static int apply(int x, IntUnaryOperator f) { return f.applyAsInt(x); }

    /** @FunctionalInterface — один абстрактный метод. */
    static void functionalInterfaceDemo() {
        // Аннотация @FunctionalInterface — проверка на этапе компиляции.
        // Ровно один абстрактный метод (не считая default и методов Object).
        // Может иметь static и default методы.

        // Пример: свой интерфейс.
        MyConverter<Integer, String> conv = i -> "n" + i;
        System.out.println(conv.convert(7));

        // Эквивалент встроенного Function<T,R>.
        Function<Integer, String> fn = i -> "n" + i;
        System.out.println(fn.apply(7));
    }

    @FunctionalInterface
    interface MyConverter<T, R> {
        R convert(T t);
        default void log(T t) { System.out.println("converted " + t); }
        static <T> boolean notNull(T t) { return t != null; }
    }

    /** Синтаксис лямбд. */
    static void lambdaSyntax() {
        // Параметр → выражение
        Function<Integer, Integer> a = x -> x * 2;
        // (Параметры) → блок с инструкциями
        Function<Integer, Integer> b = (Integer x) -> { return x * 2; };
        // Без параметров
        Supplier<Integer> c = () -> 42;
        // С параметром без типа
        IntBinaryOperator d = (x, y) -> x + y;
        // Блок с несколькими инструкциями
        Function<String, Integer> e = s -> {
            int len = s.length();
            return len * 2;
        };
        // Ссылка на метод
        Function<String, Integer> f = Integer::valueOf;
        Consumer<String> g = System.out::println;
        Supplier<App> h = App::new;

        System.out.println(a.apply(3) + " " + d.applyAsInt(2, 3)
                + " " + e.apply("abc") + " " + f.apply("42"));
        g.accept("hello");
    }

    /** Замыкания: захват значений из внешнего блока. */
    static void closuresDemo() {
        // Область видимости лямбды = окружающий блок.
        // Можно использовать только «эффективно финальные» переменные
        //   (effectively final — не переназначаются после инициализации).
        int count = 3;
        String message = "hi";

        Runnable r = () -> {
            for (int i = 0; i < count; i++) System.out.println(message + " " + i);
        };
        r.run();

        // count = 4;  // ошибка: не effectively final
        // Замыкание = лямбда + захваченные значения (count, message).

        // Раньше (Java < 8) то же делалось анонимным классом — синтаксис громоздкий:
        Runnable r2 = new Runnable() {
            @Override public void run() { System.out.println("old style"); }
        };
        r2.run();
    }
}
