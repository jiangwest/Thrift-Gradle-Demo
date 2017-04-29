//定义异常
exception StudentException {
    1: required i64 errorCode;
    2: required string description;
    3: optional string causeInfo;
}
