package Server;

import Common.Data.*;
import Common.User;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class DataBaseManager {
    private final String URL;
    private final String LOGIN;
    private final String PASSWORD;

    public DataBaseManager(String URL, String LOGIN, String PASSWORD) {
        this.URL = URL;
        this.LOGIN = LOGIN;
        this.PASSWORD = PASSWORD;
    }

    public CopyOnWriteArrayList<Worker> getCollectionsFromDB() {
        String query = "SELECT w.id, w.name, c.x, c.y, w.salary, w.creationdate, w.startdate, w.enddate," +
                "w.status, o.employeecount, o.organizationtype, a.street, a.zipcode" +
                "   FROM workers AS w JOIN coordinates AS c ON w.id = c.w_id" +
                "       JOIN organizations AS o ON  w.id = o.w_id" +
                "           JOIN addresses AS a ON o.id = a.organization_id;";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            CopyOnWriteArrayList<Worker> workers = new CopyOnWriteArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getInt("id");
                ZonedDateTime creationdate = ZonedDateTime.ofInstant(resultSet.getTimestamp("creationdate").toInstant(), ZoneId.of("Europe/Moscow"));
                ZonedDateTime startdate = ZonedDateTime.ofInstant(resultSet.getTimestamp("startdate").toInstant(), ZoneId.of("Europe/Moscow"));
                Worker worker = new Worker(resultSet.getString("name"),
                        new Coordinates(resultSet.getInt("x"), resultSet.getLong("y")),
                        resultSet.getDouble("salary"), startdate,
                        resultSet.getTimestamp("enddate").toLocalDateTime(),
                        Status.valueOf((String) resultSet.getObject("status")),
                        new Organization(resultSet.getInt("employeecount"),
                                OrganizationType.valueOf((String) resultSet.getObject("organizationtype")),
                                new Address(resultSet.getString("street"),
                                        resultSet.getString("zipcode")))
                );
                worker.setId(id);
                worker.setCreationDate(creationdate);
                workers.add(worker);
            }
            return workers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addWorker(Worker worker, User user) {
        String query = "SELECT id FROM users WHERE login = ? and password = ?";
        String query1 = "" +
                "WITH" +
                "    iw AS (INSERT INTO workers (id, name, creationdate, salary, startdate, enddate, status, user_id)" +
                "                VALUES (?, ?, ?, ?, ?, ?, ?, ?)" +
                "                RETURNING id" +
                "    )," +
                "     ic AS (INSERT INTO coordinates (w_id, x, y)" +
                "                VALUES ((SELECT id FROM iw), ?, ?)" +
                "                RETURNING w_id" +
                "    )," +
                "     io AS (INSERT INTO organizations (w_id, employeecount, organizationtype)" +
                "                VALUES ((SELECT w_id FROM ic), ?, ?)" +
                "                RETURNING id" +
                "    )" +
                "INSERT" +
                "    INTO addresses(organization_id, street, zipcode)" +
                "        VALUES ((SELECT id FROM io), ?, ?);";

        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    long userId = resultSet.getInt("id");
                    try(PreparedStatement preparedStatement1 = connection.prepareStatement(query1)) {
                        preparedStatement1.setLong(1,worker.getId());
                        preparedStatement1.setString(2, worker.getName());
                        preparedStatement1.setTimestamp(3, Timestamp.from(worker.getCreationDate().toInstant()));
                        preparedStatement1.setDouble(4, worker.getSalary());
                        preparedStatement1.setTimestamp(5, Timestamp.from(worker.getStartDate().toInstant()));
                        preparedStatement1.setTimestamp(6, Timestamp.valueOf(worker.getEndDate()));
                        preparedStatement1.setObject(7, worker.getStatus(), Types.OTHER);
                        preparedStatement1.setLong(8, userId);
                        preparedStatement1.setInt(9, worker.getCoordinates().getX());
                        preparedStatement1.setLong(10, worker.getCoordinates().getY());
                        preparedStatement1.setLong(11, worker.getOrganization().getEmployeesCount());
                        preparedStatement1.setObject(12, worker.getOrganization().getType(), Types.OTHER);
                        preparedStatement1.setString(13,worker.getOrganization().getOfficialAddress().getStreet());
                        preparedStatement1.setString(14,worker.getOrganization().getOfficialAddress().getZipCode());
                        int result = preparedStatement1.executeUpdate();
                        return result > 0;
                    }
                } else {
                    return false;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public int removeWorker(Worker worker, User user) {
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT login, password FROM users " +
                "WHERE id = (SELECT user_id FROM workers WHERE id = ?)")) {
            preparedStatement.setLong(1, worker.getId());
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    String workerLogin = resultSet.getString("login");
                    String workerPassword = resultSet.getString("password");
                    if (workerLogin.equals(user.getLogin()) && workerPassword.equals(user.getPassword())) {
                        try (PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM workers WHERE id = ?")) {
                            preparedStatement1.setLong(1, worker.getId());
                            int  result = preparedStatement1.executeUpdate();
                            return result;
                        }
                    } else {
                        return -1;
                    }
                } else {
                    return 0;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int updateWorker(Worker worker, User user) {
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT login, password FROM users " +
                    "WHERE id = (SELECT user_id FROM workers WHERE users.id = ?)")) {
            preparedStatement.setLong(1, worker.getId());
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    String workerLogin = resultSet.getString("login");
                    String workerPassword = resultSet.getString("password");
                    if (workerLogin.equals(user.getLogin()) && workerPassword.equals(user.getPassword())) {
                        try (PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE worker SET id = ? WHERE id = ?")) {
                            preparedStatement1.setLong(1, worker.getId());
                            return preparedStatement1.executeUpdate();
                        }
                    } else {
                        return -1;
                    }
                } else {
                    return 0;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public boolean checkLoginAndPassword(User user) {
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT login, password FROM users " +
                "WHERE login = ? and password = ?")) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            return preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public User isRegitered(String login) {
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT  login FROM users WHERE login = ?")) {
            preparedStatement.setString(1, login);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return new User(resultSet.getString("login"));
                } else {
                    return null;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public User singIn(String login, String password) {
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT  login, password FROM users " +
                    "WHERE login = ? and password = ?")) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return new User(resultSet.getString("login"), resultSet.getString("password"));
                } else {
                    return null;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public boolean singUp(User user) {
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (login, password) VALUES (?, ?)")) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
}
