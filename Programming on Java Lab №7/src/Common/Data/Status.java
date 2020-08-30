package Common.Data;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public enum Status implements Serializable {
    FIRED,
    HIRED,
    RECOMMENDED_FOR_PROMOTION,
    REGULAR,
    PROBATION;

    static final long serialVersionUID = 7211001911511381179L;

    Status() {
    }
}
