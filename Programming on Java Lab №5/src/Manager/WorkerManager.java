package Manager;

import Data.*;
import Parser.WorkerParser;
import TypeAdaptor.ZonedDateTimeTypeAdaptor;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Behruz Mansurov
 */
public class WorkerManager {
    public static void main(String[] args) {
        WorkerManager workerManager = new WorkerManager();
        //workerManager.help();
        Coordinates coordinates = new Coordinates(999, (long) 995);
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        LocalDateTime localDateTime = LocalDateTime.now().minusYears(5);
        Address address = new Address("fuck", "bitch");
        Organization organization = new Organization(2555, OrganizationType.COMMERCIAL, address);


//        Worker worker = new Worker("Behruz", coordinates, 250000.0, zonedDateTime, localDateTime, Status.REGULAR, organization);
//        Worker worker1 = new Worker("Behruz1", coordinates, 250000.0, zonedDateTime, localDateTime, Status.REGULAR, organization);
//        Worker worker2 = new Worker("Behruz2", coordinates, 250000.0, zonedDateTime, localDateTime, Status.REGULAR, organization);
//        workerManager.workerList.add(worker);
//        workerManager.workerList.add(worker1);
//        workerManager.workerList.add(worker2);
//        workerManager.save();
        workerManager.importData(new File("Data.json"));
        System.out.println(workerManager.workerList.toString());

    }

    private List<Worker> workerList = new LinkedList<>();
    private LocalDateTime initDate;
    private File file;
    Gson gson = ZonedDateTimeTypeAdaptor.getGsonBuilder().serializeNulls().create();

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
                "\"add {element} : добавить новый элемент в коллекцию\"\n" +
                "\"update id {element} : обновить значение элемента коллекции, id которого равен заданному\"\n" +
                "\"remove_by_id id : удалить элемент из коллекции по его id\"\n" +
                "\"clear : очистить коллекцию\"\n" +
                "\"save : сохранить коллекцию в файл\"\n" +
                "\"execute_script file_name : считать и исполнить скрипт из указанного файла. " +
                "В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\"\n" +
                "\"exit : завершить программу (без сохранения в файл)\"\n" +
                "\"remove_first : удалить первый элемент из коллекции\"\n" +
                "\"remove_last : удалить последний элемент из коллекции\"\n" +
                "\"shuffle : перемешать элементы коллекции в случайном порядке\"\n" +
                "\"remove_any_by_start_date startDate : удалить из коллекции один элемент, значение поля startDate которого эквивалентно заданному\"\n" +
                "\"max_by_id : вывести любой объект из коллекции, значение поля id которого является максимальным\"\n" +
                "\"count_less_than_organization organization : вывести количество элементов, значение поля organization которых меньше заданного\""
        );
    }

    public void info() {
        System.out.println("Информация о коллекции:\n" +
                "   Тип коллекции: " + workerList.getClass().getName() + "\n" +
                "   Дата инициализации: " + initDate.toString() + "\n" +
                "   Количество элеменотов: " + workerList.size() + "\n"
        );
    }

    public void add(Worker worker) {
        workerList.add(worker);
    }

    public void updateById(int id, int updateId) {
        for (Worker w : workerList) {
            if (w.getId() == id) {
                w.setId(updateId);
            }
        }
    }

    public void removeById(int id) {
        workerList.removeIf(w -> w.getId() == id);
    }

    public void clear() {
        workerList.clear();
    }

    public void save() {
        String stringBuilder = gson.toJson(workerList);
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("Data.json"))) {
            byte[] buffer = stringBuilder.getBytes();
            bos.write(buffer, 0, buffer.length);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void executeScript(File file) {
    }

    public void removeFirst() {
        workerList.remove(1);
    }

    public void  removeLast() {
        ListIterator<Worker> listIterator = workerList.listIterator();
        listIterator.previous();
        listIterator.remove();
    }

    public void shuffle() {
        Collections.shuffle(workerList);
    }

    public void removeAnyVyStartDate(ZonedDateTime date) {
        workerList.removeIf(w -> w.getCreationDate().equals(date));
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
        System.out.println(Collections.max(workerList));
    }

    public void countLessThanOrganization(Organization organization) {
        for(Worker w : workerList) {
            if(w.getOrganization().getEmployeesCount() < organization.getEmployeesCount()) {
                System.out.println(w);
            }
        }
        //        Iterator<Worker> iterator = workerList.iterator();
//        while(iterator.hasNext()) {
//            if(iterator.next().getOrganization().getEmployeesCount() < organization.getEmployeesCount()) {
//                System.out.println();
//            }
//        }
    }
}
