package Common;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class User implements Serializable {
    static final long serialVersionUID = -1160578898668997571L;
    String login;
    String password;

    public User(String login) {
        this.login = login;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
