package com.example.server.commands;

import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Marine;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;

/**
 * Removes all elements lower than the supplied one.
 */
public final class RemoveLower extends Command {

    public RemoveLower(CollectionManager collectionManager) {
        super(CommandType.REMOVE_LOWER, "remove_lower {element}",
                "удалить элементы, меньшие заданного", collectionManager);
    }

    @Override
    public Response apply(Request request) {
        return ok("Подходящие элементы коллекции удалены: "
                + collectionManager.removeLower(((Marine) request.payload()).marine()));
    }
}
