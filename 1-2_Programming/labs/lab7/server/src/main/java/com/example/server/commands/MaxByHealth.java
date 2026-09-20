package com.example.server.commands;

import com.example.common.Model.SpaceMarine;
import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Marines;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;
import com.example.server.utility.AuthenticatedUser;

import java.util.List;

/**
 * Shows the element with the greatest health value.
 */
public final class MaxByHealth extends Command {

    public MaxByHealth(CollectionManager collectionManager) {
        super(CommandType.MAX_BY_HEALTH, "max_by_health",
                "вывести элемент с максимальным health", collectionManager);
    }

    @Override
    public Response apply(Request request, AuthenticatedUser user) {
        SpaceMarine marine = collectionManager.maxByHealth();
        return marine == null ? fail("Коллекция пуста!") : ok(new Marines(List.of(marine)));
    }
}
