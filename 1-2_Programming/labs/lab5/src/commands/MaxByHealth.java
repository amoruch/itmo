package commands;

import managers.*;
import utility.*;

/**
 * Команда 'max_by_health' - выводит любой объект из коллекции, значение поля
 * health которого является максимальным.
 */
public class MaxByHealth extends Command {

    private final Console console;
    private final CollectionManager collectionManager;

    public MaxByHealth(Console console, CollectionManager collectionManager) {
        super("max_by_health", "вывести любой объект из коллекции, значение поля health которого является максимальным");
        this.console = console;
        this.collectionManager = collectionManager;
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

            console.println("* Вывод SpaceMarine с максимальным health: ");

            if (collectionManager.getCollection().isEmpty()) {
                return new ExecutionResponse(false, "Коллекция пуста!");
            }
            console.println(collectionManager.getCollection().getLast());

            return new ExecutionResponse("SpaceMarine с максимальным health выведен!");
        } catch (Exception e) {
            return new ExecutionResponse(false, "Что-то пошло не так...");
        }
    }
}
