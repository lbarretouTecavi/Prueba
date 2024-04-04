package com.example.pruebafastadapter.presenters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pruebafastadapter.data.models.DeviceToken;
import com.example.pruebafastadapter.data.models.Notification;
import com.example.pruebafastadapter.data.models.Task;
import com.example.pruebafastadapter.interactors.ITaskInteractor;
import com.example.pruebafastadapter.interactors.TaskInteractor;
import com.example.pruebafastadapter.views.ITaskView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TaskPresenter implements ITaskPresenter {
    private final ITaskInteractor interactor;
    private final ITaskView view;

    public TaskPresenter(Context context, ITaskView view) {
        this.interactor = new TaskInteractor(context);
        this.view = view;
    }

    @Override
    public void listTask() {
        interactor.listTaskFromRepositoryAsync()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tasks -> {
                    if (tasks != null) {
                        view.viewTasks(tasks);
                    } else {
                        view.viewMessage("No se encontraron tareas");
                    }
                }, err -> {
                    view.viewMessageError(err.getMessage());
                });
    }

    @Override
    public void listTaskFromApi() {
        interactor.listTaskFromApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tasks -> {
                    if (tasks != null && !tasks.isEmpty()) {
                        view.viewTasks(tasks);
                    } else {
                        view.viewMessage("No se encontraron datos en la api");
                    }
                }, err -> {
                    view.viewMessageError(err.getMessage());
                });
    }

    @Override
    public void listTaskChecked() {
        interactor.listTaskChecked()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tasks -> {
                    if (tasks != null) {
                        view.viewTasks(tasks);
                    } else {
                        view.viewMessage("Problemas al listar las tareas completadas");
                    }
                }, err -> {
                    view.viewMessageError(err.getMessage());
                });
    }

    @Override
    public List<Task> listTaskNotSincronized() {
        return interactor.listTaskNotSincronized();
    }

    @Override
    public void listTaskAllNotSincronized() {
        List<Task> list = interactor.listTaskNotSincronized();
        if (list!= null) {
            view.viewTasks(list);
        } else {
            view.viewMessage("Problemas al listar las tareas no sincronizadas");
        }
    }

    @Override
    public void sincronizedTasksFromApiToRepository() {
        interactor.downloadTasksFromApiToRepository();
    }

    @Override
    public void syncAllTasksFromRepositoryToApi(List<Task> tasks) {
        interactor.syncAllTasksFromRepositoryToApi(tasks);
    }

    @Override
    public void createTask(Task task) {
        if (task != null) {
            interactor.createTask(task);
            view.viewMessage("Guardado con éxito");
        } else {
            view.viewMessageError("Problemas al guardar la tarea");
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task != null) {
            interactor.updateTask(task);
            view.viewMessage("Actualizado con éxito");
        } else {
            view.viewMessageError("Problemas al actualizar la tarea");
        }
    }

    @Override
    public void deleteTask(Task task) {
        if (task != null) {
            interactor.deleteTask(task);
            view.viewMessage("Eliminado con éxito");
        } else {
            view.viewMessageError("Problemas al eliminar la tarea");
        }
    }

    @Override
    public void listTaskFromFirebase() {
        HashSet<Task> list = new HashSet<>();
        interactor.syncTaskFromFirebase(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Task task = snapshot.getValue(Task.class);
                if (task!= null) {
                    list.add(task);
                }
                view.viewTasks(orderByDateUpdated(list));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                view.refreshRecyclerView();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                view.refreshRecyclerView();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                view.refreshRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                view.viewMessageError(error.getMessage());
            }
        });
    }

    @Override
    public void registerDeviceToken(DeviceToken token) {
        interactor.registerDeviceToken(token);
    }

    @Override
    public void sendNotificationAllDevices(Notification notification) {
        interactor.sendNotificationAllDevices(notification);
    }

    private List<Task> orderByDateUpdated(HashSet<Task> tasks) {
        List<Task> list = new ArrayList<>(tasks);
        list.sort((o1, o2) -> o2.getUpdated().compareTo(o1.getUpdated()));
        return list;
    }
}
