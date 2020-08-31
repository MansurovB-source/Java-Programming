package Client;

import Client.Exception.ConnectionException;
import Common.Request;
import Common.User;

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
public class Connection {
    private static final String HOST = "localhost";
    private static final String ADDRESS = "127.0.0.1";
    private final int PORT;
    ByteBuffer byteBuffer = ByteBuffer.allocate(10240);
    byte[] bytes;
    User user;

    public User getUser() {
        return user;
    }

    public Connection(int PORT) {
        this.PORT = PORT;
    }

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
                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
                buffer.clear();
                int read = channel.read(byteBuffer);
                if (read != -1) {
                    bytes = new byte[read];
                    for (int i = 0; i < read; i++) {
                        bytes[i] = byteBuffer.get(i);
                    }
                    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    if(request.getRequest().equals("is_registered") || request.getRequest().equals("sign_in") || request.getRequest().equals("sign_up")) {
                        user = (User) ois.readObject();
                        byteBuffer.clear();
                    } else {
                        System.out.println(ois.readObject());
                        byteBuffer.clear();
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (oos != null) {
                    oos.close();
                }
            }
        }catch (ConnectionException e) {
            System.out.println("Ошибка подключения к серверу");
        } catch (IOException e) {
            System.out.println("Ошибка!!!" + e.getMessage());
        }
    }

    private SocketChannel connect(InetSocketAddress inetSocketAddress) throws ConnectionException {
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
