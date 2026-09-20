package com.example.server;

import com.example.server.utility.Console;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Runs the small set of commands available only in the server console.
 */
public final class ServerRunner {

    private static final Logger LOGGER = LogManager.getLogger(ServerRunner.class);
    private final Console console;

    public ServerRunner(Console console) {
        this.console = console;
    }

    public void run(Runnable stopServer) {
        console.println("Команда сервера: exit");
        console.prompt();
        while (console.isCanReadln()) {
            String name = console.readln().trim().toLowerCase();
            if ("exit".equals(name)) {
                LOGGER.info("Server stop requested from the local console");
                stopServer.run();
                return;
            }

            if (!name.isEmpty()) {
                console.println("Сервер принимает только команду exit.");
            }
            console.prompt();
        }
        stopServer.run();
    }
}
