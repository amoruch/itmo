package utility;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import managers.*;

public class Runner {

    private Console console;
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;
    private final List<String> scriptStack = new ArrayList<>();
    private int lengthRecursion = -1;

    public Runner(Console console, CommandManager commandManager, CollectionManager collectionManager) {
        this.console = console;
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
    }

    /**
     * Интерактивный режим
     */
    public void interactiveMode() {
        try {
            ExecutionResponse commandStatus;
            String[] userCommand = {"", ""};

            while (true) {
                console.prompt();
                userCommand = (console.readln().trim()).split(" ");

                commandManager.addToHistory(userCommand[0]);
                commandStatus = launchCommand(userCommand);

                if (commandStatus.getMessage().equals("exit")) {
                    break;
                }
                console.println(commandStatus.getMessage());
            }
        } catch (NoSuchElementException exception) {
            console.printError("Пользовательский ввод не обнаружен!");
        } catch (Ask.AskBreak exception) {
            console.printError("Проверьте скрипт на корректность введенных данных!");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
        }
    }

    /**
     * Проверяет рекурсивность выполнения скриптов.
     *
     * @param argument Название запускаемого скрипта
     * @return можно ли выполнить скрипт.
     */
    private boolean checkRecursion(String argument, Scanner scriptScanner) {
        var recStart = -1;
        var i = 0;
        for (String script : scriptStack) {
            i++;
            if (argument.equals(script)) {
                if (recStart < 0) {
                    recStart = i;
                }
                if (lengthRecursion < 0) {
                    console.selectConsoleScanner();
                    console.println("Была замечена рекурсия! Введите максимальную глубину рекурсии (0..500)");
                    while (lengthRecursion < 0 || lengthRecursion > 500) {
                        try {
                            console.print("> ");
                            lengthRecursion = Integer.parseInt(console.readln().trim());
                        } catch (NumberFormatException e) {
                            console.println("длина не распознана");
                        }
                    }
                    console.selectFileScanner(scriptScanner);
                }
                if (i > recStart + lengthRecursion || i > 500) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Режим для запуска скрипта.
     *
     * @param argument Аргумент скрипта
     * @return Код завершения.
     */
    private ExecutionResponse scriptMode(String argument) {
        String[] userCommand = {"", ""};
        StringBuilder executionOutput = new StringBuilder();

        if (!new File(argument).exists()) {
            return new ExecutionResponse(false, "Файл не существует!");
        }
        if (!Files.isReadable(Paths.get(argument))) {
            return new ExecutionResponse(false, "Прав для чтения нет!");
        }

        scriptStack.add(argument);
        try (Scanner scriptScanner = new Scanner(new File(argument))) {
            ExecutionResponse commandStatus;

            if (!scriptScanner.hasNext()) {
                throw new NoSuchElementException();
            }
            console.selectFileScanner(scriptScanner);
            do {
                userCommand = (console.readln().trim()).split(" ");
                while (console.isCanReadln() && userCommand[0].isEmpty()) {
                    userCommand = (console.readln().trim()).split(" ");
                }
                // executionOutput.append(console.getPrompt() + String.join(" ", userCommand) + "\n");
                var needLaunch = true;
                if (userCommand[0].equals("execute_script")) {
                    needLaunch = checkRecursion(userCommand[1], scriptScanner);
                }

                commandStatus = needLaunch ? launchCommand(userCommand) : new ExecutionResponse("превышена максимальная глубина рекурсии");
                if (userCommand[0].equals("execute_script")) {
                    console.selectFileScanner(scriptScanner);
                }
                // executionOutput.append(commandStatus.getMessage() + "\n");
            } while (commandStatus.getExitCode() && !commandStatus.getMessage().equals("exit") && console.isCanReadln());

            console.selectConsoleScanner();
            if (!commandStatus.getExitCode() && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty())) {
                executionOutput.append("Проверьте скрипт на корректность введенных данных!\n");
            }

            return new ExecutionResponse(commandStatus.getExitCode(), executionOutput.toString());
        } catch (FileNotFoundException exception) {
            return new ExecutionResponse(false, "");
        } catch (NoSuchElementException exception) {
            return new ExecutionResponse(false, "Файл со скриптом пуст!");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
            System.exit(0);
        } catch (Ask.AskBreak exception) {
            console.printError("Проверьте скрипт на корректность введенных данных!");
        } finally {
            scriptStack.remove(scriptStack.size() - 1);
        }
        return new ExecutionResponse("");
    }

    /**
     * Запускает команду.
     *
     * @param userCommand Команда для запуска
     * @return Код завершения.
     */
    private ExecutionResponse launchCommand(String[] userCommand) throws Ask.AskBreak {
        String commandName = userCommand[0];

        String[] arguments = new String[userCommand.length - 1];
        for (int i = 0; i < userCommand.length - 1; i++) {
            arguments[i] = userCommand[i + 1];
        }

        if (userCommand[0].equals("")) {
            return new ExecutionResponse("");
        }

        var command = commandManager.getCommands().get(commandName);

        if (command == null) {
            return new ExecutionResponse(false, "Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");
        }

        Request request = new Request(commandName, arguments);
        if (command.RequireData()) {
            request.setSpaceMarine(Ask.askSpaceMarine(console, collectionManager.getFreeId()));
        }

        switch (commandName) {
            case "execute_script" -> {
                ExecutionResponse tmp = commandManager.getCommands().get("execute_script").apply(request);
                if (!tmp.getExitCode()) {
                    return tmp;
                }
                ExecutionResponse tmp2 = scriptMode(arguments[0]);
                return new ExecutionResponse(tmp2.getExitCode(), tmp.getMessage() + "\n" + tmp2.getMessage().trim());
            }
            default -> {
                return command.apply(request);
            }
        }
    }

    /**
     * Исполняет user's request(from client's side)
     *
     * @param request Содержимое запроса
     * @return Код завершения.
     */
    public ExecutionResponse executeRequest(Request request) {
        if (request.getCommand().equals("")) {
            return new ExecutionResponse("");
        }
        var command = commandManager.getCommands().get(request.getCommand());

        if (command == null) {
            return new ExecutionResponse(false, "Команда '" + request.getCommand() + "' не найдена. Наберите 'help' для справки");
        }

        return command.apply(request);
    }
}
