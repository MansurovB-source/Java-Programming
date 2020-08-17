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

    public Request(String request) {
        this.request = request;
    }

    public Request(String request, Worker worker) {
        this.request = request;
        this.worker = worker;
    }

    public Request(String request, Organization organization) {
        this.request = request;
        this.organization = organization;
    }

    public Request(String request, ZonedDateTime startdate) {
        this.request = request;
        this.startdate = startdate;
    }

    public Request(String request, long id) {
        this.request = request;
        this.id = id;
    }

    public Request(String request, File file) {
        this.request = request;
        this.file = file;
    }

    public Request(String request, long id,  Worker worker) {
        this.request = request;
        this.id = id;
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

    public long getId() {
        return id;
    }

    public ZonedDateTime getLocalDateTime() {
        return startdate;
    }

    public Organization getOrganization() {
        return organization;
    }

}
