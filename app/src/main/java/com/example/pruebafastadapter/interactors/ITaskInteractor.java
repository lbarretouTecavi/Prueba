package com.example.pruebafastadapter.interactors;

import com.example.pruebafastadapter.data.models.DeviceToken;
import com.example.pruebafastadapter.data.models.Notification;
import com.example.pruebafastadapter.data.models.Task;
import com.google.firebase.database.ChildEventListener;

import java.util.List;

import io.reactivex.Observable;

public interface ITaskInteractor {
    Observable<List<Task>> listTaskFromRepositoryAsync();
    Observable<List<Task>> listTaskFromApi();
    void downloadTasksFromApiToRepository();
    void syncAllTasksFromRepositoryToApi(List<Task> tasks);
    void syncTaskFromFirebase(ChildEventListener callback);
    Observable<List<Task>> listTaskChecked();
    List<Task> listTaskNotSincronized();
    void createTask(Task task);
    void updateTask(Task task);
    void deleteTask(Task task);
    void registerDeviceToken(DeviceToken token);
    void sendNotificationAllDevices(Notification notification);
}
