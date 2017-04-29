- Thrift: 基于C/S架构的RPC通信框架，具有高效序列化机制
	- 定义接口描述文件 `.thrift`
	- 使用*thrift*工具利用IDL生成目标代码
	- server端
	- client端

`studentModel.thrift`: 定义学生信息和学校信息的数据结构

```
//定义学生信息和学校信息的数据结构
enum Sex {
    Boy = 1;
    Girl = 2;
}

struct StudentInfo {
    1: required string name;
    2: required Sex sex;
    3: required i32 age;
    4: optional list<string> hobby;
    5: required map<string, i64> number;
}

struct School {
    1: required string name;
    2: required list<StudentInfo> students;
    3: optional string description;
}
```

`studentException.thrift`: 定义异常

```
//定义异常
exception StudentException {
    1: required i64 errorCode;
    2: required string description;
    3: optional string causeInfo;
}
```

`studentService.thrift`: 定义服务

```
//定义服务
include "studentModel.thrift"
include "studentException.thrift"

// 服务定义在语义上相当于面向对象编程中一接口
service StudentService {
    // add student to school
    bool addStudentToSchool(1: studentModel.StudentInfo student) throws (1: studentException.StudentException ex);

    // get student info by name
    list<studentModel.StudentInfo> getStudentInfoByName(1: string name) throws (1: studentException.StudentException ex);

    // print single student info
    void printStudentInfo(1: studentModel.StudentInfo student) throws (1: studentException.StudentException ex);

    // print list students info
    void printStudentsInfo(1: list<studentModel.StudentInfo> students) throws (1: studentException.StudentException ex);
}
```

thrift编译 -> bat文件

```bat
thrift_home="{your_thrift_home}/"
thrift_file="{your_thrift_idl_files}"

${thrift_home}/thrift --gen java ${thrift_file}/studentException.thrift
${thrift_home}/thrift --gen java ${thrift_file}/studentModel.thrift
${thrift_home}/thrift --gen java ${thrift_file}/studentService.thrift
```

- `StudentServiceImpl.java`: 实现server服务业务逻辑

```java
import java.io.*;
import org.apache.thrift.*;
import org.apache.thrift.protocol.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.TException;
import java.util.List;
import java.util.*;

public class StudentServiceImpl implements StudentService.Iface {
    @Override
    public boolean addStudentToSchool(StudentInfo student) throws StudentException, TException {
        if (null == student) {
            throw new StudentException().setErrorCode(-1)
                    .setDescription("add Student To School (StudentInfo student) error")
                    .setCauseInfo("student is null");
        }

        List<StudentInfo> students = SchoolMock.getInstance().getStudents();
        students.add(student);
        SchoolMock.getInstance().setStudents(students);

        return true;
    }

    @Override
    public List<StudentInfo> getStudentInfoByName(String name) throws StudentException, TException {
        if (null == name) {
            throw new StudentException().setErrorCode(-1)
                    .setDescription("get Student InfoByName (String name) error")
                    .setCauseInfo("name is null");
        }

        List<StudentInfo> students = SchoolMock.getInstance().getStudents();
        List<StudentInfo> results = new ArrayList<StudentInfo>();
        for (StudentInfo student : students) {
            if (student.getName().equals(name)) {
                results.add(student);
            }
        }

        StudentInfo student1 = new StudentInfo();
        student1.setName("girl");
        student1.setNumber(new HashMap<String, Long>() {{
            put("john", 21209184L);
        }});
        student1.setAge(15);
        student1.setSex(Sex.Girl);
        student1.setHobby(new ArrayList<String>(Arrays.asList("ping pong", "swimming", "tai qiu")));

        StudentInfo student2 = new StudentInfo();
        student2.setName("girl");
        student2.setHobby(new ArrayList<String>(Arrays.asList("tennis", "swimming", "volleyball")));
        student2.setNumber(new HashMap<String, Long>() {{
            put("tom", 8205050122L);
        }});
        student2.setAge(18);
        student2.setSex(Sex.Girl);

        results.add(student1);
        results.add(student2);

        return results;
    }

    @Override
    public void printStudentInfo(StudentInfo student) throws StudentException, TException {
        if (null == student) {
            throw new StudentException().setErrorCode(-1)
                    .setDescription("print Student Info (StudentInfo student) error")
                    .setCauseInfo("student is null");
        }

        StringBuilder builder = new StringBuilder();
        builder.append("name : ").append(student.getName()).append("\n");

        if (student.getSex().getValue() == 1) {
            builder.append("sex : boy").append("\n");
        } else {
            builder.append("sex : girl").append("\n");
        }

        builder.append("age : ").append(student.getAge()).append("\n");
        if (student.isSetHobby()) {
            for (String hobby : student.getHobby()) {
                builder.append("hobby : ").append(hobby).append("\n");
            }
        }

        builder.append("id : ").append(student.getNumber().get(student.getName())).append("\n");

        System.out.println(builder.toString());
    }

    @Override
    public void printStudentsInfo(List<StudentInfo> students) throws StudentException, TException {
        if (null == students) {
            throw new StudentException().setErrorCode(-1)
                    .setDescription("print Students Info (List<StudentInfo> students) error")
                    .setCauseInfo("students is null");
        }

        for (StudentInfo student : students) {
            printStudentInfo(student);
        }
    }
}
```
- java `StringBuilder`

`SchoolMock.java`: 模拟业务逻辑 -> Mock对象

```java
import java.io.*;
import java.util.*;

public class SchoolMock {
    private static School school;

    private SchoolMock() {    }

    public static synchronized School getInstance() {
        if (null == school) {
            school = new School();

            school.setName("school");
            school.setDescription("this is just a mock school");
            school.setStudents(new ArrayList<StudentInfo>());
        }

        return school;
    }
}
```

启动服务

```java
import java.io.*;
import org.apache.thrift.*;
import org.apache.thrift.server.*;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;

import org.apache.thrift.protocol.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.TException;

import java.util.List;
import java.util.*;

public class StudentThriftServer {
    public static final int SERVER_PORT = 9527;

    public static void main(String[] arg) throws TException{
        TServerSocket serverTransport = new TServerSocket(SERVER_PORT);
        TProcessor tProcessor = new StudentService.Processor(new StudentServiceImpl());

        Factory protFactory = new TBinaryProtocol.Factory(true, true);

        TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
			  args.processor(tProcessor);
			  args.protocolFactory(protFactory);
        //block I/O
        //TServer server = new TSimpleServer(tProcessor, serverTransport); error!
        TServer server = new TThreadPoolServer(args);

        System.out.println("Start server on port 9527...");
        server.serve();
    }
}
```


`StudentThriftClient.java`: 客户端

```java
import java.io.*;
import org.apache.thrift.*;
import org.apache.thrift.protocol.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.TException;

import java.util.*;
//import $ImportPackage$;


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
        student1.setName("tom");
        student1.setNumber(new HashMap<String, Long>() {{
            put("john", 21209184L);
        	}});
        student1.setAge(25);
        student1.setSex(Sex.Boy);
        student1.setHobby(new ArrayList<String>(Arrays.asList("ping pong", "swimming", "tai qiu")));

        StudentInfo student2 = new StudentInfo();
        student2.setName("rose");
        student2.setNumber(new HashMap<String, Long>() {{
            put("hilbert", 8205050122L);
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
        //client.addStudentToSchool(null); ->cause exception!
    }

    private static void mockGetService() throws TException, StudentException {
        mockConstructStudent();
        client.printStudentsInfo(client.getStudentInfoByName("tom"));
        client.printStudentsInfo(client.getStudentInfoByName("john"));
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
```
