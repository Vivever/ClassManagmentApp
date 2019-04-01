package com.example.dell.classmanagmentapp;

public class ClassStudentClass {
    String roll,facultyEmail, joinCode, studentUID, classKey;

    public ClassStudentClass(String roll,String facultyEmail, String joinCode, String studentUID, String classKey) {
        this.facultyEmail = facultyEmail;
        this.roll = roll;
        this.joinCode = joinCode;
        this.studentUID = studentUID;
        this.classKey = classKey;
    }

    public ClassStudentClass() {
    }
}
