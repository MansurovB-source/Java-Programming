package Manager;

import Data.Worker;
import Parser.UserInputParser;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * Author: Behruz Mansurov
 */
public class CommandManager {
    private static boolean exit = false;
    String command = "";
    String[] userCommand = new String[2];
    UserInputParser userInputParser;
    WorkerManager workerManager;

    public CommandManager(String file) {
        userInputParser = new UserInputParser();
        workerManager = new WorkerManager(file);
    }

    public void parser() {
        while (!exit) {
            System.out.println("Введите команду:");
            Scanner commandReader = new Scanner(System.in);
            command = commandReader.nextLine();
            userCommand = command.trim().split(" ", 2);
            if (userCommand[0].equals("execute_script")) {
                userCommand[0] = String.valueOf(executeScript(userCommand[1]));
            }
            try {
                switch (userCommand[0]) {
                    case "help":
                        workerManager.help();
                        break;
                    case "info":
                        workerManager.info();
                        break;
                    case "show":
                        workerManager.show();
                        break;
                    case "add":
                        Worker worker = new Worker(userInputParser.inputName(), userInputParser.inputCoordinates(), userInputParser.inputSalary(), userInputParser.inputStartDate(), userInputParser.inputEndDate(), userInputParser.inputStatus(), userInputParser.inputOrganization());
                        workerManager.add(worker);
                        break;
                    case "update_by_id":
                        workerManager.updateById(userInputParser.inputId(), userInputParser.inputId());
                        break;
                    case "remove_by_id":
                        workerManager.removeById(userInputParser.inputId());
                        break;
                    case "clear":
                        workerManager.clear();
                        break;
                    case "save":
                        workerManager.save();
                        break;
                    case "exit":
                        exit = true;
                        break;
                    case "remove_first":
                        workerManager.removeFirst();
                        break;
                    case "remove_last":
                        workerManager.removeLast();
                        break;
                    case "shuffle":
                        workerManager.shuffle();
                        break;
                    case "remove_any_by_start_date":
                        workerManager.removeAnyByStartDate(userInputParser.inputStartDate());
                        break;
                    case "max_by_id":
                        workerManager.maxById();
                        break;
                    case "count_less_than_organization":
                        workerManager.countLessThanOrganization(userInputParser.inputOrganization());
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
