package com.example.dell.classmanagmentapp;

public class UserObjectClass {
    private String name, email, uid, phone,joinCode;
    private boolean isFaculty;

    public UserObjectClass(){}

    public UserObjectClass(String uid, String name, String email, String phone, boolean isFaculty){
        this.email = email;
        this.isFaculty = isFaculty;
        this.name = name;
        this.phone = phone;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isFaculty() {
        return isFaculty;
    }

    public void setFaculty(boolean faculty) {
        this.isFaculty = faculty;
    }
}
