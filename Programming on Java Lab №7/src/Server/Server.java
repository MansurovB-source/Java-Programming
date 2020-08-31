package Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        int PORT = 555;
        DataBaseManager dataBaseManager = new DataBaseManager(URL, LOGIN, PASSWORD);
        WorkerManager workerManager = new WorkerManager(dataBaseManager);
        UserManager userManager = new UserManager(dataBaseManager);
        try {
            if(args.length == 0) {
                throw new ArrayIndexOutOfBoundsException("Вы забыли порт");
            }
            if(args.length > 0) {
                PORT = Integer.parseInt(args[0]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());

        } catch (NumberFormatException e) {
            System.out.println("Введите целое число");
        }
        try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.socket().bind(new InetSocketAddress(PORT));
            serverSocket.configureBlocking(false);
            System.out.println("Сервер запущен");
            System.out.println("Сервер ожидает клиентов(port = " + PORT + ")");
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            while (true) {
                SocketChannel client = serverSocket.accept();
                if (client != null) {
                    System.out.println("Подключён клиент:" + "" +
                            "\n\taddr = " + client.getRemoteAddress() + ".");
                    //new Thread(new RequestHandler(client, workerManager, userManager)).start();
                    //new RequestHandler(client, workerManager, userManager).run();
                    executorService.submit(new RequestHandler(client, workerManager, userManager));
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
