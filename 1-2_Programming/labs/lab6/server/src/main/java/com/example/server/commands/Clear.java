package com.example.server.commands;

import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;

/**
 * Clears the collection.
 */
public final class Clear extends Command {

    public Clear(CollectionManager collectionManager) {
        super(CommandType.CLEAR, "clear", "очистить коллекцию", collectionManager);
    }

    @Override
    public Response apply(Request request) {
        collectionManager.clear();
        return ok("Коллекция успешно очищена!");
    }
}
