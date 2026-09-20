package com.example.server.commands;

import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;

import java.io.IOException;

/**
 * Saves the server collection to the configured JSON file.
 */
public final class Save extends Command {

    public Save(CollectionManager collectionManager) {
        super(null, "save", "сохранить коллекцию в файл", collectionManager);
    }

    @Override
    public Response apply(Request request) {
        try {
            collectionManager.saveCollection();
            return ok("Коллекция успешно сохранена!");
        } catch (IOException exception) {
            return fail("Не удалось сохранить коллекцию: " + exception.getMessage());
        }
    }
}
