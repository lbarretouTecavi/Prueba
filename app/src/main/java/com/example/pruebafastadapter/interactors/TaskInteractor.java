package com.example.pruebafastadapter.interactors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.pruebafastadapter.data.api.services.ITaskService;
import com.example.pruebafastadapter.data.api.services.TaskService;
import com.example.pruebafastadapter.data.db.FirebaseHelper;
import com.example.pruebafastadapter.data.db.repository.ITaskRepository;
import com.example.pruebafastadapter.data.db.repository.TaskRepository;
import com.example.pruebafastadapter.data.models.DeviceToken;
import com.example.pruebafastadapter.data.models.Notification;
import com.example.pruebafastadapter.data.models.Task;
import com.google.firebase.database.ChildEventListener;

import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TaskInteractor implements ITaskInteractor {
    private final ITaskRepository repository;
    private final ITaskService service;
    private final FirebaseHelper firebaseHelper;

    public TaskInteractor(Context context) {
        this.repository = TaskRepository.getInstance(context);
        this.service = new TaskService();
        this.firebaseHelper = new FirebaseHelper();
    }

    @Override
    public Observable<List<Task>> listTaskFromRepositoryAsync() {
        return repository.getAllTaskNotCheckedAsync();
    }

    @Override
    public Observable<List<Task>> listTaskFromApi() {
        return service.getAllTasks()
                .observeOn(Schedulers.io())
                .flatMap(tasks -> {
                    return Observable.just(tasks);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void downloadTasksFromApiToRepository() {
        service.getAllTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(apiTasks -> {
                    List<Task> localTasks = repository.getAllTask();
                    List<Task> tasksToAdds = new ArrayList<>();
                    if (!apiTasks.isEmpty()) {
                        if (!localTasks.isEmpty()) {
                            long lastLocalTaskId = localTasks.get(localTasks.size() - 1).getId();
                            Set<String> localTaskIds = localTasks.stream()
                                    .map(Task::getIdUnique)
                                    .collect(Collectors.toSet());
                            for (Task apiTask : apiTasks) {
                                if (!localTaskIds.contains(apiTask.getIdUnique())) {
                                    apiTask.setId(++lastLocalTaskId);
                                    tasksToAdds.add(apiTask);
                                } else {
                                    repository.putTask(apiTask);
                                }
                            }
                            if (!tasksToAdds.isEmpty()) {
                                repository.saveListTask(tasksToAdds);
                            }
                        } else {
                            repository.saveListTask(apiTasks);
                        }
                    } else {
                        Log.e("ADVERTENCIA", "La data en la api es nula");
                    }
                }, throwable -> {
                    Log.e("INTERACTOR", "Error al sincronizar la data de la api al repositorio", throwable);
                });
    }


    @Override
    public void syncAllTasksFromRepositoryToApi(List<Task> tasks) {
        service.syncTaskFromRepositoryToApi(tasks)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseList -> {
                        Task getTask = new Task();
                        for (Task task : responseList) {
                            getTask = repository.getTask(task.getIdUnique());
                            task.setId(getTask.getId());
                            task.setCreated(task.getCreated().replace("T", " "));
                            task.setUpdated(task.getUpdated().replace("T"," "));
                            repository.putTask(task);
                            firebaseHelper.updateTask(task);
                            firebaseHelper.actualizarTask(task);
                            firebaseHelper.deleteTask(task);
                        }
                    },
                    throwable -> {
                        Log.e("INTERACTOR", "Error al sincronizar todas las tareas", throwable);
                    });
    }

    @Override
    public void syncTaskFromFirebase(ChildEventListener callback) {
        firebaseHelper.getDatabaseReference().addChildEventListener(callback);
    }

    @Override
    public Observable<List<Task>> listTaskChecked() {
        return repository.getAllTaskChecked();
    }

    @Override
    public List<Task> listTaskNotSincronized() {
        List<Task> list = repository.getAllTaskNotSincronized();
        if (list!= null) {
            return list;
        } else {
            return null;
        }
    }

    @Override
    public void createTask(Task task) {
        if (task != null) {
            repository.postTask(task);
            firebaseHelper.saveTask(task);
            firebaseHelper.crearTask(task);
        } else {
            Log.e("ERROR ITERACTOR", "No se pudo guardar la tarea");
        }
    }

    @Override
    public void updateTask(Task task) {
        Task taskExist = repository.getTask(task.getIdUnique());
        if (taskExist != null) {
            taskExist.setContent(task.getContent());
            taskExist.setIsCheck(task.getIsCheck());
            repository.putTask(taskExist);
            firebaseHelper.updateTask(taskExist);
            firebaseHelper.actualizarTask(taskExist);
        } else {
            Log.e("ERROR INTERACTOR", "No se pudo actualizar la tarea");
        }
    }

    @Override
    public void deleteTask(Task task) {
        Task taskExist = repository.getTask(task.getIdUnique());
        if (taskExist != null) {
            repository.deleteTask(taskExist);
            firebaseHelper.deleteTask(task);
            firebaseHelper.eliminarTask(task);
        } else {
            Log.e("ERROR INTERACTOR", "No se pudo eliminar la tarea");
        }
    }

    @Override
    public void registerDeviceToken(DeviceToken token) {
        service.registerDeviceToken(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                        Log.e("INTERACTOR", "Token registrado");
                     }, throwable -> {
                         Log.e("INTERACTOR", "Error al registrar el token", throwable);
                     });
    }

    @Override
    public void sendNotificationAllDevices(Notification notification) {
        service.sendNotificationAllDevices(notification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Log.e("INTERACTOR", "Notificacion enviada");
                        }, throwable -> {
                            Log.e("INTERACTOR", "Error al enviar la notificacion", throwable);
                        });
    }
}
