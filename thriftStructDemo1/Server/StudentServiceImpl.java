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
        student2.setName("tom");
        student2.setHobby(new ArrayList<String>(Arrays.asList("tennis", "swimming", "volleyball")));
        student2.setNumber(new HashMap<String, Long>() {{
            put("brucezhang", 8205050122L);
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
