package Common;

import Common.Data.Organization;
import Common.Data.Worker;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class Request implements Serializable {
    static final long serialVersionUID = 3198040683532776052L;
    private String request;
    private Worker worker;
    private File file;
    private long id;
    private ZonedDateTime startdate;
    private Organization organization;
    String login;
    String password;
    User user;

    public Request(String request, String login) {
        this.request = request;
        this.login = login;
    }

    public Request(String request, User user) {
        this.request = request;
        this.user = user;
    }

    public Request(String request, Worker worker, User user) {
        this.request = request;
        this.worker = worker;
        this.user = user;
    }

    public Request(String request, Organization organization, User user) {
        this.request = request;
        this.organization = organization;
        this.user = user;
    }

    public Request(String request, String login, String password) {
        this.request = request;
        this.login = login;
        this.password = password;
    }

    public Request(String request, ZonedDateTime startdate, User user) {
        this.request = request;
        this.startdate = startdate;
        this.user = user;
    }

    public Request(String request, long id, User user) {
        this.request = request;
        this.id = id;
        this.user = user;
    }

    public Request(String request, File file) {
        this.request = request;
        this.file = file;
    }

    public Request(String request, long id, Worker worker, User user) {
        this.request = request;
        this.id = id;
        this.worker = worker;
        this.user = user;
    }

    public String getRequest() {
        return request;
    }

    public Worker getWorker() {
        return worker;
    }

    public File getFile() {
        return file;
    }

    public long getId() {
        return id;
    }

    public ZonedDateTime getLocalDateTime() {
        return startdate;
    }

    public Organization getOrganization() {
        return organization;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public User getUser() {
        return user;
    }
}
