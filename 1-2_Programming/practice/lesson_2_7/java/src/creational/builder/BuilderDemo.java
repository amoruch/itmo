package creational.builder;

/**
 * Демонстрация пошаговой сборки отчёта.
 */
public final class BuilderDemo {

    private BuilderDemo() {
    }

    public static void run() {
        Report report = new ReportBuilder("Практика по Java")
                .author("Аморуч")
                .pageCount(12)
                .published(true)
                .build();

        System.out.println(report);
    }
}
