package com.example.server.commands;

import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;
import com.example.server.managers.CommandManager;
import com.example.server.utility.AuthenticatedUser;

/**
 * Shows the last client commands.
 */
public final class History extends Command {

    private final CommandManager commandManager;

    public History(CollectionManager collectionManager, CommandManager commandManager) {
        super(CommandType.HISTORY, "history", "вывести последние команды", collectionManager);
        this.commandManager = commandManager;
    }

    @Override
    public Response apply(Request request, AuthenticatedUser user) {
        return ok(commandManager.getCommandHistory().isEmpty()
                ? "История команд пуста."
                : String.join("\n", commandManager.getCommandHistory()));
    }
}
