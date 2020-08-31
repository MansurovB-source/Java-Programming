package Client;

import Common.Data.Worker;
import Common.Parser.UserInputParser;
import Common.Request;

import java.io.*;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class RequestSender {
    private boolean exit = false;
    private final Connection sender;
    private final Authentication authentication;

    public RequestSender(int PORT) {
        sender = new Connection(PORT);
        authentication = new Authentication(sender);
        authentication.authentication();
    }

    public void parser() {
        String command;
        String[] userCommand;
        Scanner scanner = new Scanner(System.in);
        UserInputParser userInputParser = new UserInputParser();
        while (!exit) {
            System.out.println("Введите команду:");
            command = scanner.nextLine();
            userCommand = command.trim().split(" ", 2);
            if (userCommand[0].equals("execute_script")) {
                userCommand[0] = String.valueOf(executeScript(userCommand[1]));
            }
            try {
                switch (userCommand[0]) {
                    case "help":
                    case "info":
                    case "show":
                    case "clear":
                    case "remove_first":
                    case "remove_last":
                    case "shuffle":
                    case "max_by_id":
                        sender.sendRequest(new Request(userCommand[0], authentication.getCurrentUser()));
                        break;
                    case "add":
                        long id = userInputParser.inputId();
                        Worker worker = new Worker(userInputParser.inputName(), userInputParser.inputCoordinates(),
                                userInputParser.inputSalary(), userInputParser.inputStartDate(),
                                userInputParser.inputEndDate(), userInputParser.inputStatus(),
                                userInputParser.inputOrganization());
                        worker.setId(id);
                        sender.sendRequest(new Request(userCommand[0], worker, authentication.getCurrentUser()));
                        break;
                    case "update_by_id":
                        sender.sendRequest(new Request(userCommand[0], userInputParser.inputId(),
                                new Worker(userInputParser.inputName(), userInputParser.inputCoordinates(), userInputParser.inputSalary(), userInputParser.inputStartDate(), userInputParser.inputEndDate(), userInputParser.inputStatus(), userInputParser.inputOrganization()), authentication.getCurrentUser()));
                        break;
                    case "remove_by_id":
                        sender.sendRequest(new Request(userCommand[0], userInputParser.inputId(), authentication.getCurrentUser()));
                        break;
                    case "exit":
//                        sender.sendRequest(new Request(userCommand[0]));
                        exit = true;
                        break;
                    case "remove_any_by_start_date":
                        sender.sendRequest(new Request(userCommand[0], userInputParser.inputStartDate(), authentication.getCurrentUser()));
                        break;
                    case "count_less_than_organization":
                        sender.sendRequest(new Request(userCommand[0], userInputParser.inputOrganization(), authentication.getCurrentUser()));
                        break;
                    default:
                        System.out.println("Введите правельную команду");
                        break;
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                System.out.println("Отсутствует аргумент");
            }
        }
    }

    public StringBuilder executeScript(String file) {
        StringBuilder stringBuilder = new StringBuilder();
        try (Scanner in = new Scanner(new BufferedInputStream(new FileInputStream(file)))) {
            while (in.hasNextLine()) {
                stringBuilder.append(in.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        }
        return stringBuilder;
    }
}
