package com.example.server.commands;

import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;
import com.example.server.managers.CommandManager;
import com.example.server.utility.AuthenticatedUser;

/**
 * Prints help for commands registered by CommandManager.
 */
public final class Help extends Command {

    private final CommandManager commandManager;

    public Help(CollectionManager collectionManager, CommandManager commandManager) {
        super(CommandType.HELP, "help", "вывести справку по доступным командам", collectionManager);
        this.commandManager = commandManager;
    }

    @Override
    public Response apply(Request request, AuthenticatedUser user) {
        StringBuilder result = new StringBuilder();
        commandManager.getCommands().forEach((name, command)
                -> result.append(String.format("%-38s %s%n", name, command.getDescription())));
        result.append("execute_script file_name                 выполнить команды из файла (клиент)\n")
                .append("exit                                     завершить клиент");
        return ok(result.toString());
    }
}
