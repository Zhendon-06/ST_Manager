package com.example.st_manager.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "teachers")
public class Teacher {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String teacherNo;
    private String name;
    private String gender;
    private String department;
    private String title;
    private String phone;
    private long createdAt;

    public Teacher(String teacherNo, String name, String gender, String department, String title, String phone, long createdAt) {
        this.teacherNo = teacherNo;
        this.name = name;
        this.gender = gender;
        this.department = department;
        this.title = title;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeacherNo() {
        return teacherNo;
    }

    public void setTeacherNo(String teacherNo) {
        this.teacherNo = teacherNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
