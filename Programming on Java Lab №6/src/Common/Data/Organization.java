package Common.Data;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Author: Behruz Mansurov
 */
public class Organization implements Serializable {
    static final long serialVersionUID = 4011298202669741884L;
    private long employeesCount; //Значение поля должно быть больше 0
    private OrganizationType type; //Поле не может быть null
    private Address officialAddress; //Поле может быть null

    public Organization(long employeesCount, OrganizationType type, Address officialAddress) {
        this.employeesCount = employeesCount;
        this.type = type;
        this.officialAddress = officialAddress;
    }

    public long getEmployeesCount() {
        return employeesCount;
    }

    public void setEmployeesCount(long employeesCount) {
        this.employeesCount = employeesCount;
    }

    public OrganizationType getType() {
        return type;
    }

    public void setType(OrganizationType type) {
        this.type = type;
    }

    public Address getOfficialAddress() {
        return officialAddress;
    }

    public void setOfficialAddress(Address officialAddress) {
        this.officialAddress = officialAddress;
    }

    @Override
    public String toString() {
        return "Organization { " +
                "employeesCount = " + employeesCount +
                ", type = " + type +
                ", officialAddress = " + officialAddress +
                " }";
    }
}
