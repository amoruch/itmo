
public class main {

    static long[] w = new long[6];
    static double[] x = new double[12];
    static double[][] s = new double[6][12];

    public static double frand(double from, double to) {
        double random = Math.random();
        double number = random * (to - from) + from;
        return number;
    }

    public static void makew() {
        for (int i = 0; i < 6; i++) {
            w[i] = (long) (5 + i * 2);
        }
    }

    public static void makex() {
        for (int i = 0; i < 12; i++) {
            x[i] = frand(-10.0, 2.0);
        }
    }

    public static double case1(int j) {
        double a = Math.cos(Math.pow(x[j], 1 / 3)) - 1 / 2;
        double b = Math.pow(Math.E, x[j]) - Math.PI;
        b = Math.pow(b / Math.cos(x[j]), 3);
        return Math.pow(a / b, 3);
    }

    public static double case2(int j) {
        return Math.atan(Math.pow(Math.cos(x[j]), 2));
    }

    public static double case3(int j) {
        double tmp = Math.tan(Math.pow((2 / 3 - x[j]) / x[j], x[j]));
        return Math.tan(Math.pow(0.5 * tmp, 2));
    }

    public static double element(int i, int j) {
        if (w[i] == 13) {
            return case1(j);
        } else if (w[i] < 10) {
            return case2(j);
        } else {
            return case3(j);
        }
    }

    public static void printw() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 12; j++) {
                System.out.printf("%.2f ", s[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        makew();
        makex();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 12; j++) {
                s[i][j] = element(i, j);
            }
        }
        printw();
    }
}
