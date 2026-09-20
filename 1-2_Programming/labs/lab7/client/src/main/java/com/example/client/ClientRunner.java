package com.example.client;

import com.example.client.commands.Command;
import com.example.client.commands.ExecutionResult;
import com.example.client.commands.ExecutionStatus;
import com.example.client.managers.CommandManager;
import com.example.client.managers.DatagramClient;
import com.example.client.utility.Ask;
import com.example.client.utility.Console;
import com.example.common.Model.SpaceMarine;
import com.example.common.Protocol.Marines;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.common.Protocol.Text;
import com.example.common.Protocol.Credentials;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Scanner;
import java.util.List;
import java.util.Locale;

/**
 * Runs the client input loop and provides commands with console and network
 * services.
 */
public final class ClientRunner {

    private static final DateTimeFormatter TABLE_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm z", Locale.ROOT);
    private final DatagramClient client;
    private final Console console;
    private final CommandManager commandManager;
    private Credentials credentials;
    private final Deque<Scanner> scriptScanners = new ArrayDeque<>();
    private final Deque<Ask> activeAsks = new ArrayDeque<>();
    private final Deque<LoopState> activeLoops = new ArrayDeque<>();

    public ClientRunner(DatagramClient client, Console console, CommandManager commandManager) {
        this.client = client;
        this.console = console;
        this.commandManager = commandManager;
    }

    public void run() {
        runLoop(true);
    }

    public ExecutionResult runScript(Path script) {
        try (Scanner scanner = new Scanner(script, StandardCharsets.UTF_8)) {
            scriptScanners.push(scanner);
            console.selectFileScanner(scanner);
            return runLoop(false);
        } catch (IOException exception) {
            return ExecutionResult.failure("Script error: " + exception.getMessage());
        } catch (Exception exception) {
            return ExecutionResult.failure("Script error: " + exception.getMessage());
        } finally {
            if (!scriptScanners.isEmpty()) {
                scriptScanners.pop();
            }
            if (scriptScanners.isEmpty()) {
                console.selectConsoleScanner();
            } else {
                console.selectFileScanner(scriptScanners.peek());
            }
        }
    }

    public Ask ask() {
        return activeAsks.peek();
    }

    public ExecutionResult send(Request request) throws Exception {
        try {
            Request authenticatedRequest = request.credentials() == null
                    ? new Request(request.command(), request.payload(), credentials)
                    : request;
            Response response = client.request(authenticatedRequest);
            return display(response);
        } catch (SocketTimeoutException exception) {
            return ExecutionResult.failure(
                    "Сервер временно недоступен. Попробуйте выполнить команду позже.");
        }
    }

    public void authorize(Credentials newCredentials) {
        credentials = newCredentials;
    }

    public boolean isAuthorized() {
        return credentials != null;
    }

    public void println(String message) {
        console.println(message);
    }

    public void printError(String message) {
        console.printError(message);
    }

    public Map<String, Command> commands() {
        return commandManager.getCommands();
    }

    private ExecutionResult runLoop(boolean interactive) {
        activeAsks.push(new Ask(console, interactive));
        LoopState loop = new LoopState();
        activeLoops.push(loop);
        try {
            while (loop.running) {
                String raw;
                try {
                    raw = ask().line(interactive ? "> " : "");
                } catch (EOFException exception) {
                    return ExecutionResult.success("");
                } catch (IOException exception) {
                    return ExecutionResult.failure("Input error: " + exception.getMessage());
                }
                if (raw.isBlank()) {
                    continue;
                }

                String[] parts = raw.split("\\s+", 2);
                String name = parts[0].toLowerCase();
                String argument = parts.length > 1 ? parts[1].trim() : "";
                Command command = commandManager.getCommands().get(name);
                if (command == null) {
                    printResult(ExecutionResult.failure("Unknown command. Type help."));
                    continue;
                }
                ExecutionResult result = command.execute(argument, this);
                printResult(result);
                if (result.status() == ExecutionStatus.EXIT) {
                    return result;
                }
            }
        } finally {
            activeLoops.pop();
            activeAsks.pop();
        }
        return ExecutionResult.success("");
    }

    private void printResult(ExecutionResult result) {
        if (result.message().isBlank()) {
            return;
        }
        if (result.status() == ExecutionStatus.FAILURE) {
            console.printError(result.message());
        } else {
            console.println(result.message());
        }
    }

    private ExecutionResult display(Response response) {
        if (response.payload() instanceof Text text) {
            return response.ok()
                    ? ExecutionResult.success(text.text())
                    : ExecutionResult.failure(text.text());
        }
        if (response.payload() instanceof Marines marines) {
            if (!response.ok()) {
                return ExecutionResult.failure("Server returned an invalid collection response.");
            }
            printMarines(marines.marines());
            return ExecutionResult.success("");
        }
        return ExecutionResult.failure("Server returned an unsupported response payload.");
    }

    private void printMarines(List<SpaceMarine> marines) {
        if (marines.isEmpty()) {
            console.println("Коллекция пуста!");
            return;
        }
        List<String> headers = List.of("id", "name", "x", "y", "creation date", "health", "loyal",
                "category", "melee weapon", "chapter", "chapter world");
        List<List<String>> rows = marines.stream().map(marine -> List.of(
                String.valueOf(marine.id()),
                marine.name(),
                String.valueOf(marine.coordinates().x()),
                String.valueOf(marine.coordinates().y()),
                TABLE_DATE_FORMAT.format(marine.creationDate()),
                String.valueOf(marine.health()),
                String.valueOf(marine.loyal()),
                marine.category().toString(),
                marine.meleeWeapon().toString(),
                marine.chapter() == null ? "-" : marine.chapter().name(),
                marine.chapter() == null ? "-" : marine.chapter().world()
        )).toList();
        console.printTable(headers, rows);
    }

    private static final class LoopState {

        private boolean running = true;
    }
}
