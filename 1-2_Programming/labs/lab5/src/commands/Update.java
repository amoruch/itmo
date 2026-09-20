package commands;

import managers.*;
import models.*;
import utility.*;

/**
 * Команда 'update' - обновляет значение элемента коллекции, id которого равен
 * заданному.
 */
public class Update extends Command {

    private final Console console;
    private final CollectionManager collectionManager;

    public Update(Console console, CollectionManager collectionManager) {
        super("update id", "обновить значение элемента коллекции, id которого равен заданному");
        this.console = console;
        this.collectionManager = collectionManager;
        this.requireData = true;
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

            if (collectionManager.byId((int) id) == null) {
                return new ExecutionResponse(false, "Ошибка: элемента не существует!");
            }

            SpaceMarine newbee = request.getSpaceMarine();

            console.println("* Обновление SpaceMarine:");

            if (newbee != null && newbee.validate()) {
                SpaceMarine old = collectionManager.byId((int) id);
                newbee.setCreationDate(old.getCreationDate()); // костыль
                collectionManager.update(newbee);
                collectionManager.update();
                return new ExecutionResponse("SpaceMarine успешно обновлен!");
            } else {
                return new ExecutionResponse(false, "Поля SpaceMarine не валидны! SpaceMarine не изменен!");
            }
        } catch (Exception e) {
            return new ExecutionResponse(false, "Отмена...");
        }
    }
}
