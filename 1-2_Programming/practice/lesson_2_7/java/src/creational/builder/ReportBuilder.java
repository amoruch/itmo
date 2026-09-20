package creational.builder;

import java.util.Objects;

/**
 * Пошаговая сборка отчёта.
 */
public final class ReportBuilder {

    private final String title;
    private String author = "Неизвестен";
    private int pageCount;
    private boolean published;

    public ReportBuilder(String title) {
        this.title = Objects.requireNonNull(title, "Название обязательно");
    }

    public ReportBuilder author(String author) {
        this.author = Objects.requireNonNull(author, "Автор обязателен");
        return this;
    }

    public ReportBuilder pageCount(int pageCount) {
        if (pageCount < 0) {
            throw new IllegalArgumentException("Число страниц не может быть отрицательным");
        }

        this.pageCount = pageCount;
        return this;
    }

    public ReportBuilder published(boolean published) {
        this.published = published;
        return this;
    }

    public Report build() {
        return new Report(title, author, pageCount, published);
    }
}
