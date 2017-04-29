import java.io.*;
import org.apache.thrift.*;
import org.apache.thrift.protocol.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.TException;

import java.util.*;


public class StudentThriftClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 9527;
    private static final int TIMEOUT = 5000;
    private static TTransport transport;
    private static StudentService.Client client;

    static {
        //block I/O
        transport = new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT);

        TProtocol protocol = new TBinaryProtocol(transport);
        client = new StudentService.Client(protocol);
    }

    private static void mockConstructStudent() throws TException, StudentException {
        StudentInfo student1 = new StudentInfo();
        student1.setName("qinyi");
        student1.setNumber(new HashMap<String, Long>() {{
            put("qinyi", 21209184L);
          }});
        student1.setAge(25);
        student1.setSex(Sex.Boy);
        student1.setHobby(new ArrayList<String>(Arrays.asList("ping pong", "swimming", "tai qiu")));

        StudentInfo student2 = new StudentInfo();
        student2.setName("brucezhang");
        student2.setNumber(new HashMap<String, Long>() {{
            put("brucezhang", 8205050122L);
          }});
        student2.setAge(18);
        student2.setSex(Sex.Boy);

        System.out.println("the action of add #1 student: " + client.addStudentToSchool(student1));
        System.out.println("the action of add #2 student: " + client.addStudentToSchool(student2));

        List<StudentInfo> students = client.getStudentInfoByName("girl");
        for (StudentInfo student : students) {
            System.out.println("studnet sex=" + student.sex +" age= " + student.age);

            for(String str:student.hobby)
              System.out.println("studnet hobby=" + str);

          }
        //client.addStudentToSchool(null); -> cause exception!
    }

    private static void mockGetService() throws TException, StudentException {
        mockConstructStudent();
        client.printStudentsInfo(client.getStudentInfoByName("qinyi"));
        client.printStudentsInfo(client.getStudentInfoByName("brucezhang"));
    }

    public static void main(String[] args) throws TException{
        transport.open();
        
        try {
            mockGetService();
        } catch (StudentException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        transport.close();
    }
}
