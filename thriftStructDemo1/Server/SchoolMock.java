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
