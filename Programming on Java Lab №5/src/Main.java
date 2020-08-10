import Data.*;
import TypeAdaptor.ZonedDateTimeTypeAdaptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;


public class Main {

    public static void main(String[] args) {
        Coordinates coordinates = new Coordinates(60, (long) 30.13295);
        Address address = new Address("Vyazemsky lane 5/7", "197022");
        Address organizationAddress = new Address("Kronverksky avenue 49", "197101");
        Organization organization = new Organization(12000, OrganizationType.COMMERCIAL, organizationAddress);
        ZonedDateTime creationTime = ZonedDateTime.now();
        ZonedDateTime startDate = creationTime.plusDays(7);
        LocalDateTime endDate = LocalDateTime.now().plusYears(5);
        Status status = Status.REGULAR;
        double salary = 250000.0;

        Worker worker = new Worker("Behruz", coordinates, salary, startDate, endDate, status, organization);
        Worker worker1 = new Worker("Behruz1", coordinates, salary, startDate, endDate, status, organization);
//
//        Employee employee = new Employee(5, "ABdu", 5984.24456);
//        Employee employee2 = new Employee(1, "fasfdsaf", 24568.00);
//        //Gson gson = new GsonBuilder().serializeNulls().create();

         Gson gson = ZonedDateTimeTypeAdaptor.getGsonBuilder().create();
         List<Worker> workers = new ArrayList<>();
         workers.add(worker);
         workers.add(worker1);
         String JSON = gson.toJson(workers);

//
//        List<Employee> list = new ArrayList<>();
//        list.add(employee);
//        list.add(employee2);
//        String JSON = gson.toJson(list);
//        System.out.println(JSON);

        try {
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter("worker.com")));
            printWriter.println(JSON);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder str = new StringBuilder();
        String ls;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("worker.com"), StandardCharsets.UTF_8));
            while ((ls = in.readLine()) != null) {
                str.append(ls);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("------------------------------------------------------");
        System.out.println(str);

//        Type collectionType = new TypeToken<ArrayList<Employee>>(){}.getType();
//        ArrayList<Employee> employee1 = gson.fromJson(String.valueOf(str), collectionType);
//        for (Employee e : employee1) {
//            System.out.println(e.toString());
//        }
        Type collectionType = new TypeToken<ArrayList<Worker>>(){}.getType();
        ArrayList<Worker> workers1 = gson.fromJson(String.valueOf(str), collectionType);
        for(Worker w : workers1) {
            System.out.println(w.toString());
        }
    }
}