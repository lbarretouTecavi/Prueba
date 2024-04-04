package com.example.pruebafastadapter.data.db.repository;

import com.example.pruebafastadapter.data.models.Task;

import java.util.List;

import io.reactivex.Observable;

public interface ITaskRepository {
    List<Task> getAllTask();
    Observable<List<Task>> getAllTaskNotCheckedAsync();
    Observable<List<Task>> getAllTaskChecked();
    List<Task> getAllTaskNotSincronized();
    Task getTask(String idUnique);
    void postTask(Task task);
    void saveListTask(List<Task> tasks);
    void putTask(Task task);
    void deleteTask(Task task);
}
