package Server;

import Common.Data.Organization;
import Common.Data.Worker;
import Common.Request;
import Common.User;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.time.ZonedDateTime;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class RequestHandler implements Runnable{
    private final SocketChannel socket;
    private final WorkerManager workerManager;
    private final UserManager userManager;
    private Request request;
    private String command;
    private Worker worker;
    private File file;
    private long id;
    private ZonedDateTime startdate;
    private Organization organization;
    private String login;
    private String password;
    private User user;

    public RequestHandler(SocketChannel socket, WorkerManager workerManager, UserManager userManager) {
        this.socket = socket;
        this.workerManager = workerManager;
        this.userManager = userManager;
    }

    public void run() {
        try(ObjectInputStream ois = new ObjectInputStream(socket.socket().getInputStream());
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bais)) {
            try {
                request = (Request) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            command = request.getRequest();
            worker = request.getWorker();
            file = request.getFile();
            id = request.getId();
            startdate = request.getLocalDateTime();
            organization = request.getOrganization();
            login = request.getLogin();
            password = request.getPassword();
            user = request.getUser();

            switch (command) {
                case "help":
                    oos.writeObject(workerManager.help());
                    break;
                case "info":
                    oos.writeObject(workerManager.info());
                    break;
                case "show":
                    oos.writeObject(workerManager.show());
                    break;
                case "add":
                    oos.writeObject(workerManager.add(worker));
                    break;
                case "update_by_id":
                    oos.writeObject(workerManager.updateById(id, worker));
                    break;
                case "remove_by_id":
                    oos.writeObject(workerManager.removeById(id));
                    break;
                case "clear":
                    oos.writeObject(workerManager.clear());
                    break;
                case "save_server":
                case "exit":
                    workerManager.save();
                    oos.writeObject("Коллекция сохранена в файл на сервере");
                    break;
                case "remove_first":
                    oos.writeObject(workerManager.removeFirst());
                    break;
                case "remove_last":
                    oos.writeObject(workerManager.removeLast());
                    break;
                case "shuffle":
                    oos.writeObject(workerManager.shuffle());
                    break;
                case "remove_any_by_start_date":
                    oos.writeObject(workerManager.removeAnyByStartDate(startdate));
                    break;
                case "max_by_id":
                    oos.writeObject(workerManager.maxById());
                    break;
                case "count_less_than_organization":
                    oos.writeObject(workerManager.countLessThanOrganization(organization));
                    break;
                case "is_registered":
                    oos.writeObject(userManager.isRegistered(login));
                case "sign_up":
                    oos.writeObject(userManager.singUp(login, password));
                case "sign_in":
                    oos.writeObject(userManager.signIn(login,password));
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(bais.size());
            byteBuffer.put(bais.toByteArray());
            byteBuffer.flip();
            while(byteBuffer.hasRemaining()) {
                socket.write(byteBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Не смогли закрыть сокет");
                e.printStackTrace();
            }
        }
    }


}
