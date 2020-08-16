package Server;

import Common.Data.Organization;
import Common.Data.Worker;
import Server.Parser.WorkerParser;
import Common.TypeAdaptor.ZonedDateTimeTypeAdaptor;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Behruz Mansurov
 */
public class WorkerManager {
    private List<Worker> workerList;
    private LocalDateTime initDate;
    private File file;
    private static Gson gson = ZonedDateTimeTypeAdaptor.getGsonBuilder().serializeNulls().create();

    public WorkerManager(String file) {
        workerList = new LinkedList<>();
        initDate = LocalDateTime.now();
        importData(new File(file));
    }

    public void importData(File file) {
        try {
            if (!file.isFile()) throw new FileNotFoundException("Ошибка указанный путь не ведет к файлу");
            if (!file.exists()) throw new FileNotFoundException("Файл не найден. Укажите другой путь");
            if (!file.canRead()) throw new SecurityException("Доступ запрещен. Нет доступа для чтения");
            StringBuilder strJson = readFromFile(file);
            fromJsonToWorker(strJson);
        } catch (FileNotFoundException | SecurityException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Непредвиденная ошибка ввода\n " + e);
        } catch (Exception e) {
            System.out.println("Что произошло не так\n " + e);
            e.printStackTrace();
        }
    }

    public StringBuilder readFromFile(File file) throws IOException {
        StringBuilder result = new StringBuilder();
        String nextLine;
        try (Scanner in = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream(file))))) {
            while (in.hasNextLine()) {
                result.append(in.nextLine());
            }
        }
        return result;
    }

    public void fromJsonToWorker(StringBuilder stringBuilder) {
        List<Worker> workers = new LinkedList<>();
//        Gson gson = ZonedDateTimeTypeAdaptor.getGsonBuilder().serializeNulls().create();
        Type collectionType = new TypeToken<List<Worker>>() {
        }.getType();
        try {
            workers = gson.fromJson(String.valueOf(stringBuilder), collectionType);
            if (workers.size() != 0) {
                for (Worker worker : workers) {
                    if (WorkerParser.parse(worker)) workerList.add(worker);
                }
            }
        } catch (JsonSyntaxException e) {
            System.out.println("Не смогли распарсить данные с файла");
        }
        System.out.println("Коллекция успешно загружено. Добавлено " + workerList.size() + " элементов");
    }

    public void help() {
        Map<String, String> help = new HashMap<>();
        System.out.println("Lab 5 made by Behruz Mansurov\n" +
                "\"help : вывести справку по доступным командам\"\n" +
                "\"info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\"\n" +
                "\"add : добавить новый элемент в коллекцию\"\n" +
                "\"update_by_id : обновить значение элемента коллекции, id которого равен заданному\"\n" +
                "\"remove_by_id : удалить элемент из коллекции по его id\"\n" +
                "\"clear : очистить коллекцию\"\n" +
                "\"save : сохранить коллекцию в файл\"\n" +
                "\"execute_script file_name : считать и исполнить скрипт из указанного файла. " +
                "В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\"\n" +
                "\"exit : завершить программу (без сохранения в файл)\"\n" +
                "\"remove_first : удалить первый элемент из коллекции\"\n" +
                "\"remove_last : удалить последний элемент из коллекции\"\n" +
                "\"shuffle : перемешать элементы коллекции в случайном порядке\"\n" +
                "\"remove_any_by_start_date : удалить из коллекции один элемент, значение поля startDate которого эквивалентно заданному\"\n" +
                "\"max_by_id : вывести любой объект из коллекции, значение поля id которого является максимальным\"\n" +
                "\"count_less_than_organization : вывести количество элементов, значение поля organization которых меньше заданного\""
        );
    }

    public void info() {
        System.out.println("Информация о коллекции:\n" +
                "   Тип коллекции: " + workerList.getClass().getName() + "\n" +
                "   Дата инициализации: " + initDate.toString() + "\n" +
                "   Количество элеменотов: " + workerList.size() + "\n"
        );
    }

    public void show() {
        System.out.println(workerList.toString());
    }

    public void add(Worker worker) {
        workerList.add(worker);
        System.out.println("Объект успешно добавлен в коллекцию");
    }

    public void updateById(long id, long updateId) {
        for (Worker w : workerList) {
            if (w.getId() == id) {
                w.setId(updateId);
            }
        }
        System.out.println("Если был объект с таким id, id объекта было обновлено");
    }

    public void removeById(long id) {
        int l = workerList.size();
        workerList.removeIf(w -> w.getId() == id);
        if(l == workerList.size()) {
            System.out.println("Не существует оюъект с таким id");
        } else {
            System.out.println("Объект успешно удален");
        }
    }

    public void clear() {
        workerList.clear();
        System.out.println("Коллекция удалена");
    }

    public void save() {
        String stringBuilder = gson.toJson(workerList);
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("Common.Common.Data.json"))) {
            byte[] buffer = stringBuilder.getBytes();
            bos.write(buffer, 0, buffer.length);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Коллекция сохранена в файл");
    }

    public void removeFirst() {
        if(workerList.size() != 0) {
            workerList.remove(1);
            System.out.println("Удален первый элемент");
        } else {
            System.out.println("Коллекция пуста");
        }

    }

    public void removeLast() {
        if(workerList.size() != 0) {
            ListIterator<Worker> listIterator = workerList.listIterator();
            listIterator.previous();
            listIterator.remove();
            System.out.println("Удален последний элемент");
        } else {
            System.out.println("Коллекция пуста");
        }
    }

    public void shuffle() {
        if(workerList.size() != 0) {
            Collections.shuffle(workerList);
        } else System.out.println("Коллекция пуста");
    }

    public void removeAnyByStartDate(ZonedDateTime date) {
        int l = workerList.size();
        workerList.removeIf(w -> w.getStartDate().equals(date));
        if(l == workerList.size()) {
            System.out.println("Объект с таким startdate не существует");
        } else System.out.println("Объект был удален");
    }
//    public void removeAnyByStartDate(String date) {
//        ZoneId timeZone = ZoneId.systemDefault();
//        ZonedDateTime time = null;
//        try {
//            time = (LocalDateTime.parse(date,
//                    DateTimeFormatter.ISO_DATE_TIME).atZone(timeZone));
//        } catch (Exception e) {
//            System.out.println("Дата введена неправельно");
//        }
//
//        ZonedDateTime finalTime = time;
//        workerList.removeIf(w -> w.getCreationDate().equals(finalTime) );
//    }

    public void maxById() {
        System.out.println("Max : " + Collections.max(workerList));
    }

    public void countLessThanOrganization(Organization organization) {
        int cnt = 0;
        for (Worker w : workerList) {
            if (w.getOrganization().getEmployeesCount() < organization.getEmployeesCount()) {
                cnt++;
            }
        }
        System.out.println("Количество элементов, значение поля organization которых меньше заданного " + cnt);
        //        Iterator<Worker> iterator = workerList.iterator();
//        while(iterator.hasNext()) {
//            if(iterator.next().getOrganization().getEmployeesCount() < organization.getEmployeesCount()) {
//                System.out.println();
//            }
//        }
    }
}
