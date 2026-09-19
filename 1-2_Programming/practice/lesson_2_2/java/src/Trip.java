import java.time.LocalDate;

/** Поездка с необязательными параметрами. */
class Trip {

    private final String where;
    private final String transport;
    private final LocalDate date;

    private Trip(Builder builder) {
        where = builder.where;
        transport = builder.transport;
        date = builder.date;
    }

    @Override
    public String toString() {
        return "%s, %s, %s".formatted(where, transport, date);
    }

    static class Builder {

        private String where;
        private String transport;
        private LocalDate date;

        Builder where(String where) {
            this.where = where;
            return this;
        }

        Builder transport(String transport) {
            this.transport = transport;
            return this;
        }

        Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        Trip build() {
            return new Trip(this);
        }
    }
}