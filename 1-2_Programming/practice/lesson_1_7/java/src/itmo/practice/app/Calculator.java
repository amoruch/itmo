package itmo.practice.app;

/**
 * Выполняет базовые операции с целыми числами.
 */
public class Calculator {

    /**
     * Возвращает сумму двух чисел.
     *
     * @param first первое слагаемое
     * @param second второе слагаемое
     * @return сумма аргументов
     */
    public int add(int first, int second) {
        return first + second;
    }

    /**
     * Делит первое число на второе.
     *
     * @param dividend делимое
     * @param divisor делитель
     * @return результат целочисленного деления
     * @throws IllegalArgumentException если делитель равен нулю
     */
    public int divide(int dividend, int divisor) {
        if (divisor == 0) {
            throw new IllegalArgumentException("Делитель не может быть равен нулю");
        }

        return dividend / divisor;
    }
}
