package commands;

import java.util.Map;
import managers.*;
import utility.*;

/**
 * Команда 'help' - выводит справку по доступным командам.
 */
public class Help extends Command {

    private final Console console;
    private final CommandManager commandManager;

    public Help(Console console, CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
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
            for (String a : request.getArguments()) {
                System.out.println(a);
            }
            if (request.getArguments().length > 0) {
                return new ExecutionResponse(false, "Неправильное кол-во аргументов!\nИспользование: '" + getName() + "'");
            }

            console.println("* Вывод справки по доступным командам:");

            var commands = commandManager.getCommands();
            for (Map.Entry<String, Command> item : commands.entrySet()) {
                String name = item.getKey();
                Command command = item.getValue();
                console.println(name + " - " + command.getDescription());
            }

            return new ExecutionResponse("Справка успешно выведена!");
        } catch (Exception e) {
            return new ExecutionResponse(false, "Что-то пошло не так...");
        }
    }
}
