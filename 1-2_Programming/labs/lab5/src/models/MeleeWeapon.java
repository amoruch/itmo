package models;

public enum MeleeWeapon {
    POWER_SWORD("power sword"),
    MANREAPER("Manreaper"),
    LIGHTING_CLAW("Lighting claw"),
    POWER_FIST("Power fist");

    public String name;

    private MeleeWeapon(String name) {
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