package com.example;

import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Урок 2.6 — GUI и локализация.
 */
public class Main {

    public static void main(String[] args) {
        part1Ui();
        part2Swing();
        part3Java2D();
        part4JavaFx(args);
        part5Localization();
    }

    static void part1Ui() {
        System.out.println("UI: CLI — текстовый интерфейс, GUI — графический.");
        System.out.println("В GUI компоненты образуют дерево внутри контейнеров.");
    }

    static void part2Swing() {
        System.out.println("Swing: окно с компонентами и обработчиком события.");
        SwingApp.main(new String[0]);
    }

    static void part3Java2D() {
        System.out.println("Java 2D: отдельное окно с анимацией через Timer и repaint().");
        Java2DApp.main(new String[0]);
    }

    static void part4JavaFx(String[] args) {
        System.out.println("JavaFX: FXML, CSS и обработчик события.");
        JavaFXApp.main(args);
    }

    static void part5Localization() {
        Locale russian = Locale.forLanguageTag("ru-RU");
        ResourceBundle eclipse = ResourceBundle.getBundle("com.example.Eclipse", russian);
        Date date = Date.from(Instant.parse("2024-04-08T18:17:00Z"));
        MessageFormat eclipseFormat = new MessageFormat(eclipse.getString("msg"), russian);

        System.out.println(eclipseFormat.format(new Object[]{eclipse.getString("full"), date}));
        System.out.println(NumberFormat.getCurrencyInstance(russian).format(1234.5));
        System.out.println(formatLikes(Locale.ENGLISH, 15));
    }

    static String formatLikes(Locale locale, int count) {
        ResourceBundle bundle = ResourceBundle.getBundle("com.example.Like", locale);
        ChoiceFormat choice = new ChoiceFormat(
                new double[]{0, 1, 2},
                new String[]{bundle.getString("none"), bundle.getString("one"), bundle.getString("many")}
        );
        String pattern = choice.format(count);
        return new MessageFormat(pattern, locale).format(new Object[]{count});
    }
}