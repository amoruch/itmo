package com.example.server.managers;

import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.commands.Command;
import com.example.server.utility.Console;

/**
 * Dispatches UDP requests and the commands available in the server console.
 */
public final class ServerCommandManager {

    private final CommandManager commandManager;
    private final Console console;

    public ServerCommandManager(CommandManager commandManager, Console console) {
        this.commandManager = commandManager;
        this.console = console;
    }

    public Response handle(Request request) {
        Command command = commandManager.getCommand(request.command());
        if (command == null) {
            return new Response(false, "Команда не найдена. Наберите help для справки.");
        }
        commandManager.addToHistory(command.getName());
        try {
            return command.apply(request);
        } catch (ClassCastException exception) {
            return new Response(false, "Неверные данные для команды " + command.getName() + ".");
        } catch (Exception exception) {
            return new Response(false, "Ошибка выполнения команды: " + exception.getMessage());
        }
    }

    public void runConsole(Runnable stopServer) {
        console.println("Команды сервера: save, exit");
        console.prompt();
        while (console.isCanReadln()) {
            String name = console.readln().trim().toLowerCase();
            if ("exit".equals(name)) {
                stopServer.run();
                return;
            }

            Command command = commandManager.getCommand(name);
            if (command == null || !"save".equals(name)) {
                if (!name.isEmpty()) {
                    console.println("Сервер принимает только команды save и exit.");
                }
            } else {
                Response response = command.apply(null);
                if (response.ok()) {
                    console.println(response.message());
                } else {
                    console.printError(response.message());
                }
            }
            console.prompt();
        }
        stopServer.run();
    }

}
