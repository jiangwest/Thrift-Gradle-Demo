//定义服务

include "studentModel.thrift"
include "studentException.thrift"

// 一个服务的定义在语义上相当于面向对象编程中的一个接口
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
