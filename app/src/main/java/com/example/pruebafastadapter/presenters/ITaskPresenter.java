package com.example.pruebafastadapter.presenters;

import com.example.pruebafastadapter.data.models.DeviceToken;
import com.example.pruebafastadapter.data.models.Notification;
import com.example.pruebafastadapter.data.models.Task;

import java.util.List;

public interface ITaskPresenter {
    void listTask();
    void listTaskFromApi();
    void listTaskChecked();
    List<Task> listTaskNotSincronized();
    void listTaskAllNotSincronized();
    void sincronizedTasksFromApiToRepository();
    void syncAllTasksFromRepositoryToApi(List<Task> tasks);
    void createTask(Task task);
    void updateTask(Task task);
    void deleteTask(Task task);
    void listTaskFromFirebase();
    void registerDeviceToken(DeviceToken token);
    void sendNotificationAllDevices(Notification notification);
}
