
import commands.*;
import managers.*;
import utility.*;

public class Main {

    public static void main(String[] args) {
        var console = new StandardConsole();

        if (args.length == 0) {
            console.println("Введите имя загружаемого файла как аргумент командной строки");
            System.exit(1);
        }

        var dumpManager = new DumpManager(args[0], console);
        var collectionManager = new CollectionManager(dumpManager);
        if (!collectionManager.init()) {
            System.exit(1);
        }

        var commandManager = new CommandManager() {
            {
                register("add", new Add(console, collectionManager));
                register("show", new Show(console, collectionManager));
                register("help", new Help(console, this));
                register("history", new History(console, this));
                register("exit", new Exit(console));
                register("info", new Info(console, collectionManager));
                register("clear", new Clear(console, collectionManager));
                register("save", new Save(console, collectionManager));
                register("remove_by_id", new RemoveById(console, collectionManager));
                register("update", new Update(console, collectionManager));
                register("execute_script", new ExecuteScript(console));
                register("remove_first", new RemoveFirst(console, collectionManager));
                register("add_if_max", new AddIfMax(console, collectionManager));
                register("remove_lower", new RemoveLower(console, collectionManager));
                register("max_by_health", new MaxByHealth(console, collectionManager));
                register("group_counting_by_creation_date", new GroupCountingByCreationDate(console, collectionManager));
                register("print_descending", new PrintDescending(console, collectionManager));
            }
        };

        new Runner(console, commandManager, collectionManager).interactiveMode();
    }
}
