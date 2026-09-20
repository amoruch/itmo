package com.example.server.commands;

import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;

/**
 * Shows collection metadata.
 */
public final class Info extends Command {

    public Info(CollectionManager collectionManager) {
        super(CommandType.INFO, "info", "вывести информацию о коллекции", collectionManager);
    }

    @Override
    public Response apply(Request request) {
        return ok("Тип: " + collectionManager.collectionType()
                + "\nКоличество элементов: " + collectionManager.size()
                + "\nДата последней инициализации: " + collectionManager.getLastInitTime()
                + "\nДата последнего сохранения: " + collectionManager.getLastSaveTime());
    }
}
