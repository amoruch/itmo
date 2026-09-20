package commands;

import managers.*;
import utility.*;

/**
 * Команда 'clear' - очищяет коллекцию.
 */
public class Clear extends Command {

    private final Console console;
    private final CollectionManager collectionManager;

    public Clear(Console console, CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
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

            console.println("* Очистка коллекции:");

            while (collectionManager.getCollection().isEmpty()) {
                int id = collectionManager.getCollection().getLast().getId();
                collectionManager.remove(id);
                console.println("Element with id " + id + " was removed...");
            }

            return new ExecutionResponse("Коллекция успешно очищена!");
        } catch (Exception e) {
            return new ExecutionResponse(false, "Что-то пошло не так...");
        }
    }
}
