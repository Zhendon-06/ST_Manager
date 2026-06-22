package com.example.st_manager.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.st_manager.data.AppDatabase;
import com.example.st_manager.data.Teacher;
import com.example.st_manager.data.TeacherDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TeacherRepository {
    private final TeacherDao teacherDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TeacherRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        teacherDao = database.teacherDao();
    }

    public LiveData<List<Teacher>> getAllTeachers() {
        return teacherDao.getAllTeachers();
    }

    public LiveData<List<Teacher>> searchTeachers(String keyword) {
        return teacherDao.searchTeachers(keyword);
    }

    public void insert(Teacher teacher) {
        executorService.execute(() -> teacherDao.insert(teacher));
    }

    public void update(Teacher teacher) {
        executorService.execute(() -> teacherDao.update(teacher));
    }

    public void delete(Teacher teacher) {
        executorService.execute(() -> teacherDao.delete(teacher));
    }
}
