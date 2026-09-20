package commands;

import managers.*;
import utility.*;

/**
 * Команда 'remove_by_id' - удаление элемента их коллекции по его id.
 */
public class RemoveById extends Command {

    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveById(Console console, CollectionManager collectionManager) {
        super("remove_by_id id", "удалить элемент из коллекции по его id");
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
            if (request.getArguments().length != 1) {
                return new ExecutionResponse(false, "Неправильное кол-во аргументов!\nИспользование: '" + getName() + "'");
            }

            long id;
            try {
                id = Long.parseLong(request.getArguments()[0]);
            } catch (NumberFormatException e) {
                return new ExecutionResponse(false, "Неправильный формат введенного id: должно быть числом.");
            }

            console.println("* Удаление элемента с id " + id + "...");

            if (collectionManager.byId((int) id) == null) {
                return new ExecutionResponse(false, "Ошибка: элемента не существует!");
            }
            collectionManager.remove(id);
            collectionManager.update();
            return new ExecutionResponse("Элемент успешно удален!");
        } catch (Exception e) {
            return new ExecutionResponse(false, "Что-то пошло не так...");
        }
    }
}
