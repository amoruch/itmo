package com.example.client.utility;

import com.example.common.Model.AstartesCategory;
import com.example.common.Model.Chapter;
import com.example.common.Model.Coordinates;
import com.example.common.Model.MeleeWeapon;
import com.example.common.Model.SpaceMarine;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Reads and validates command arguments and SpaceMarine fields.
 */
public final class Ask {

    private final Console console;
    private final boolean prompts;

    public Ask(Console console, boolean prompts) {
        this.console = console;
        this.prompts = prompts;
    }

    public String line(String prompt) throws IOException {
        if (prompts) {
            console.print(prompt);
        }
        try {
            return console.readln().trim();
        } catch (NoSuchElementException exception) {
            throw new EOFException("unexpected end of input");
        }
    }

    public int positiveInt(String prompt) throws IOException {
        while (true) {
            try {
                int value = Integer.parseInt(line(prompt));
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // Validation message is shown below.
            }
            if (!prompts) {
                throw new IOException("expected an integer > 0");
            }
            console.println("Enter an integer > 0");
        }
    }

    public long longMin(String prompt, long minExclusive) throws IOException {
        while (true) {
            try {
                long value = Long.parseLong(line(prompt));
                if (value > minExclusive) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // Validation message is shown below.
            }
            if (!prompts) {
                throw new IOException("expected a number > " + minExclusive);
            }
            console.println("Value must be > " + minExclusive);
        }
    }

    public double positiveDouble(String prompt) throws IOException {
        while (true) {
            try {
                double value = Double.parseDouble(line(prompt));
                if (Double.isFinite(value) && value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // Validation message is shown below.
            }
            if (!prompts) {
                throw new IOException("expected a finite number > 0");
            }
            console.println("Value must be a finite number > 0");
        }
    }

    public boolean bool(String prompt) throws IOException {
        while (true) {
            String value = line(prompt).toLowerCase();
            if (value.equals("true") || value.equals("yes") || value.equals("y")) {
                return true;
            }
            if (value.equals("false") || value.equals("no") || value.equals("n")) {
                return false;
            }
            if (!prompts) {
                throw new IOException("expected true/false");
            }
            console.println("Enter true/false");
        }
    }

    public <E extends Enum<E>> E enumValue(String prompt, Class<E> type) throws IOException {
        if (prompts) {
            console.println("Available: " + Arrays.toString(type.getEnumConstants()));
        }
        while (true) {
            try {
                return Enum.valueOf(type, line(prompt).toUpperCase());
            } catch (IllegalArgumentException exception) {
                if (!prompts) {
                    throw new IOException("invalid " + type.getSimpleName(), exception);
                }
                console.println("Invalid enum value");
            }
        }
    }

    public SpaceMarine marine() throws IOException {
        String name = requiredString("name: ", "name");
        Coordinates coordinates = new Coordinates(longMin("coordinates.x (> -319): ", -319),
                longMin("coordinates.y (> -601): ", -601));
        double health = positiveDouble("health (> 0): ");
        boolean loyal = bool("loyal (true/false): ");
        AstartesCategory category = enumValue("category: ", AstartesCategory.class);
        MeleeWeapon weapon = enumValue("meleeWeapon: ", MeleeWeapon.class);
        Chapter chapter = askChapter();
        return SpaceMarine.draft(name, coordinates, health, loyal, category, weapon, chapter);
    }

    private Chapter askChapter() throws IOException {
        String answer = line("chapter? (y/n, empty = no): ").toLowerCase();
        if (answer.equals("y") || answer.equals("yes")) {
            return new Chapter(requiredString("chapter.name: ", "chapter.name"), line("chapter.world: "));
        }
        if (answer.isEmpty() || answer.equals("n") || answer.equals("no")) {
            return null;
        }
        if (!prompts) {
            throw new IOException("expected y/n for chapter");
        }
        console.println("Unknown answer; chapter will be omitted");
        return null;
    }

    private String requiredString(String prompt, String fieldName) throws IOException {
        while (true) {
            String value = line(prompt);
            if (!value.isBlank()) {
                return value;
            }
            if (!prompts) {
                throw new IOException(fieldName + " must not be empty");
            }
            console.println(fieldName + " must not be empty");
        }
    }
}
