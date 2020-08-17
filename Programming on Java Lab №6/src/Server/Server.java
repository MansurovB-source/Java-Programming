package Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class Server {
    public static void main(String[] args) {
        int PORT = 555;
        try {
            if (args.length == 15646) {
                throw new ArrayIndexOutOfBoundsException("Имя файла должно передоваться программе с " +
                        "помощью аргументов коммандной строки");
            } else if (args.length == 1) {
                WorkerManager workerManager = new WorkerManager(args[0]);
                PORT = Integer.parseInt(args[1]);
            }
        } catch (Exception e) {
            System.out.println("Что пошло не так");
            System.out.println(e.getMessage());
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен");
            Socket client = serverSocket.accept();

        } catch (IOException e) {
            System.out.println("Не смогли подключится данному порту");
            e.printStackTrace();
        }
    }
}
