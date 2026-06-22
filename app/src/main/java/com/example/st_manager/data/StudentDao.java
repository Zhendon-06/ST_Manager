package com.example.st_manager.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudentDao {
    @Insert
    void insert(Student student);

    @Update
    void update(Student student);

    @Delete
    void delete(Student student);

    @Query("SELECT * FROM students ORDER BY createdAt DESC")
    LiveData<List<Student>> getAllStudents();

    @Query("SELECT * FROM students WHERE name LIKE '%' || :keyword || '%' OR studentNo LIKE '%' || :keyword || '%' OR college LIKE '%' || :keyword || '%' OR className LIKE '%' || :keyword || '%' OR phone LIKE '%' || :keyword || '%' ORDER BY createdAt DESC")
    LiveData<List<Student>> searchStudents(String keyword);
}
