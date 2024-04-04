package com.example.pruebafastadapter.data.api.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pruebafastadapter.data.api.HttpClient;
import com.example.pruebafastadapter.data.models.DeviceToken;
import com.example.pruebafastadapter.data.models.Notification;
import com.example.pruebafastadapter.data.models.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TaskService implements ITaskService {
    private static final String URL_API = "https://cam.tecavi.net/api/task/";

    private final HttpClient httpClient;

    public TaskService() {
        httpClient = new HttpClient();
    }

    @Override
    public Observable<List<Task>> getAllTasks() {
        String url = URL_API + "list";
        return Observable.fromCallable(new Callable<List<Task>>() {
            @Override
            public List<Task> call() throws Exception {
                String jsonResponse = httpClient.getAllTasks(url);
                ObjectMapper objectMapper = new ObjectMapper();
                List<Task> tasks = objectMapper.readValue(jsonResponse, new TypeReference<List<Task>>() {});
                for (Task task : tasks) {
                    task.setCreated(task.getCreated().replace("T", " "));
                    task.setUpdated(task.getUpdated().replace("T"," "));
                }
                return tasks;
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Task>> syncTaskFromRepositoryToApi(List<Task> tasks) {
        String url = URL_API.concat("syncAll");
        return Observable.fromCallable(() -> {
            String jsonResonse = httpClient.syncAllTask(url, tasks);
            ObjectMapper objectMapper = new ObjectMapper();
            List<Task> list = objectMapper.readValue(jsonResonse, new TypeReference<List<Task>>() {});
            return list;
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable registerDeviceToken(DeviceToken token) {
        String url = URL_API.concat("deviceToken");
        return Completable.fromCallable(() -> {
            String jsonResonse = httpClient.registerDeviceToken(url, token);
            return jsonResonse;
        })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable sendNotificationAllDevices(Notification notification) {
        String url = URL_API.concat("sendNotification");
        return Completable.fromCallable(
                () -> {
                    String jsonResonse = httpClient.senNotificationAllDevices(url, notification);
                    return jsonResonse;
                }
        ).subscribeOn(Schedulers.io());
    }


}