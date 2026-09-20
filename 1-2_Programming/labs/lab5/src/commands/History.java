package commands;

import managers.*;
import utility.*;

/**
 * Команда 'history' - выводит историю введенных команд.
 */
public class History extends Command {

    private final Console console;
    private final CommandManager commandManager;

    public History(Console console, CommandManager commandManager) {
        super("history", "вывести историю введенных команд");
        this.console = console;
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды и сообщение об успешности.
     */
    @Override
    public ExecutionResponse apply(Request request) {
        try {
            if (request.getArguments().length > 0) {
                return new ExecutionResponse(false, "Неправильное кол-во аргументов!\nИспользование: '" + getName() + "'");
            }

            console.println("* Вывод истории введенных команд:");

            var history = commandManager.getCommandHistory();
            for (String command : history) {
                System.out.println(command);
            }

            return new ExecutionResponse("История успешно выведена!");
        } catch (Exception e) {
            return new ExecutionResponse(false, "Что-то пошло не так...");
        }
    }
}
