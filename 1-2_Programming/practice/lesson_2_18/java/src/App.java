import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Урок 2.18 — традиционные классы даты и времени.
 */
public class App {

    public static void main(String[] args) {
        concepts();
        dateDemo();
        timeZoneDemo();
        calendarDemo();
        gregorianDemo();
        bridgesDemo();
    }

    /** Человеческое vs машинное время, EPOCH, Y2K и Y2038. */
    static void concepts() {
        // Человеческое: часы, минуты, дни, недели, месяцы.
        // Машинное: миллисекунды от точки отсчёта.
        //   EPOCH = 1970-01-01 00:00:00 (Java, UNIX).
        //   Windows использует 1601-01-01.
        //
        // Y2K problem (2000) — из-за хранения года двумя цифрами.
        // Y2038 problem — int для секунд с EPOCH переполнится:
        //   2038-01-19 03:14:07 + 1 сек = 1901-12-13 20:45:52.
    }

    /** java.util.Date — момент времени. */
    static void dateDemo() {
        // В Date 1.0 было и человеческое, и машинное представление,
        // и форматирование — но почти всё это deprecated.
        // Сейчас Date — только «момент времени».

        Date now = new Date();                     // текущее время
        Date epoch = new Date(0L);                 // 1970-01-01T00:00:00Z

        long ms = now.getTime();                   // мс от EPOCH
        boolean a = now.after(epoch);
        boolean b = now.before(epoch);

        System.out.println(now + " ms=" + ms + " after=" + a + " before=" + b);

        // Конструктор Date(long) — мс от EPOCH.
        // Метод setTime(long) — изменить момент.
        // Всё остальное (getYear, getMonth, ...) — устарело.
    }

    /** TimeZone — временная зона. */
    static void timeZoneDemo() {
        // До 1972 — GMT (Гринвич). После — UTC (всемирное координированное).
        TimeZone tz = TimeZone.getDefault();
        System.out.println("default=" + tz.getID());

        // Список доступных идентификаторов:
        String[] ids = TimeZone.getAvailableIDs();
        System.out.println("available count=" + ids.length);

        // Смещение без учёта летнего времени:
        int raw = tz.getRawOffset();                // мс
        // С учётом летнего времени для конкретного момента:
        int offset = tz.getOffset(System.currentTimeMillis());

        System.out.println("raw=" + raw + " offset=" + offset);

        // SimpleTimeZone — реализованный подкласс с произвольными правилами DST.
    }

    /** Calendar — абстрактный машинное ↔ человеческое. */
    static void calendarDemo() {
        // Фабрика: Calendar.getInstance() (с учётом локали и TZ).
        Calendar cal = Calendar.getInstance();

        // Поля: YEAR, MONTH (0=январь!), DAY_OF_MONTH, HOUR, MINUTE, ...
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);        // 0..11
        int day = cal.get(Calendar.DAY_OF_MONTH);

        // Изменение:
        cal.set(Calendar.YEAR, 2027);
        cal.add(Calendar.DAY_OF_MONTH, 10);          // с переносом в следующий месяц
        cal.roll(Calendar.DAY_OF_MONTH, 5);          // без переноса

        // Преобразование:
        Date d = cal.getTime();                      // Calendar → Date
        cal.setTime(d);                              // Date → Calendar

        System.out.println(year + "-" + (month + 1) + "-" + day);
    }

    /** GregorianCalendar — конкретный календарь. */
    static void gregorianDemo() {
        // GregorianCalendar наследует Calendar, реализует григорианский календарь
        // и учитывает переход с юлианского.
        GregorianCalendar gc = new GregorianCalendar(2026, Calendar.MARCH, 10);
        System.out.println(gc.getTime());

        // Проверка «високосности»:
        System.out.println("leap=" + gc.isLeapYear(2024));
    }

    /** Мосты между старым (java.util) и новым (java.time) API. */
    static void bridgesDemo() {
        // Date              → Instant:  date.toInstant()
        // Instant           → Date:     Date.from(instant)
        //
        // Calendar          → Instant:  calendar.toInstant()
        // GregorianCalendar → ZonedDateTime:
        //     gc.toZonedDateTime() и GregorianCalendar.fromZonedDateTime(zdt)
        //
        // TimeZone          → ZoneId:   tz.toZoneId()
        //
        // В новом коде используем java.time — java.util.Date/Calendar
        // остаются для совместимости и старых API.
    }
}
