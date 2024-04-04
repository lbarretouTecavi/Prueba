package com.example.pruebafastadapter.views.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pruebafastadapter.R;
import com.example.pruebafastadapter.data.models.Task;
import com.example.pruebafastadapter.presenters.ITaskPresenter;
import com.example.pruebafastadapter.presenters.TaskPresenter;
import com.example.pruebafastadapter.views.ITaskView;

import java.util.List;

public class TaskAddFragment extends Fragment implements ITaskView {
    private View view;
    private ITaskPresenter presenter;
    private EditText txtContent;
    private Button btnAddTask;

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task_add, container, false);
        txtContent = view.findViewById(R.id.txtContentAddTask);
        btnAddTask = view.findViewById(R.id.btnAgregarAddTask);
        presenter = new TaskPresenter(getContext(), this);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = txtContent.getText().toString();
                if (!content.isEmpty()) {
                    Task task = new Task();
                    task.setContent(content);
                    task.setIsCheck(false);
                    task.setIsSincronized(false);
                    presenter.createTask(task);
                    txtContent.setText("");
                } else {
                    showAlertDialog("El contenido no puede estar vacío");
                }

            }
        });
        return view;
    }

    @Override
    public void viewTasks(List<Task> tasks) {

    }

    @Override
    public void viewMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void viewMessageError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshRecyclerView() {

    }

    private void showAlertDialog(String mensaje) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Advertencia ⚠️");
        alert.setMessage(mensaje);
        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}