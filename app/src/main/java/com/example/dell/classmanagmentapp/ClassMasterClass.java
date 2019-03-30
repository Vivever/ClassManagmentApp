package com.example.dell.classmanagmentapp;

public class ClassMasterClass {
    String facultyUID, className, joinCode, sessionStart, sessionEnd, startRoll, endRoll;

    public ClassMasterClass(String facultyUID, String className, String joinCode, String sessionStart, String sessionEnd, String startRoll, String endRoll) {
        this.facultyUID = facultyUID;
        this.className = className;
        this.joinCode = joinCode;
        this.sessionStart = sessionStart;
        this.sessionEnd = sessionEnd;
        this.startRoll = startRoll;
        this.endRoll = endRoll;
    }

    public ClassMasterClass() {
    }
}
