package Parser;

import Data.Worker;

/**
 * Created by IntelliJ IDEA.
 * Author: Behruz Mansurov
 */
public class WorkerParser {
    public static boolean parse(Worker worker) {
        return worker.getId() != 0 &&
                worker.getName() != null && !
                worker.getName().equals("") &&
                worker.getCoordinates() != null && worker.getCoordinates().getX() > -912 && worker.getCoordinates().getY() > 139 &&
                worker.getCreationDate() != null &&
                (worker.getSalary() == null || worker.getSalary() > 0) &&
                worker.getStartDate() != null &&
                worker.getStatus() != null &&
                (worker.getOrganization() == null || (worker.getOrganization().getEmployeesCount() > 0 &&
                        (worker.getOrganization().getOfficialAddress() == null ||
                                worker.getOrganization().getOfficialAddress().getZipCode() != null)));
    }
}
