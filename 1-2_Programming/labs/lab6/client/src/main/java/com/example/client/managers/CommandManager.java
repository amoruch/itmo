package com.example.client.managers;

import com.example.client.DatagramClient;
import com.example.client.commands.*;
import com.example.client.utility.Ask;
import com.example.client.utility.Console;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;

import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;

/**
 * Handles interactive input, scripts, local commands, and server responses.
 */
public final class CommandManager {

    private final DatagramClient client;
    private final Console console;
    private final Map<String, Command> commands = new LinkedHashMap<>();
    private final Set<Path> activeScripts = new HashSet<>();
    private final Deque<Scanner> scriptScanners = new ArrayDeque<>();
    private final Deque<Ask> activeAsks = new ArrayDeque<>();
    private final Deque<LoopState> activeLoops = new ArrayDeque<>();

    public CommandManager(DatagramClient client, Console console) {
        this.client = client;
        this.console = console;
        registerCommands();
    }

    public void run() throws Exception {
        run(true);
    }

    private void run(boolean interactive) throws Exception {
        activeAsks.push(new Ask(console, interactive));
        LoopState loop = new LoopState();
        activeLoops.push(loop);
        try {
            while (loop.running) {
                String raw;
                try {
                    raw = ask().line(interactive ? "> " : "");
                } catch (EOFException exception) {
                    return;
                }
                if (raw.isBlank()) {
                    continue;
                }
                String[] parts = raw.split("\\s+", 2);
                String name = parts[0].toLowerCase();
                String argument = parts.length > 1 ? parts[1].trim() : "";
                Command command = commands.get(name);
                if (command == null) {
                    console.printError("Unknown command. Type help.");
                    continue;
                }
                command.execute(argument, this);
            }
        } finally {
            activeLoops.pop();
            activeAsks.pop();
        }
    }

    private void registerCommands() {
        register(new Help());
        register(new Info());
        register(new Show());
        register(new Add());
        register(new Update());
        register(new RemoveById());
        register(new Clear());
        register(new RemoveFirst());
        register(new AddIfMax());
        register(new RemoveLower());
        register(new MaxByHealth());
        register(new GroupCountingByCreationDate());
        register(new PrintDescending());
        register(new History());
        register(new ExecuteScript());
        register(new Exit());
    }

    private void register(Command command) {
        commands.put(command.getName(), command);
    }

    public void register(String name, Command command) {
        commands.put(name, command);
    }

    public Map<String, Command> getCommands() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(commands));
    }

    public Ask ask() {
        return activeAsks.peek();
    }

    public void send(Request request) throws Exception {
        Response response = client.request(request);
        if (response.ok()) {
            console.println(response.message());
        } else {
            console.printError(response.message());
        }
    }

    public void println(String message) {
        console.println(message);
    }

    public void printError(String message) {
        console.printError(message);
    }

    public void stopCurrentLoop() {
        activeLoops.peek().running = false;
    }

    public void executeScript(String argument) {
        if (argument.isBlank()) {
            console.printError("Usage: execute_script file_name");
            return;
        }
        Path script = Path.of(argument).toAbsolutePath().normalize();
        if (!activeScripts.add(script)) {
            console.printError("Recursive script call blocked: " + script);
            return;
        }
        try (Scanner scanner = new Scanner(script, StandardCharsets.UTF_8)) {
            scriptScanners.push(scanner);
            console.selectFileScanner(scanner);
            run(false);
        } catch (IOException exception) {
            console.printError("Script error: " + exception.getMessage());
        } catch (Exception exception) {
            console.printError("Script error: " + exception.getMessage());
        } finally {
            if (!scriptScanners.isEmpty()) {
                scriptScanners.pop();
            }
            if (scriptScanners.isEmpty()) {
                console.selectConsoleScanner();
            } else {
                console.selectFileScanner(scriptScanners.peek());
            }
            activeScripts.remove(script);
        }
    }

    private static final class LoopState {

        private boolean running = true;
    }
}
