package com.example.st_manager.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.st_manager.data.AppDatabase;
import com.example.st_manager.data.Student;
import com.example.st_manager.data.StudentDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentRepository {
    private final StudentDao studentDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public StudentRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        studentDao = database.studentDao();
    }

    public LiveData<List<Student>> getAllStudents() {
        return studentDao.getAllStudents();
    }

    public LiveData<List<Student>> searchStudents(String keyword) {
        return studentDao.searchStudents(keyword);
    }

    public void insert(Student student) {
        executorService.execute(() -> studentDao.insert(student));
    }

    public void update(Student student) {
        executorService.execute(() -> studentDao.update(student));
    }

    public void delete(Student student) {
        executorService.execute(() -> studentDao.delete(student));
    }
}
