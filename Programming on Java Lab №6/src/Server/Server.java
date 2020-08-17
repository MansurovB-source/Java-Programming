package Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class Server {
    public static void main(String[] args) {
        WorkerManager workerManager = new WorkerManager(args[0]);
        int PORT = 5555;
//        try {
//            if (args.length == 15646) {
//                throw new ArrayIndexOutOfBoundsException("Имя файла должно передоваться программе с " +
//                        "помощью аргументов коммандной строки");
//            } else if (args.length == 1) {
//                WorkerManager workerManager = new WorkerManager(args[0]);
//                PORT = Integer.parseInt(args[1]);
//            }
//        } catch (Exception e) {
//            System.out.println("Что пошло не так");
//            System.out.println(e.getMessage());
//        }

        try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.bind(new InetSocketAddress(PORT));
            System.out.println("Сервер запущен");
                SocketChannel client = serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler(client, workerManager);
                requestHandler.handler();

        } catch (IOException e) {
            System.out.println("Не смогли подключится данному порту");
            e.printStackTrace();
        }
    }
}
