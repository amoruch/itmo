package com.example.server.commands;

import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;
import com.example.server.utility.AuthenticatedUser;
import com.example.common.Model.SpaceMarine;

/**
 * Removes the first collection element.
 */
public final class RemoveFirst extends Command {

    public RemoveFirst(CollectionManager collectionManager) {
        super(CommandType.REMOVE_FIRST, "remove_first",
                "удалить первый элемент коллекции", collectionManager);
    }

    @Override
    public Response apply(Request request, AuthenticatedUser user) throws Exception {
        SpaceMarine first = collectionManager.first();
        if (first == null) {
            return fail("Коллекция пуста!");
        }
        return collectionManager.remove(user, first.id())
                ? ok("Первый элемент коллекции успешно удалён!")
                : fail("Первый элемент принадлежит другому пользователю.");
    }
}
