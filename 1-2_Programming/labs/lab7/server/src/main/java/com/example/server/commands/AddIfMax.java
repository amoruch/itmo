package com.example.server.commands;

import com.example.common.Model.SpaceMarine;
import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Marine;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;
import com.example.server.utility.AuthenticatedUser;

/**
 * Adds an element only when it is greater than the current maximum.
 */
public final class AddIfMax extends Command {

    public AddIfMax(CollectionManager collectionManager) {
        super(CommandType.ADD_IF_MAX, "add_if_max",
                "добавить элемент, если он больше максимального", collectionManager);
    }

    @Override
    public Response apply(Request request, AuthenticatedUser user) throws Exception {
        SpaceMarine marine = collectionManager.addIfMax(user, ((Marine) request.payload()).marine());
        return marine == null
                ? fail("SpaceMarine не добавлен: его значение меньше максимального элемента коллекции.")
                : ok("SpaceMarine успешно добавлен! (id=" + marine.id() + ")");
    }
}
