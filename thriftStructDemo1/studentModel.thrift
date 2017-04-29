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
