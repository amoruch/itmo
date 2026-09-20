package com.example.server.commands;

import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;
import com.example.server.utility.AuthenticatedUser;

/**
 * Updates editable fields of an element while preserving server fields.
 */
public final class Update extends Command {

    public Update(CollectionManager collectionManager) {
        super(CommandType.UPDATE, "update id {element}",
                "обновить элемент по id", collectionManager);
    }

    @Override
    public Response apply(Request request, AuthenticatedUser user) throws Exception {
        com.example.common.Protocol.Update update
                = (com.example.common.Protocol.Update) request.payload();
        return collectionManager.update(user, update.id(), update.marine())
                ? ok("SpaceMarine успешно обновлён!")
                : fail("Элемент с id=" + update.id()
                        + " не найден или принадлежит другому пользователю.");
    }
}
