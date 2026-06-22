package com.example.st_manager.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.st_manager.data.Student;
import com.example.st_manager.repository.StudentRepository;

import java.util.List;

public class StudentViewModel extends AndroidViewModel {
    private final StudentRepository repository;
    private final MutableLiveData<String> keyword = new MutableLiveData<>("");
    private final LiveData<List<Student>> students;

    public StudentViewModel(@NonNull Application application) {
        super(application);
        repository = new StudentRepository(application);
        students = Transformations.switchMap(keyword, value -> {
            if (value == null || value.trim().isEmpty()) {
                return repository.getAllStudents();
            }
            return repository.searchStudents(value.trim());
        });
    }

    public LiveData<List<Student>> getStudents() {
        return students;
    }

    public void setKeyword(String value) {
        keyword.setValue(value == null ? "" : value);
    }

    public void insert(Student student) {
        repository.insert(student);
    }

    public void update(Student student) {
        repository.update(student);
    }

    public void delete(Student student) {
        repository.delete(student);
    }
}
