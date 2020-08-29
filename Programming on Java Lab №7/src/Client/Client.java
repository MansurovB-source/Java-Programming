package Client;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class Client {
    public static void main(String[] args) {
        int PORT = 555;
        try {
            PORT = Integer.parseInt(args[0]);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Вы забыли порт");
        }
        RequestSender requestSender = new RequestSender(PORT);
        requestSender.parser();
    }
}
