package com.example.server.commands;

import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;

import java.util.stream.Collectors;

/**
 * Shows all elements in descending natural order.
 */
public final class PrintDescending extends Command {

    public PrintDescending(CollectionManager collectionManager) {
        super(CommandType.PRINT_DESCENDING, "print_descending",
                "вывести элементы в порядке убывания", collectionManager);
    }

    @Override
    public Response apply(Request request) {
        return ok(collectionManager.descending().isEmpty() ? "Коллекция пуста!"
                : collectionManager.descending().stream().map(Object::toString)
                        .collect(Collectors.joining("\n\n")));
    }
}
