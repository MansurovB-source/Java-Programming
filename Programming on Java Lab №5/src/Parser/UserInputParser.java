package Parser;

import Data.Address;
import Data.Coordinates;
import Data.Organization;
import Data.OrganizationType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * Author: Behruz Mansurov
 */
public class UserInputParser {
    private Scanner in = new Scanner(System.in);
    private String read;
    private boolean check = false;
    private long cnt;
    ZonedDateTime zonedDateTime;
    private ZoneId timeZone = ZoneId.systemDefault();

    public String inputName() {
        while (!check) {
            System.out.print("Введите имя: ");
            if (in.hasNextLine()) {
                read = in.nextLine();
                if (read != null && !read.equals("")) {
                    check = true;
                    System.out.println();
//                    in.nextLine();
                } else
                    System.out.println("Wrong value please repeat!");
                System.out.println();
            } else {
                System.out.println("Wrong value please repeat!");
                System.out.println();
                in.next();
            }
        }
        check = false;
        return read;
    }

    public int inputCoordinatesX() {
        System.out.println("Coordinates { \n" +
                "   x: \n" +
                "   y: \n" +
                "}");
        while (!check) {
            System.out.println("Введите x: ");
            System.out.println("Введите целое число!\n" +
                    "Значение поля должно быть больше -912");
            if (in.hasNextInt()) {
                if ((cnt = in.nextInt()) > -912) {
                    check = true;
                    System.out.println();
//                    in.nextLine();
                } else
                    System.out.println("Wrong value please repeat!");
                System.out.println();
            } else {
                System.out.println("Wrong value please repeat!");
                System.out.println();
                in.next();
                check = false;
            }
        }
        check = false;
        return (int) cnt;
    }

    public long inputCoordinatesY() {
        while (!check) {
            System.out.println("Введите y: ");
            System.out.println("Введите целое число!\n" +
                    "Значение поля должно быть больше 139, \n" +
                    "Поле не может быть null");
            if (in.hasNextLong()) {
                cnt = in.nextLong();
                if (cnt > 139) {
                    check = true;
                    System.out.println();
//                    in.nextLine();
                } else
                    System.out.println("Wrong value please repeat!");
                System.out.println();
            } else {
                System.out.println("Wrong value please repeat!");
                System.out.println();
                in.next();
                check = false;
            }
        }
        check = false;
        return cnt;
    }

    public Double inputSalary() {
        Double salary = null;
        while (!check) {
            System.out.println("Введите зарплату: ");
            System.out.println("-1 это null\n" +
                    "Значение поля должно быть больше 0");
            if (in.hasNextDouble()) {
                salary = in.nextDouble();
                if(salary == -1) {
                    salary = null;
                    check = true;
                } else if(salary > 0) {
                    check = true;
                    System.out.println();
//                    in.nextLine();
                } else
                    System.out.println("Wrong value please repeat!");
                System.out.println();
            } else {
                System.out.println("Wrong value please repeat!");
                System.out.println();
                in.next();
                check = false;
            }
        }
        check = false;
        return salary;
    }

    public ZonedDateTime inputStartDate() {
        System.out.println("Введите дату поступления в формате YYYY-MM-DD HH:MM:SS");
        read = in.nextLine();
        zonedDateTime = LocalDateTime.parse(read,
                DateTimeFormatter.ISO_DATE_TIME).atZone(timeZone);
        return zonedDateTime;
    }

    public ZonedDateTime inputEndDate() {
        System.out.println("Введите дату окончания в формате YYYY-MM-DD HH:MM:SS");
        read = in.nextLine();
        if(read.equals("-1")) {
            zonedDateTime = null;
        }
        zonedDateTime = LocalDateTime.parse(read,
                DateTimeFormatter.ISO_DATE_TIME).atZone(timeZone);
        return zonedDateTime;
    }

    public long inputOrganizationEmployeesCount() {
        while (!check) {
            System.out.println("Введите количество сотрудников: ");
            System.out.println("Введите целое число!\n" +
                    "Значение поля должно быть больше 0");
            if (in.hasNextLong()) {
                cnt = in.nextLong();
                if (cnt > 0) {
                    check = true;
                    System.out.println();
                    in.nextLine();
                } else
                    System.out.println("Wrong value please repeat!");
                System.out.println();
            } else {
                System.out.println("Wrong value please repeat!");
                System.out.println();
                in.next();
                check = false;
            }
        }
        check = false;
        return cnt;
    }

    public OrganizationType inputOrganizationType() {
        System.out.println("Тип организации:\n" +
                "    COMMERCIAL,\n" +
                "    TRUST,\n" +
                "    PRIVATE_LIMITED_COMPANY,\n" +
                "    OPEN_JOINT_STOCK_COMPANY;");
        while (!check) {
            System.out.print("Введите тип организации: ");
            if (in.hasNextLine()) {
                read = in.nextLine();
                if (read.equals(OrganizationType.COMMERCIAL.toString()) || read.equals(OrganizationType.PRIVATE_LIMITED_COMPANY.toString())
                        || read.equals(OrganizationType.PRIVATE_LIMITED_COMPANY.toString())
                        || read.equals(OrganizationType.TRUST.toString())) {
                    check = true;
                    System.out.println();
                } else
                    System.out.println("Wrong value please repeat!");
                System.out.println();
            } else {
                System.out.println("Wrong value please repeat! ");
                System.out.println();
                in.next();
            }
        }
        check = false;
        return OrganizationType.valueOf(read);
    }

    private String inputAddressStreet() {
        while (!check) {
            System.out.print("Введите адрес: ");
            if (in.hasNextLine()) {
                read = in.nextLine();
                if(read.equals("-1")) {
                    read = null;
                    check = true;
                } else if (read != null) {
                    check = true;
                    System.out.println();
//                    in.nextLine();
                } else
                    System.out.println("Wrong value please repeat!");
                    System.out.println();
            } else {
                System.out.println("Wrong value please repeat! ");
                System.out.println();
                in.next();
            }
        }
        check = false;
        return read;
    }

    private String inputAddressZipCode() {
        while (!check) {
            System.out.print("Введите почтовый индекс: ");
            if (in.hasNextLine()) {
                read = in.nextLine();
                if (read != null) {
                    check = true;
                    System.out.println();
                } else
                    System.out.println("Wrong value please repeat!");
                    System.out.println();
            } else {
                System.out.println("Wrong value please repeat! ");
                System.out.println();
                in.next();
            }
        }
        check = false;
        return read;
    }

    public Address inputAddress() {
        System.out.println("Адресс может быть null\n" +
                "Если хотите чтобы адресс был null-ом\n" +
                "Введите -1");
        while(!check) {
            if(in.hasNextInt()) {
                cnt = in.nextInt();
                if(cnt == -1) {
                    break;
                } else {
                    return new Address(inputAddressStreet(), inputAddressZipCode());
                }
            }
        }
        return null;
    }

    public Organization inputOrganization() {
        System.out.println("Организация может быть null\n" +
                "Если хотите чтобы организация была null-ом\n" +
                "Введите -1");
        while(!check) {
            if(in.hasNextInt()) {
                cnt = in.nextInt();
                if(cnt == -1) {
                    break;
                } else {
                    return new Organization(inputOrganizationEmployeesCount(),inputOrganizationType(), inputAddress());
                }
            }
        }
        return null;
    }
}