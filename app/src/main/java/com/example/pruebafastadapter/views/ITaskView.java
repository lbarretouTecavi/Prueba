package com.example.pruebafastadapter.views;

import com.example.pruebafastadapter.data.models.Task;

import java.util.List;

public interface ITaskView {
    void viewTasks(List<Task> tasks);
    void viewMessage(String message);
    void viewMessageError(String message);
    void refreshRecyclerView();
}
