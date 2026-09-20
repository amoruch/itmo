package commands;

import utility.*;

/**
 * Команда 'exit' - завершает выполнение программы(без сохранения)
 */
public class Exit extends Command {

    private final Console console;

    public Exit(Console console) {
        super("exit", "закончить выполнение программы(без сохранения)");
        this.console = console;
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

            console.println("* Завершение программы...");

            return new ExecutionResponse("exit");
        } catch (Exception e) {
            return new ExecutionResponse(false, "Что-то пошло не так...");
        }
    }
}
