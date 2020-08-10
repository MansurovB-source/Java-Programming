import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Created by IntelliJ IDEA.
 * Author: Behruz Mansurov
 */
public class Employee {
    private int id;
    private String name;
    private double salary;
    private LocalDateTime localDateTime;
    private LocalDateTime l;
    private ZonedDateTime zonedDateTime;

    public Employee(int id, String name, Double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.localDateTime = LocalDateTime.now();
        this.l = LocalDateTime.now().minusYears(1);
        this.zonedDateTime = ZonedDateTime.now();
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", localDateTime=" + localDateTime +
                ", l=" + l +
                ", zonedDateTime=" + zonedDateTime +
                '}';
    }
}

//    private static GsonBuilder getGsonBuilder() {
//        final GsonBuilder builder = new GsonBuilder();
//        builder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdaptor());
//        return builder;
//    }
//
