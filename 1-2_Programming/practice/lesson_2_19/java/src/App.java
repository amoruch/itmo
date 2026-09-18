import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * Урок 2.19 — Date/Time API java.time.
 */
public class App {

    public static void main(String[] args) {
        packages();
        enumsDemo();
        factories();
        instanceMethods();
        localDemo();
        instantDemo();
        zonesDemo();
        formatterDemo();
        periodDurationDemo();
        adjustersDemo();
    }

    /** Пакеты java.time. */
    static void packages() {
        // java.time          — дата, время, периоды, зоны;
        // java.time.chrono   — календарные системы;
        // java.time.format   — форматирование и разбор;
        // java.time.temporal — единицы измерения и поля (ChronoUnit, TemporalAdjusters);
        // java.time.zone     — правила временных зон (ZoneRules).
    }

    /** DayOfWeek / Month — перечисления. */
    static void enumsDemo() {
        DayOfWeek d = DayOfWeek.WEDNESDAY;   // 1..7 (MONDAY=1, SUNDAY=7)
        Month m = Month.MARCH;               // 1..12

        System.out.println(d.getValue() + " " + m.getValue());
        // System.out.println(d.getDisplayName(FormatStyle.FULL, Locale.ENGLISH));
        // System.out.println(m.getDisplayName(FormatStyle.SHORT, Locale.forLanguageTag("ru")));
    }

    /** Соглашения по именам: статические фабрики. */
    static void factories() {
        // of(...)     — создать из параметров
        // from(...)   — из другого типа
        // parse(...)  — из строки
        LocalDate d1 = LocalDate.of(2026, 3, 10);
        LocalDate d2 = LocalDate.parse("2026-01-01");
        LocalDate d3 = LocalDate.from(LocalDateTime.now());
        System.out.println(d1 + " / " + d2 + " / " + d3);
    }

    /** Соглашения по именам: методы экземпляра. */
    static void instanceMethods() {
        // get — получить поле:        getYear(), getMonth()
        // with — копия с изменением:  withYear(2021)
        // plus / minus — копия ± :    plusDays(2), minusWeeks(3)
        // to — привести к другому:    toLocalTime()
        // at — скомбинировать:        date.atTime(time)
        // format — в строку:          format(fmt)
        LocalDate d = LocalDate.now();
        System.out.println(d.getYear() + " " + d.withMonth(1).plusDays(5));
    }

    /** LocalDate / LocalTime / LocalDateTime — «местные». */
    static void localDemo() {
        LocalDate date = LocalDate.of(2026, 3, 10);
        LocalTime time = LocalTime.of(16, 43, 58);
        LocalDateTime dt = LocalDateTime.of(date, time);

        System.out.println(dt.getYear() + "-" + dt.getMonthValue() + "-" + dt.getDayOfMonth()
                + " " + dt.getHour() + ":" + dt.getMinute());
        System.out.println(dt.toLocalDate() + " / " + dt.toLocalTime());

        // Year, YearMonth, MonthDay — полезные частичные типы:
        YearMonth cardExp = YearMonth.of(2028, 12);  // срок действия карты
        MonthDay holiday  = MonthDay.of(1, 1);       // праздник
        System.out.println(cardExp + " / " + holiday);
    }

    /** Instant — момент времени (timestamp). */
    static void instantDemo() {
        Instant now = Instant.now();
        Instant later = now.plusSeconds(60).plusMillis(500).plusNanos(1);
        Instant back  = later.minusSeconds(10).minusMillis(1).minusNanos(1);
        System.out.println(now + " / " + later + " / " + back);
        // Instant — «машинное время» без зоны; аналог Date.
    }

    /** ZoneOffset / ZoneId / OffsetDateTime / ZonedDateTime. */
    static void zonesDemo() {
        // ZoneOffset — фиксированное смещение: UTC+03:00, GMT+2.
        ZoneOffset offset = ZoneOffset.ofHours(3);

        // ZoneId — идентификатор зоны с правилами DST: Europe/Moscow.
        ZoneId zone = ZoneId.of("Europe/Moscow");

        // Комбинации:
        OffsetDateTime odt = OffsetDateTime.of(LocalDateTime.now(), offset);
        ZonedDateTime zdt  = ZonedDateTime.of(LocalDateTime.now(), zone);
        OffsetTime ot = OffsetTime.now(zone);

        System.out.println(odt);
        System.out.println(zdt + " rules=" + zdt.getZone().getRules());
        System.out.println(ot);
    }

    /** DateTimeFormatter — форматирование и разбор. */
    static void formatterDemo() {
        LocalDateTime dt = LocalDateTime.of(2024, 2, 29, 18, 15, 30);

        // Константы:
        System.out.println(dt.format(DateTimeFormatter.ISO_LOCAL_DATE));
        System.out.println(dt.format(DateTimeFormatter.BASIC_ISO_DATE));
        System.out.println(dt.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));

        // Локализованные стили (FULL / LONG / MEDIUM / SHORT):
        var loc = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);
        System.out.println(dt.format(loc));

        // Свой шаблон:
        var fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        System.out.println(dt.format(fmt));
        // Разбор строки обратно:
        LocalDateTime parsed = LocalDateTime.parse("10.03.2026 16:43", fmt);
        System.out.println(parsed);
    }

    /** Duration (часы и меньше) vs Period (дни и больше). */
    static void periodDurationDemo() {
        LocalDateTime t1 = LocalDateTime.now();
        LocalDateTime t2 = t1.plusHours(2).plusMinutes(30);

        Duration dur = Duration.between(t1, t2);
        System.out.println("dur=" + dur.toMinutes() + " min");   // toNanos/toMillis/toSeconds/toMinutes/toHours/toDays

        LocalDate d1 = LocalDate.of(2026, 1, 1);
        LocalDate d2 = LocalDate.of(2026, 3, 10);

        Period per = Period.between(d1, d2);
        System.out.println("period=" + per + " years=" + per.getYears()
                + " months=" + per.getMonths() + " days=" + per.getDays());

        // plus / minus у обоих:
        System.out.println(dur.plusHours(1) + " / " + per.plusMonths(2));
    }

    /** TemporalAdjusters — «умные» переходы. */
    static void adjustersDemo() {
        LocalDate today = LocalDate.now();

        LocalDate firstDay  = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay   = today.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate nextMon   = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate nextYear  = today.plus(1, ChronoUnit.YEARS);

        System.out.println(firstDay + " / " + lastDay + " / " + nextMon + " / " + nextYear);
    }
}
