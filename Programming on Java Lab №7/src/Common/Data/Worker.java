package Common.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Created by IntelliJ IDEA.
 * Author: Behruz Mansurov
 */
public class Worker implements Comparable<Worker>, Serializable {
    static final long serialVersionUID = 5747945029602321277L;

    private static long idCounter = 0;

    public static long setID() {
        return ++idCounter;
    }

    private static ZonedDateTime setCreationDate() {
        return ZonedDateTime.now();
    }

    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Double salary; //Поле может быть null, Значение поля должно быть больше 0
    private java.time.ZonedDateTime startDate; //Поле не может быть null
    private java.time.LocalDateTime endDate; //Поле может быть null
    private Status status; //Поле не может быть null
    private Organization organization; //Поле может быть null

    public Worker(String name, Coordinates coordinates, Double salary, ZonedDateTime startDate, LocalDateTime endDate, Status status, Organization organization) {
        this.id = setID();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = setCreationDate();
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.organization = organization;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public int compareTo(Worker other) {
        return Long.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Worker { " +
                "id = " + id +
                ", name = " + name +
                ", coordinates = " + coordinates +
                ", creationDate = " + creationDate +
                ", salary = " + salary +
                ", startDate = " + startDate +
                ", endDate = " + endDate +
                ", status = " + status +
                ", organization = " + organization +
                " }";
    }
}
