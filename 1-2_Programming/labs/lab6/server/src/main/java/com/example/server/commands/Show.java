package com.example.server.commands;

import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Shows all elements in natural order.
 */
public final class Show extends Command {

    public Show(CollectionManager collectionManager) {
        super(CommandType.SHOW, "show", "вывести все элементы коллекции", collectionManager);
    }

    @Override
    public Response apply(Request request) {
        List<?> marines = collectionManager.snapshot();
        return ok(marines.isEmpty() ? "Коллекция пуста!"
                : marines.stream().map(Object::toString).collect(Collectors.joining("\n\n")));
    }
}
