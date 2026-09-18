class Outer {

    private final int value;

    Outer(int value) {
        this.value = value;
    }

    static class Label {

        private final String text;

        Label(String text) {
            this.text = text;
        }

        String text() {
            return text;
        }
    }

    class Counter {

        int outerValue() {
            return value;
        }
    }

    String localClassMessage() {
        class LocalMessage {

            String text() {
                return "Привет из локального класса";
            }
        }

        return new LocalMessage().text();
    }
}