package Parser;

import Data.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * Author: Behruz Mansurov
 */
public class UserInputParser {
    private Scanner in = new Scanner(System.in);
    private String read;
    private boolean check = false;
    private long cnt;
    private ZoneId timeZone = ZoneId.systemDefault();
    private final static String DataTime_Pattern = "((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][1-9]|3[01])T(0?[0-9]|1[1-9]|2[1-3]):(0?[0-9]|[1-5][0-9]):(0?[0-9]|[1-5][0-9])";
    private static Pattern pattern = Pattern.compile(DataTime_Pattern);
    public long inputId() {
        System.out.print("Введите id: ");
//        in.nextLine();
        while(!check) {
            if (in.hasNextLong()) {
                cnt = in.nextLong();
                in.nextLine();
                if (cnt > 0) {
                    check = true;
//                    in.nextLine();
                } else {
                    System.out.println("Wrong value please repeat!");
//                    in.nextLine();
                }
            } else {
                System.out.println("Wrong value please repeat!");
//                in.nextLine();
                check = false;
            }
        }
        check = false;
        return cnt;
    }


    public String inputName() {
        System.out.print("Введите имя: ");
        while (!check) {
            if (in.hasNextLine()) {
                read = in.nextLine();
                if (read != null && !read.equals("")) {
                    check = true;
                } else {
                    System.out.println("Wrong value please repeat!");
                }
            } else {
                System.out.println("Wrong value please repeat!");
            }
        }
        check = false;
        return read;
    }

    public Coordinates inputCoordinates() {
        System.out.println("Координата может быть null-ом\n" +
                "Если хотите чтобы координата была null-ом\n" +
                "Введите -1");
        while (!check) {
            if (in.hasNextInt()) {
                cnt = in.nextInt();
                in.nextLine();
                if (cnt == -1) {
                    break;
                } else {
                    return new Coordinates(inputCoordinatesX(), inputCoordinatesY());
                }
            } else {
                System.out.println("Введите корректные данные");
                in.nextLine();
            }
        }
        return null;
    }

    public Double inputSalary() {
        Double salary = null;
        System.out.println("Введите зарплату: ");
        System.out.println("-1 это null\n" +
                "Значение поля должно быть больше 0");
//        in.nextLine();
        while (!check) {
            if (in.hasNextDouble()) {
                salary = in.nextDouble();
                if (salary == -1) {
                    salary = null;
                    check = true;
                    in.nextLine();
                } else if (salary > 0) {
                    check = true;
                    in.nextLine();
                } else {
                    System.out.println("Wrong value please repeat!");
                    in.nextLine();
                }
            } else {
                System.out.println("Wrong value please repeat!");
                in.nextLine();
                check = false;
            }
        }
        check = false;
        return salary;
    }

    public ZonedDateTime inputStartDate() {
        ZonedDateTime zonedDateTime = null;
        System.out.println("Введите дату поступления в формате YYYY-MM-DDTHH:MM:SS");
        while(!check) {
            read = in.nextLine();
            Matcher matcher = pattern.matcher(read);
            if (matcher.matches()) {
                zonedDateTime = LocalDateTime.parse(read,
                        DateTimeFormatter.ISO_DATE_TIME).atZone(timeZone);
                check = true;
            } else System.out.println("Введите корректные данные!");
        }
        check = false;
        return zonedDateTime;
    }

    public LocalDateTime inputEndDate() {
        LocalDateTime localDateTime = null;
        System.out.println("Введите дату окончания в формате YYYY-MM-DDTHH:MM:SS");
        System.out.println("Дата окончания может быть null-ом\n" +
                " -1 это null");
        while (!check) {
            read = in.nextLine();
            if (read.equals("-1")) {
                localDateTime = null;
                check = true;
            } else {
                Matcher matcher = pattern.matcher(read);
                if (matcher.matches()) {
                    localDateTime = LocalDateTime.parse(read, DateTimeFormatter.ISO_DATE_TIME);
                    check = true;
                } else System.out.println("Введите корректные данные!");
            }
        }
        check = false;
        return localDateTime;
    }

    public Status inputStatus() {
        System.out.println("Статус:\n" +
                "    FIRED,\n" +
                "    HIRED,\n" +
                "    RECOMMENDED_FOR_PROMOTION,\n" +
                "    REGULAR,\n" +
                "    PROBATION;" +
                ""
        );
        while (!check) {
            System.out.print("Введит Статус: ");
            if (in.hasNextLine()) {
                read = in.nextLine();
                if (read.equals(Status.FIRED.toString()) || read.equals(Status.HIRED.toString())
                        || read.equals(Status.RECOMMENDED_FOR_PROMOTION.toString())
                        || read.equals(Status.REGULAR.toString()) || read.equals(Status.PROBATION.toString())) {
                    check = true;
                } else
                    System.out.println("Wrong value please repeat!");
            } else {
                System.out.println("Wrong value please repeat! 1");
            }
        }
        check = false;
        return Status.valueOf(read);
    }

