package utility;

import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import models.*;

public class Ask {

    public static class AskBreak extends Exception {
    }

    public static SpaceMarine askSpaceMarine(Console console, int id) throws AskBreak {
        try {
            String name;
            while (true) {
                console.print("name: ");
                name = console.readln().trim();
                if (name.equals("exit")) {
                    throw new AskBreak();
                }
                if (!name.isEmpty()) {
                    break;
                }
            }

            var coordinates = askCoordinates(console);
            var creationDate = ZonedDateTime.now();
            var health = askHealth(console);
            var loyal = askLoyal(console);
            var category = askAstartesCategory(console);
            var meleeWeapon = askMeleeWeapon(console);
            var chapter = askChapter(console);

            return new SpaceMarine(id, name, coordinates, creationDate, health, loyal, category, meleeWeapon, chapter);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    public static Coordinates askCoordinates(Console console) throws AskBreak {
        try {
            long x;
            while (true) {
                console.print("coordinates.x: ");
                var line = console.readln().trim();
                if (line.equals("exit")) {
                    throw new AskBreak();
                }
                if (!line.equals("")) {
                    try {
                        x = Long.parseLong(line);
                        if (x > -319) {
                            break;
                        }
                    } catch (Exception e) {
                    }
                }
            }
            Long y;
            while (true) {
                console.print("coordinates.y: ");
                var line = console.readln().trim();
                if (line.equals("exit")) {
                    throw new AskBreak();
                }
                if (!line.equals("")) {
                    try {
                        y = Long.parseLong(line);
                        if (y > -601) {
                            break;
                        }
                    } catch (Exception e) {
                    }
                }
            }
            return new Coordinates(x, y);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    public static double askHealth(Console console) throws AskBreak {
        try {
            double health;
            while (true) {
                console.print("health: ");
                var line = console.readln().trim();
                if (line.equals("exit")) {
                    throw new AskBreak();
                }
                if (!line.equals("")) {
                    try {
                        health = Double.parseDouble(line);
                        if (health > 0) {
                            break;
                        }
                    } catch (Exception e) {
                    }
                }
            }
            return health;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return -1;
        }
    }

    public static Boolean askLoyal(Console console) throws AskBreak {
        try {
            Boolean loyal;
            while (true) {
                console.print("loyal: ");
                var line = console.readln().trim();
                if (line.equals("exit")) {
                    throw new AskBreak();
                }
                if (!line.equals("")) {
                    try {
                        loyal = Boolean.parseBoolean(line);
                        break;
                    } catch (Exception e) {
                    }
                }
            }
            return loyal;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    public static AstartesCategory askAstartesCategory(Console console) throws AskBreak {
        try {
            AstartesCategory category;
            while (true) {
                console.print("astartes category(" + AstartesCategory.names() + "): ");
                var line = console.readln().trim();
                if (line.equals("exit")) {
                    throw new AskBreak();
                }
                if (!line.equals("")) {
                    try {
                        category = AstartesCategory.valueOf(line);
                        break;
                    } catch (Exception e) {
                    }
                }
            }
            return category;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    public static MeleeWeapon askMeleeWeapon(Console console) throws AskBreak {
        try {
            MeleeWeapon meleeWeapon;
            while (true) {
                console.print("melee weapon(" + MeleeWeapon.names() + "): ");
                var line = console.readln().trim();
                if (line.equals("exit")) {
                    throw new AskBreak();
                }
                if (!line.equals("")) {
                    try {
                        meleeWeapon = MeleeWeapon.valueOf(line);
                        break;
                    } catch (Exception e) {
                    }
                }
            }
            return meleeWeapon;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    public static Chapter askChapter(Console console) throws AskBreak {
        try {
            String name;
            while (true) {
                console.print("chapter.name: ");
                String line = console.readln().trim();
                if (line.equals("exit")) {
                    throw new AskBreak();
                }
                if (line.isEmpty()) {
                    return null;
                } else {
                    name = line;
                    break;
                }
            }
            String world;
            while (true) {
                console.print("chapter.world: ");
                String line = console.readln().trim();
                if (line.equals("exit")) {
                    throw new AskBreak();
                }
                if (line != null) {
                    world = line;
                    break;
                }
            }
            return new Chapter(name, world);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }
}
