package com.example.server.commands;

import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Id;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;

/**
 * Removes an element by id.
 */
public final class RemoveById extends Command {

    public RemoveById(CollectionManager collectionManager) {
        super(CommandType.REMOVE_BY_ID, "remove_by_id id",
                "удалить элемент по id", collectionManager);
    }

    @Override
    public Response apply(Request request) {
        int id = ((Id) request.payload()).id();
        return collectionManager.remove(id) ? ok("SpaceMarine успешно удалён!")
                : fail("Элемент с id=" + id + " не найден.");
    }
}
