package Common;

import Common.Data.Organization;
import Common.Data.Worker;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;

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
    private int id;
    private LocalDateTime localDateTime;
    private Organization organization;

    public Request(String request, Worker worker) {
        this.request = request;
        this.worker = worker;
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

    public int getId() {
        return id;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Request(String request, Organization organization) {
        this.request = request;
        this.organization = organization;
    }

    public Request(String request, LocalDateTime localDateTime) {
        this.request = request;
        this.localDateTime = localDateTime;
    }

    public Request(String request, int id) {
        this.request = request;
        this.id = id;
    }

    public Request(String request, File file) {
        this.request = request;
        this.file = file;
    }
}
