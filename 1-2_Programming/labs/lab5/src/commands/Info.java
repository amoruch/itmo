package commands;

import java.time.LocalDateTime;
import managers.*;
import utility.*;

/**
 * Команда 'info' - выводит информацию о коллекции.
 */
public class Info extends Command {

    private final Console console;
    private final CollectionManager collectionManager;

    public Info(Console console, CollectionManager collectionManager) {
        super("info", "вывести информацию о коллекции");
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

            console.println("* Вывод информации о коллекции:");

            LocalDateTime lastInitTime = collectionManager.getLastInitTime();

            String lastInitTimeString = (lastInitTime == null) ? "в данной сессии инициализации еще не происходило"
                    : lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();

            LocalDateTime lastSaveTime = collectionManager.getLastSaveTime();
            String lastSaveTimeString = (lastSaveTime == null) ? "в данной сессии сохранений еще не происходило"
                    : lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();

            console.println("Тип: " + collectionManager.getCollection().getClass());
            console.println("Количество элементов: " + collectionManager.getCollection().size());
            console.println("Дата последней инициализации: " + lastInitTimeString);
            console.println("Дата последнего сохранения: " + lastSaveTimeString);

            return new ExecutionResponse("Информация успешно выведена!");
        } catch (Exception e) {
            return new ExecutionResponse(false, "Что-то пошло не так...");
        }
    }
}
