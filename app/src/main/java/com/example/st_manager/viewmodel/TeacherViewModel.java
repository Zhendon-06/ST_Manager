package com.example.st_manager.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.st_manager.data.Teacher;
import com.example.st_manager.repository.TeacherRepository;

import java.util.List;

public class TeacherViewModel extends AndroidViewModel {
    private final TeacherRepository repository;
    private final MutableLiveData<String> keyword = new MutableLiveData<>("");
    private final LiveData<List<Teacher>> teachers;

    public TeacherViewModel(@NonNull Application application) {
        super(application);
        repository = new TeacherRepository(application);
        teachers = Transformations.switchMap(keyword, value -> {
            if (value == null || value.trim().isEmpty()) {
                return repository.getAllTeachers();
            }
            return repository.searchTeachers(value.trim());
        });
    }

    public LiveData<List<Teacher>> getTeachers() {
        return teachers;
    }

    public void setKeyword(String value) {
        keyword.setValue(value == null ? "" : value);
    }

    public void insert(Teacher teacher) {
        repository.insert(teacher);
    }

    public void update(Teacher teacher) {
        repository.update(teacher);
    }

    public void delete(Teacher teacher) {
        repository.delete(teacher);
    }
}
