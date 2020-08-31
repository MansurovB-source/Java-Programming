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
        final String URL = "jdbc:postgresql://localhost:5432/postgres";
        final String LOGIN = "postgres";
        final String PASSWORD = "postgresql4800";

        WorkerManager workerManager = null;
        DataBaseManager dataBaseManager = null;

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
            serverSocket.socket().bind(new InetSocketAddress(PORT));
            serverSocket.configureBlocking(false);
            System.out.println("Сервер запущен");
            System.out.println("Сервер ожидает клиентов(port = " + PORT + ")");
            while (true) {
                SocketChannel client = serverSocket.accept();
                if (client != null) {
                    System.out.println("Подключён клиент:" + "" +
                            "\n\taddr = " + client.getRemoteAddress() + ".");
                    new Thread(new RequestHandler(client, workerManager)).start();
                    System.out.println("Сервер ожидает клиентов(port = " + PORT + ")");
                }
            }
        } catch (IOException e) {
            System.out.println("Не смогли подключится данному порту");
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(workerManager != null ? workerManager::save : null));
    }

}
