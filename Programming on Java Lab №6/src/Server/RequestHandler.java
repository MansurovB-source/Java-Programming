package Server;

import Common.Data.Organization;
import Common.Data.Worker;
import Common.Request;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.ZonedDateTime;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class RequestHandler {
    private final Socket socket;
    private final WorkerManager workerManager;
    private Request request;
    String command;
    Worker worker;
    File file;
    int id;
    ZonedDateTime startdate;
    Organization organization;

    public RequestHandler(Socket socket, WorkerManager workerManager) {
        this.socket = socket;
        this.workerManager = workerManager;
    }

    public void handler() {
        try(ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
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
            switch (command) {
                case "help":
                    oos.writeUTF(workerManager.help());
                    break;
                case "info":
                    oos.writeUTF(workerManager.info());
                    break;
                case "show":
                    oos.writeUTF(workerManager.show());
                    break;
                case "add":
                    oos.writeUTF(workerManager.add(worker));
                    break;
                case "update_by_id":
                    oos.writeUTF(workerManager.updateById(id, id));
                    break;
                case "remove_by_id":
                    oos.writeUTF(workerManager.removeById(id));
                    break;
                case "clear":
                    oos.writeUTF(workerManager.clear());
                    break;
                case "save_server":
                    workerManager.save();
                    break;
                case "remove_first":
                    oos.writeUTF(workerManager.removeFirst());
                    break;
                case "remove_last":
                    oos.writeUTF(workerManager.removeLast());
                    break;
                case "shuffle":
                    oos.writeUTF(workerManager.shuffle());
                    break;
                case "remove_any_by_start_date":
                    oos.writeUTF(workerManager.removeAnyByStartDate(startdate));
                    break;
                case "max_by_id":
                    oos.writeUTF(workerManager.maxById());
                    break;
                case "count_less_than_organization":
                    oos.writeUTF(workerManager.countLessThanOrganization(organization));
                    break;
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
