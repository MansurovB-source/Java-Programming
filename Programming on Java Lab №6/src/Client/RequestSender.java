package Client;

import Client.Exception.ConnectionException;
import Common.Data.Worker;
import Common.Parser.UserInputParser;
import Common.Request;
import Server.Server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class RequestSender {
    private boolean exit = false;
    private final Client sender;
    public RequestSender(int PORT) {
        sender = new Client(PORT);
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
                        sender.sendRequest(new Request(userCommand[0]));
                        break;
                    case "add":
                        Worker worker = new Worker(userInputParser.inputName(), userInputParser.inputCoordinates(), userInputParser.inputSalary(), userInputParser.inputStartDate(), userInputParser.inputEndDate(), userInputParser.inputStatus(), userInputParser.inputOrganization());
                        sender.sendRequest(new Request(userCommand[0], worker));
                        break;
//                    case "update_by_id":
//                        workerManager.updateById(userInputParser.inputId(), userInputParser.inputId());
//                        break;
                    case "remove_by_id":
                        sender.sendRequest(new Request(userCommand[0], userInputParser.inputId()));
                        break;
                    case "exit":
                        exit = true;
                        break;
                    case "remove_any_by_start_date":
                        sender.sendRequest(new Request(userCommand[0],userInputParser.inputStartDate()));
                        break;
                    case "count_less_than_organization":
                        sender.sendRequest(new Request(userCommand[0],userInputParser.inputOrganization()));
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
}
