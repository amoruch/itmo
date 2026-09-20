package com.example.server.commands;

import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Marine;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;

/**
 * Adds a new SpaceMarine.
 */
public final class Add extends Command {

    public Add(CollectionManager collectionManager) {
        super(CommandType.ADD, "add", "добавить новый элемент в коллекцию", collectionManager);
    }

    @Override
    public Response apply(Request request) {
        return ok("SpaceMarine успешно добавлен! (id="
                + collectionManager.add(((Marine) request.payload()).marine()).id() + ")");
    }
}
