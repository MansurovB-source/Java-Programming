package Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class Server {
    public static void main(String[] args) {
        WorkerManager workerManager = null;
        int PORT = 555;
        try {
            if (args.length == 0 || args.length == 1) {
                throw new ArrayIndexOutOfBoundsException("Имя файла должно передоваться программе с " +
                        "помощью аргументов коммандной строки");
            } else if (args.length == 2) {
                workerManager = new WorkerManager(args[0]);
                PORT = Integer.parseInt(args[1]);
            }
        } catch (Exception e) {
            System.out.println("Что пошло не так");
            System.out.println(e.getMessage());
        }
        try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.bind(new InetSocketAddress(PORT));
            System.out.println("Сервер запущен");
            while (true) {
                System.out.println("Сервер ожидает клиентов(port = " + PORT + ")");
                SocketChannel client = serverSocket.accept();
                System.out.println("Подключён клиент:" + "" +
                        "\n\taddr = " + client.getLocalAddress() + ".");
                new Thread(new RequestHandler(client, workerManager)).start();
            }
        } catch (IOException e) {
            System.out.println("Не смогли подключится данному порту");
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(workerManager != null ? workerManager::save : null));
    }

}
