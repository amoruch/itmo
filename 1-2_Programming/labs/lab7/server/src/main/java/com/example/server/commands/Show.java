package com.example.server.commands;

import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Marines;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;
import com.example.server.utility.AuthenticatedUser;

/**
 * Shows all elements in natural order.
 */
public final class Show extends Command {

    public Show(CollectionManager collectionManager) {
        super(CommandType.SHOW, "show", "вывести все элементы коллекции", collectionManager);
    }

    @Override
    public Response apply(Request request, AuthenticatedUser user) {
        return ok(new Marines(collectionManager.snapshot()));
    }
}
