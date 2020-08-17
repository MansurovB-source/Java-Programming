package Client;

import Client.Exception.ConnectionException;
import Common.Request;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class Client {
    private static final String HOST = "localhost";
    private static final String ADDRESS = "127.0.0.1";
    private int PORT;
    ByteBuffer byteBuffer = ByteBuffer.allocate(10240);
    byte[] bytes;

    public void sendRequest(Request request) {
        try {
            ByteArrayOutputStream baos;
            ObjectOutputStream oos = null;
            try (SocketChannel channel = connect(new InetSocketAddress(HOST, PORT))) {
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(request);
                ByteBuffer buffer = ByteBuffer.allocate(baos.size());
                buffer.put(baos.toByteArray());
                buffer.flip();
                channel.write(buffer);
                int read = channel.read(byteBuffer);
                if (read != -1) {
                    bytes = new byte[read];
//                byteBuffer.remaining();
                    for (int i = 0; i < read; i++) {
                        bytes[i] = byteBuffer.get(i);
                    }
                    System.out.println(bytes.toString());
                }
            } finally {
                oos.close();
            }
        }catch (ConnectionException e) {
            System.out.println("Ошибка подключения к серверу");
        } catch (IOException e) {
            System.out.println("Ошибка!!!");
            e.printStackTrace();
        }
    }

    public SocketChannel connect(InetSocketAddress inetSocketAddress) throws ConnectionException {
        int countReconnect = 0;
        SocketChannel socketChannel;
        try {
            socketChannel = SocketChannel.open(inetSocketAddress);
        } catch (IOException e) {
            System.out.println("Не удается подключиться к серверу. Ожидайте ...");
            while (true) {
                try {
                    countReconnect++;
                    Thread.sleep(1000);
                    socketChannel = SocketChannel.open(inetSocketAddress);
                    break;
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                } catch (IOException ex) {
                    System.out.println("Не удается подключиться к серверу. Ожидайте ...");
                    if(countReconnect > 4) {
                        Scanner scanner = new Scanner(System.in);
                        String command;
                        do {
                            System.out.println("Хотите возобновить подключение?\n" +
                                    "Введите Y/N: ");
                            command = scanner.nextLine().toUpperCase();
                        } while (!(command.equals("Y") | command.equals("N")));
                        if(command.equals("Y")) {
                            countReconnect++;
                        } else {
                            throw new ConnectionException();
                        }
                    }
                }
            }
        }
        return socketChannel;
    }
}
