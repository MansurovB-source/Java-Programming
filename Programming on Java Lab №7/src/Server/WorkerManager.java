package Server;

import Common.Data.Organization;
import Common.Data.Worker;
import Common.User;
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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class WorkerManager {
    private CopyOnWriteArrayList<Worker> workerList = new CopyOnWriteArrayList<>();
    private final LocalDateTime initDate;
    private static final Gson gson = ZonedDateTimeTypeAdaptor.getGsonBuilder().serializeNulls().create();
    DataBaseManager dataBaseManager;
    private Lock lock = new ReentrantLock();

    public WorkerManager(String file) {
        initDate = LocalDateTime.now();
        importData(new File(file));
    }

    public WorkerManager(DataBaseManager dataBaseManager) {
        this.dataBaseManager = dataBaseManager;
        initDate = LocalDateTime.now();
        workerList = dataBaseManager.getCollectionsFromDB();
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
        try (Scanner in = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream(file))))) {
            while (in.hasNextLine()) {
                result.append(in.nextLine());
            }
        }
        return result;
    }

    public void fromJsonToWorker(StringBuilder stringBuilder) {
        List<Worker> workers;
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

    public String help(User user) {
        if (dataBaseManager.checkLoginAndPassword(user)) {
            return "Lab 5 made by Behruz Mansurov\n" +
                    "\"help : вывести справку по доступным командам\"\n" +
                    "\"info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\"\n" +
                    "\"add : добавить новый элемент в коллекцию\"\n" +
                    "\"update_by_id : обновить значение элемента коллекции, id которого равен заданному\"\n" +
                    "\"remove_by_id : удалить элемент из коллекции по его id\"\n" +
                    "\"clear : очистить коллекцию\"\n" +
//                "\"save : сохранить коллекцию в файл\"\n" +
                    "\"execute_script file_name : считать и исполнить скрипт из указанного файла. " +
                    "В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\"\n" +
                    "\"exit : завершить программу (без сохранения в файл)\"\n" +
                    "\"remove_first : удалить первый элемент из коллекции\"\n" +
                    "\"remove_last : удалить последний элемент из коллекции\"\n" +
                    "\"shuffle : перемешать элементы коллекции в случайном порядке\"\n" +
                    "\"remove_any_by_start_date : удалить из коллекции один элемент, значение поля startDate которого эквивалентно заданному\"\n" +
                    "\"max_by_id : вывести любой объект из коллекции, значение поля id которого является максимальным\"\n" +
                    "\"count_less_than_organization : вывести количество элементов, значение поля organization которых меньше заданного\"";
        } else {
            return "У вас нет доступа";
        }
    }

    public String info(User user) {
        if (dataBaseManager.checkLoginAndPassword(user)) {
            return "Информация о коллекции:\n" +
                    "   Тип коллекции: " + workerList.getClass().getName() + "\n" +
                    "   Дата инициализации: " + initDate.toString() + "\n" +
                    "   Количество элеменотов: " + workerList.size() + "\n";
        } else {
            return "У вас нет доступа";
        }
    }

    public String show(User user) {
        if(dataBaseManager.checkLoginAndPassword(user)) {
            return workerList.toString();
        } else {
            return "У вас нет доступа";
        }
    }

    public String  add(Worker worker, User user) {
        lock.lock();
        try {
            if (dataBaseManager.addWorker(worker, user)) {
                workerList.add(worker);
                return "Объект успешно добавлен в коллекцию";
            } else {
                return "Объект с таким ID: " + worker.getId() + " уже существует";
            }
        } finally {
            lock.unlock();
        }
    }

    public String updateById(long id, Worker worker, User user) {
        lock.lock();
        try {
            int status;
            for (Worker w : workerList) {
                if (w.getId() == id) {
                    if ((status = dataBaseManager.removeWorker(w, user)) > 0) {
                        workerList.remove(w);
                        if (dataBaseManager.addWorker(worker, user)) {
                            workerList.add(worker);
                        }
                    } else if (status == 0) {
                        return "База данных не содержит объект с ID: " + id;
                    } else {
                        return "Объект с ID " + id + " вам не принадлежит";
                    }
                } else {
                    return "База данных не содержит объект с ID: " + id;
                }
            }
            return "База данных не содержит объект с ID: " + id;
        } finally {
            lock.unlock();
        }
    }

    public String removeById(long id, User user) {
        lock.lock();
        try {
            int status;
            for (Worker w : workerList) {
                if (w.getId() == id) {
                    status = dataBaseManager.removeWorker(w, user);
                    if (status > 0) {
                        workerList.remove(w);
                        return "Объект с ID " + w.getId() + " удалён из коллекции";
                    } else if (status == 0) {
                        return "База данных не содержит объект с ID: " + id;
                    } else {
                        return "Объект с ID " + id + " вам не принадлежит";
                    }
                }
            }
            return "База данных не содержит объект с ID: " + id;
        } finally {
            lock.unlock();
        }
    }

    public String clear() {
        workerList.clear();
        return "Коллекция удалена";
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
        System.out.println("Коллекция сохранена в файл");
    }

    public String removeFirst(User user) {
        lock.lock();
        try {
            int status = dataBaseManager.removeWorker(workerList.get(0), user);
            if (status > 0) {
                workerList.remove(0);
                return "Объект удалён из коллекции и из базы данных";
            } else if (status == 0) {
                return "База данных не содержит такой объект ";
            } else {
                return "Объект вам не принадлежит";
            }
        } finally {
            lock.unlock();
        }
    }

    public String removeLast(User user) {
        lock.lock();
        try {
            int status = dataBaseManager.removeWorker(workerList.get(workerList.size() - 1), user);
            if (status > 0) {
                workerList.remove(workerList.size() - 1);
                return "Объект удалён из коллекции и из базы данных";
            } else if (status == 0) {
                return "База данных не содержит такой объект ";
            } else {
                return "Объект вам не принадлежит";
            }
        } finally {
            lock.unlock();
        }
    }

    public String shuffle(User user) {
        lock.lock();
        try {
            if (dataBaseManager.checkLoginAndPassword(user)) {
                if (workerList.size() != 0) {
                    Collections.shuffle(workerList);
                    return "Элементы коллекции были перемешанны в случайном порядке";
                } else return "Коллекция пуста";
            } else {
                return "У вас нет доступа";
            }
        } finally {
            lock.unlock();
        }
    }

    public String removeAnyByStartDate(ZonedDateTime date, User user) {
        lock.lock();
        try {
            for (Worker w : workerList) {
                if (w.getStartDate().compareTo(date) == 0) {
                    int status = dataBaseManager.removeWorker(w, user);
                    if (status > 0) {
                        workerList.remove(workerList.size() - 1);
                        workerList.remove(w);
                        return "Объект удалён из коллекции и из базы данных";
                    } else if (status == 0) {
                        return "База данных не содержит такой объект ";
                    } else {
                        return "Объект вам не принадлежит";
                    }
                }
            }
            return "База данных не содержит такой объект ";
        }finally {
            lock.unlock();
        }
    }

    public String maxById(User user) {
        if(dataBaseManager.checkLoginAndPassword(user)) {
            return "Max : " + Collections.max(workerList);
        } else {
            return "У вас нет доступа";
        }
    }

    public String countLessThanOrganization(Organization organization, User user) {
        lock.lock();
        try {
            if (dataBaseManager.checkLoginAndPassword(user)) {
                int cnt = (int) workerList.stream().filter(w -> w.getOrganization().getEmployeesCount() < organization.getEmployeesCount()).count();
                return "Количество элементов, значение поля organization которых меньше заданного: " + cnt;
            } else {
                return "У вас нет доступа";
            }
        } finally {
            lock.unlock();
        }
    }
}
