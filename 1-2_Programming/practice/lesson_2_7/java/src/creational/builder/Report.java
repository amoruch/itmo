package creational.builder;

/**
 * Неизменяемый отчёт, который собирает ReportBuilder.
 */
public final class Report {

    private final String title;
    private final String author;
    private final int pageCount;
    private final boolean published;

    Report(String title, String author, int pageCount, boolean published) {
        this.title = title;
        this.author = author;
        this.pageCount = pageCount;
        this.published = published;
    }

    @Override
    public String toString() {
        return "Report{title='%s', author='%s', pages=%d, published=%b}"
                .formatted(title, author, pageCount, published);
    }
}
