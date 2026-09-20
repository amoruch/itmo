package com.example.client;

import com.example.client.managers.CommandManager;
import com.example.client.managers.DatagramClient;
import com.example.client.utility.Console;
import com.example.client.utility.StandardConsole;

/**
 * Entry point of the client application.
 */
public class Main {

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "127.0.0.1";
        int port;
        try {
            port = args.length > 1 ? Integer.parseInt(args[1]) : 8000;
            if (port < 1 || port > 65_535) {
                throw new NumberFormatException("port is outside 1..65535");
            }
        } catch (NumberFormatException exception) {
            System.err.println("Invalid port: " + exception.getMessage());
            return;
        }

        Console console = new StandardConsole();
        try (DatagramClient client = new DatagramClient(host, port)) {
            console.println("Client started. Server: " + host + ":" + port + ". Type help.");
            new ClientRunner(client, console, new CommandManager()).run();
        } catch (Exception exception) {
            console.printError("Client error: " + exception.getMessage());
        }
    }

}
