package models;

public enum AstartesCategory {
    ASSAULT("Assault"),
    SUPPRESSOR("Suppressor"),
    TACTICAL("Tactical"),
    TERMINATOR("Terminator"),
    APOTHECARY("Apothecary");

    public String name;

    private AstartesCategory(String name) {
        this.name = name;
    }

    public static String names() {
        String nameList = values()[0].name();
        for (int i = 1; i < values().length; i++) {
            nameList += ", " + values()[i].name();
        }
        return nameList;
    }

    @Override
    public String toString() {
        return name;
    }
}