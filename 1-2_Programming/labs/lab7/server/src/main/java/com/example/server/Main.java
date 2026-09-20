package com.example.server;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.server.commands.Add;
import com.example.server.commands.AddIfMax;
import com.example.server.commands.Clear;
import com.example.server.commands.GroupCountingByCreationDate;
import com.example.server.commands.Help;
import com.example.server.commands.History;
import com.example.server.commands.Info;
import com.example.server.commands.MaxByHealth;
import com.example.server.commands.PrintDescending;
import com.example.server.commands.RemoveById;
import com.example.server.commands.RemoveFirst;
import com.example.server.commands.RemoveLower;
import com.example.server.commands.Show;
import com.example.server.commands.Update;
import com.example.server.managers.CollectionManager;
import com.example.server.managers.CommandManager;
import com.example.server.managers.DatabaseManager;
import com.example.server.managers.DatagramServer;
import com.example.server.managers.RequestHandler;
import com.example.server.utility.Console;
import com.example.server.utility.MarineRepository;
import com.example.server.utility.StandardConsole;
import com.example.server.utility.UserRepository;

/**
 * Entry point of the server application.
 */
public class Main {

    private static final DateTimeFormatter SESSION_FORMAT
            = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public static void main(String[] args) {
        configureLogSession();
        Logger logger = LogManager.getLogger(Main.class);
        int port;
        try {
            port = args.length > 0 ? Integer.parseInt(args[0]) : 8000;
            if (port < 1 || port > 65_535) {
                throw new NumberFormatException("port is outside 1..65535");
            }
        } catch (NumberFormatException exception) {
            System.err.println("Invalid port: " + exception.getMessage());
            logger.error("Server did not start because of an invalid port", exception);
            return;
        }

        Console console = new StandardConsole();
        DatabaseManager databaseManager = DatabaseManager.fromEnvironment();
        try {
            databaseManager.verifyConnection();
            CollectionManager collectionManager = new CollectionManager(
                    new MarineRepository(databaseManager));
            if (!collectionManager.init()) {
                console.printError("Коллекция в БД содержит некорректные или повторяющиеся id.");
                logger.error("Server did not start because the database collection is invalid");
                return;
            }
            logger.info("Collection initialized from database with {} elements", collectionManager.size());

            CommandManager commandManager = createCommandManager(collectionManager);
            RequestHandler requestHandler = new RequestHandler(
                    commandManager, new UserRepository(databaseManager));
            try (DatagramServer server = new DatagramServer(port, requestHandler)) {
                server.start();
                console.println("Сервер запущен на UDP-порту " + port + ".");
                logger.info("Server started on UDP port {}", port);
                new ServerRunner(console).run(server::stop);
            }
        } catch (SQLException exception) {
            console.printError("Не удалось подключиться к PostgreSQL: " + exception.getMessage());
            logger.error("Server did not start because PostgreSQL is unavailable", exception);
        } catch (IOException exception) {
            console.printError("Server error: " + exception.getMessage());
            logger.error("Server error", exception);
        }
    }

    private static void configureLogSession() {
        if (System.getProperty("server.session.id") == null) {
            System.setProperty("server.session.id", SESSION_FORMAT.format(LocalDateTime.now()));
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
        return manager;
    }
}
