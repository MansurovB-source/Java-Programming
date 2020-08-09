package Data;

/**
 * Created by IntelliJ IDEA.
 * Author: Behruz Mansurov
 */
public enum OrganizationType {
    COMMERCIAL,
    TRUST,
    PRIVATE_LIMITED_COMPANY,
    OPEN_JOINT_STOCK_COMPANY;

    OrganizationType() {
    }

    @Override
    public String toString() {
        return "OrganizationType{}";
    }
}
