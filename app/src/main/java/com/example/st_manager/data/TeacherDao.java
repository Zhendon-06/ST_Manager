package com.example.st_manager.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TeacherDao {
    @Insert
    void insert(Teacher teacher);

    @Update
    void update(Teacher teacher);

    @Delete
    void delete(Teacher teacher);

    @Query("SELECT * FROM teachers ORDER BY createdAt DESC")
    LiveData<List<Teacher>> getAllTeachers();

    @Query("SELECT * FROM teachers WHERE name LIKE '%' || :keyword || '%' OR teacherNo LIKE '%' || :keyword || '%' OR department LIKE '%' || :keyword || '%' OR title LIKE '%' || :keyword || '%' OR phone LIKE '%' || :keyword || '%' ORDER BY createdAt DESC")
    LiveData<List<Teacher>> searchTeachers(String keyword);
}
