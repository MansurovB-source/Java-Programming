package Client.Exception;

import Client.Client;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class ConnectionException extends Exception {
    public ConnectionException() {
    }

    public ConnectionException(String message) {
        super(message);
    }
}
