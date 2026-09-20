package com.example.server.commands;

import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;

/**
 * Marks the local server console command that stops the application.
 */
public final class Exit extends Command {

    public Exit(CollectionManager collectionManager) {
        super(null, "exit", "завершить сервер", collectionManager);
    }

    @Override
    public Response apply(Request request) {
        return ok("exit");
    }
}
