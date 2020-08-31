package Client;

import Common.Request;
import Common.User;

import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class Authentication {
    private Scanner in = new Scanner(System.in);
    private Connection connection;
    private User currentUser = null;
    private String login;
    private String password;

    public Authentication(Connection connection) {
        this.connection = connection;
    }

    public void authentication() {
        System.out.println("Введите логин (mail): ");
        while (true) {
            login = in.nextLine();
            if (login.matches("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
                connection.sendRequest(new Request("is_registered", login));
                currentUser = connection.getUser();
                if (currentUser != null) {
                    System.out.println("Введите пароль:");
                } else {
                    System.out.println("Пользователь с таким логином не найден. Хотите зарегистрироваться?");
                    String command;
                    do {
                        System.out.println("Введите Y/N");
                        command = in.nextLine().toLowerCase();
                    } while (!(command.equals("y") || command.equals("n")));
                    if (command.equals("y")) {
                        System.out.println("Хотите автоматически генерированный пароль?");
                        do {
                            System.out.println("Введите Y/N");
                            command = in.nextLine().toLowerCase();
                        } while (!(command.equals("y") || command.equals("n")));
                        if (command.equals("y")) {
                            System.out.println("Генерируем пароль...");
                            password = null;
                        } else {
                            System.out.println("Введите пароль: ");
                            password = in.nextLine();
                        }
                        connection.sendRequest(new Request("sign_up", login, password));
                        if (command.equals("n")) {
                            currentUser = connection.getUser();
                            if (currentUser.getPassword() != null) {
                                System.out.println("Введите пароль, который был отправлен на ваш e-mail:");
                                break;
                            } else {
                                System.out.println("Не удалось отправить письмо на " + login + "\nВведите другой e-mail:");
                            }
                        }
                    }
                }
            } else {
                System.out.println("Вы ввели e-mail в неверном формате. Попробуйте снова:");
            }
        }
        sing_in();
    }

    private void sing_in() {
        while (true) {
            password = in.nextLine();
            connection.sendRequest(new Request("sing_in", login, password));
            currentUser = connection.getUser();
            if (currentUser != null) {
                System.out.println("Добро пожаловать, " + currentUser.getLogin().split("@")[0]);
                break;
            } else {
                System.out.println("Вы ввели неверный пароль. Попробуйте снова:");
            }
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
