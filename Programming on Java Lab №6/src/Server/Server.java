package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class Server {
    public static void main(String[] args) {
        int PORT = 0;
        try {
            if (args.length == 0) {
                throw new ArrayIndexOutOfBoundsException("Имя файла должно передоваться программе с " +
                        "помощью аргументов коммандной строки");
            } else if (args.length == 2) {
                WorkerManager workerManager = new WorkerManager(args[0]);
                PORT = Integer.parseInt(args[1]);
            }
        } catch (Exception e) {
            System.out.println("Что пошло не так");
            System.out.println(e.getMessage());
        }

        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен");
            Socket client = serverSocket.accept();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
