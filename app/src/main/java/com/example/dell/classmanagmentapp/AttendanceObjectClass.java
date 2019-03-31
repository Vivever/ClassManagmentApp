package com.example.dell.classmanagmentapp;

import java.io.Serializable;

public class AttendanceObjectClass implements Serializable {
    int roll;
    String status;
    String date;

    public AttendanceObjectClass() {
    }

    public AttendanceObjectClass(int roll, String status, String date) {
        this.roll = roll;
        this.status = status;
        this.date = date;
    }
}
