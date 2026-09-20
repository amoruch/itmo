package com.example.server.managers;

import com.example.common.Model.SpaceMarine;
import com.example.common.ZonedDateTimeAdapter;
import com.example.server.utility.Console;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.LinkedList;

/**
 * Reads and writes the collection JSON, as in lab 5.
 */
public final class DumpManager {

    private final Path file;
    private final Console console;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .create();

    public DumpManager(Path file, Console console) {
        this.file = file;
        this.console = console;
    }

    public LinkedList<SpaceMarine> readCollection() {
        if (!Files.exists(file)) {
            return new LinkedList<>();
        }
        try {
            String json = Files.readString(file, StandardCharsets.UTF_8);
            if (json.isBlank()) {
                return new LinkedList<>();
            }
            Type type = new TypeToken<LinkedList<SpaceMarine>>() {
            }.getType();
            LinkedList<SpaceMarine> collection = gson.fromJson(json, type);
            return collection == null ? new LinkedList<>() : collection;
        } catch (Exception exception) {
            console.printError("Не удалось загрузить коллекцию: " + exception.getMessage());
            return new LinkedList<>();
        }
    }

    public void writeCollection(LinkedList<SpaceMarine> collection) throws IOException {
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(file, gson.toJson(collection), StandardCharsets.UTF_8);
    }
}
