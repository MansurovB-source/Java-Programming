import Manager.CommandManager;

/**
 * Created by IntelliJ IDEA.
 * Author: Behruz Mansurov
 */
public class Main {
    public static void main(String[] args) {
        String file = null;
        try {
            file = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Вы забыли имя файла");
        }
        CommandManager commandManager = new CommandManager(file);
        commandManager.parser();

    }
}
