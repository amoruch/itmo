package com.example.server;

import com.example.server.commands.Add;
import com.example.server.commands.AddIfMax;
import com.example.server.commands.Clear;
import com.example.server.commands.Exit;
import com.example.server.commands.GroupCountingByCreationDate;
import com.example.server.commands.Help;
import com.example.server.commands.History;
import com.example.server.commands.Info;
import com.example.server.commands.MaxByHealth;
import com.example.server.commands.PrintDescending;
import com.example.server.commands.RemoveById;
import com.example.server.commands.RemoveFirst;
import com.example.server.commands.RemoveLower;
import com.example.server.commands.Save;
import com.example.server.commands.Show;
import com.example.server.commands.Update;
import com.example.server.managers.CollectionManager;
import com.example.server.managers.CommandManager;
import com.example.server.managers.DumpManager;
import com.example.server.managers.ServerCommandManager;
import com.example.server.utility.Console;
import com.example.server.utility.StandardConsole;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Entry point of the server application.
 */
public class Main {

    public static void main(String[] args) {
        int port;
        try {
            port = args.length > 0 ? Integer.parseInt(args[0]) : 8000;
            if (port < 1 || port > 65_535) {
                throw new NumberFormatException("port is outside 1..65535");
            }
        } catch (NumberFormatException exception) {
            System.err.println("Invalid port: " + exception.getMessage());
            return;
        }

        Console console = new StandardConsole();
        DumpManager dumpManager = new DumpManager(
                Path.of(args.length > 1 ? args[1] : "collection.json"), console);
        CollectionManager collectionManager = new CollectionManager(dumpManager);
        if (!collectionManager.init()) {
            console.printError("Загруженная коллекция содержит некорректные или повторяющиеся id.");
            return;
        }

        CommandManager commandManager = createCommandManager(collectionManager);
        ServerCommandManager serverCommandManager
                = new ServerCommandManager(commandManager, console);

        try (DatagramServer server = new DatagramServer(port, serverCommandManager)) {
            server.start();
            console.println("Сервер запущен на UDP-порту " + port + ".");
            serverCommandManager.runConsole(server::stop);
        } catch (IOException exception) {
            console.printError("Server error: " + exception.getMessage());
        } finally {
            try {
                collectionManager.saveCollection();
                console.println("Server stopped; collection saved.");
            } catch (IOException exception) {
                console.printError("Could not save collection: " + exception.getMessage());
            }
        }
    }

    private static CommandManager createCommandManager(CollectionManager collectionManager) {
        CommandManager manager = new CommandManager();
        manager.register("help", new Help(collectionManager, manager));
        manager.register("info", new Info(collectionManager));
        manager.register("show", new Show(collectionManager));
        manager.register("add", new Add(collectionManager));
        manager.register("update", new Update(collectionManager));
        manager.register("remove_by_id", new RemoveById(collectionManager));
        manager.register("clear", new Clear(collectionManager));
        manager.register("remove_first", new RemoveFirst(collectionManager));
        manager.register("add_if_max", new AddIfMax(collectionManager));
        manager.register("remove_lower", new RemoveLower(collectionManager));
        manager.register("max_by_health", new MaxByHealth(collectionManager));
        manager.register("group_counting_by_creation_date",
                new GroupCountingByCreationDate(collectionManager));
        manager.register("print_descending", new PrintDescending(collectionManager));
        manager.register("history", new History(collectionManager, manager));
        manager.register("save", new Save(collectionManager));
        manager.register("exit", new Exit(collectionManager));
        return manager;
    }
}
