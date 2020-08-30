package Common.Data;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public enum OrganizationType implements Serializable {
    COMMERCIAL,
    TRUST,
    PRIVATE_LIMITED_COMPANY,
    OPEN_JOINT_STOCK_COMPANY;

    static final long serialVersionUID = 4340727448135346100L;

    OrganizationType() {
    }
}
