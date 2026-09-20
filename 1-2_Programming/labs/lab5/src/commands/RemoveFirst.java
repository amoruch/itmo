package commands;

import managers.*;
import utility.*;

/**
 * Команда 'remove_first' - удаляет первый элемент из коллекции.
 */
public class RemoveFirst extends Command {

    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveFirst(Console console, CollectionManager collectionManager) {
        super("remove_first", "удалить первый элемент из коллекции");
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

            console.println("* Удаление первого элемента коллекции...");

            if (!collectionManager.removeFirst()) {
                return new ExecutionResponse(false, "Коллекция пуста!");
            }

            return new ExecutionResponse("Первый элемент коллекции успешно удален!");
        } catch (Exception e) {
            return new ExecutionResponse(false, "Что-то пошло не так...");
        }
    }
}