    public Organization inputOrganization() {
        System.out.println("Организация может быть null-ом\n" +
                "Если хотите чтобы организация была null-ом\n" +
                "Введите -1");
        while (!check) {
            if (in.hasNextInt()) {
                cnt = in.nextInt();
                in.nextLine();
                if (cnt == -1) {
                    break;
                } else {
                    return new Organization(inputOrganizationEmployeesCount(), inputOrganizationType(), inputAddress());
                }
            } else {
                System.out.println("Введите корректные данные!");
                in.nextLine();
            }
        }
        return null;
    }

    public Address inputAddress() {
        System.out.println("Адресс может быть null-ом\n" +
                "Если хотите чтобы адресс был null-ом\n" +
                "Введите -1");
        while (!check) {
            if (in.hasNextInt()) {
                cnt = in.nextInt();
                in.nextLine();
                if (cnt == -1) {
                    break;
                } else {
                    return new Address(inputAddressStreet(), inputAddressZipCode());
                }
            } else {
                System.out.println("Введите корректные данные!");
                in.nextLine();
            }
        }
        return null;
    }


    //////////////////// Organization Methods ////////////////////
    private long inputOrganizationEmployeesCount() {
        System.out.println("Введите количество сотрудников: ");
        System.out.println("Введите целое число!\n" +
                "Значение поля должно быть больше 0");
        while (!check) {
            if (in.hasNextLong()) {
                cnt = in.nextLong();
                in.nextLine();
                if (cnt > 0) {
                    check = true;
//                    System.out.println();
//                    in.nextLine();
                } else
                    System.out.println("Wrong value please repeat!");
            } else {
                System.out.println("Wrong value please repeat!");
                in.nextLine();
                check = false;
            }
        }
        check = false;
        return cnt;
    }

    private OrganizationType inputOrganizationType() {
        System.out.println("Тип организации:\n" +
                "    COMMERCIAL,\n" +
                "    TRUST,\n" +
                "    PRIVATE_LIMITED_COMPANY,\n" +
                "    OPEN_JOINT_STOCK_COMPANY;" +
                "");
        while (!check) {
            System.out.print("Введите тип организации: ");
            if (in.hasNextLine()) {
                read = in.nextLine();
                if (read.equals(OrganizationType.COMMERCIAL.toString()) || read.equals(OrganizationType.PRIVATE_LIMITED_COMPANY.toString())
                        || read.equals(OrganizationType.PRIVATE_LIMITED_COMPANY.toString())
                        || read.equals(OrganizationType.TRUST.toString())) {
                    check = true;
                } else
                    System.out.println("Wrong value please repeat!");
            } else {
                System.out.println("Wrong value please repeat! ");
            }
        }
        check = false;
        return OrganizationType.valueOf(read);
    }


    //////////////////// Coordinates Methods ////////////////////
    private int inputCoordinatesX() {
        System.out.println("Coordinates { \n" +
                "   x: \n" +
                "   y: \n" +
                "}");
        System.out.println("Введите x: ");
        System.out.println("Введите целое число!\n" +
                "Значение поля должно быть больше -912");
        while (!check) {
            if (in.hasNextInt()) {
                if ((cnt = in.nextInt()) > -912) {
                    in.nextLine();
                    check = true;
                } else
                    System.out.println("Wrong value please repeat!");
            } else {
                System.out.println("Wrong value please repeat!");
                in.nextLine();
                check = false;
            }
        }
        check = false;
        return (int) cnt;
    }

    private long inputCoordinatesY() {
        System.out.println("Введите y: ");
        System.out.println("Введите целое число!\n" +
                "Значение поля должно быть больше 139, \n" +
                "Поле не может быть null");
        while (!check) {
            if (in.hasNextLong()) {
                cnt = in.nextLong();
                in.nextLine();
                if (cnt > 139) {
                    check = true;
                } else
                    System.out.println("Wrong value please repeat!");
            } else {
                System.out.println("Wrong value please repeat!");
                in.nextLine();
                check = false;
            }
        }
        check = false;
        return cnt;
    }


    //////////////////// Address Methods ////////////////////
    private String inputAddressStreet() {
        System.out.print("Введите адрес: ");
        while (!check) {
            if (in.hasNextLine()) {
                read = in.nextLine();
                if (read.equals("-1")) {
                    read = null;
                    check = true;
                } else if (read != null) {
                    check = true;
                } else
                    System.out.println("Wrong value please repeat!");
            } else {
                System.out.println("Wrong value please repeat! 1");
            }
        }
        check = false;
        return read;
    }

    private String inputAddressZipCode() {
        System.out.print("Введите почтовый индекс: ");
        while (!check) {
            if (in.hasNextLine()) {
                read = in.nextLine();
                if (read != null) {
                    check = true;
                } else
                    System.out.println("Wrong value please repeat!");
            } else {
                System.out.println("Wrong value please repeat! 1");
            }
        }
        check = false;
        return read;
    }


}