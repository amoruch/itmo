package com.example.server.commands;

import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;

/**
 * Removes the first collection element.
 */
public final class RemoveFirst extends Command {

    public RemoveFirst(CollectionManager collectionManager) {
        super(CommandType.REMOVE_FIRST, "remove_first",
                "удалить первый элемент коллекции", collectionManager);
    }

    @Override
    public Response apply(Request request) {
        return collectionManager.removeFirst() == null ? fail("Коллекция пуста!")
                : ok("Первый элемент коллекции успешно удалён!");
    }
}
