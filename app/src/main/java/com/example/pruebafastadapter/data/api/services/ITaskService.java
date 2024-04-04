package com.example.pruebafastadapter.data.api.services;

import com.example.pruebafastadapter.data.models.DeviceToken;
import com.example.pruebafastadapter.data.models.Notification;
import com.example.pruebafastadapter.data.models.Task;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface ITaskService {
    Observable<List<Task>> getAllTasks();
    Observable<List<Task>> syncTaskFromRepositoryToApi(List<Task> tasks);
    Completable registerDeviceToken(DeviceToken token);
    Completable sendNotificationAllDevices(Notification notification);
}
