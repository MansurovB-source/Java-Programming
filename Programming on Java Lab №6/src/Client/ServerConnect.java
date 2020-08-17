package Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class ServerConnect {
    private static final String HOST = "localhost";
    private static final String ADDRESS = "127.0.0.1";
    private int PORT;
    SocketChannel channel;

    public ServerConnect(int PORT) {
        this.PORT = PORT;
    }

    public void connect() {
        int tryConnect = 0;
       while(!channel.connect(new InetSocketAddress(HOST, PORT))) {

       }

    }

}
