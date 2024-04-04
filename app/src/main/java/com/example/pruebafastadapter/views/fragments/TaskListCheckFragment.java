package com.example.pruebafastadapter.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pruebafastadapter.R;
import com.example.pruebafastadapter.data.models.Task;
import com.example.pruebafastadapter.presenters.ITaskPresenter;
import com.example.pruebafastadapter.presenters.TaskPresenter;
import com.example.pruebafastadapter.utils.IFastItem;
import com.example.pruebafastadapter.utils.FastItem;
import com.example.pruebafastadapter.views.ITaskView;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TaskListCheckFragment extends Fragment implements ITaskView, IFastItem {
    private RecyclerView recyclerViewTask;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout linearLayout;
    private ITaskPresenter presenter;
    private FastItemAdapter<FastItem> fastItemAdapter;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task_list_check, container, false);
        recyclerViewTask = view.findViewById(R.id.recyclerViewListTaskCheck);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutChecked);
        linearLayout = view.findViewById(R.id.linearLayoutListTaskChecked);
        recyclerViewTask.setHasFixedSize(true);
        recyclerViewTask.setLayoutManager(new LinearLayoutManager(getContext()));
        fastItemAdapter  = new FastItemAdapter<>();
        recyclerViewTask.setItemAnimator(null);
        recyclerViewTask.setAdapter(fastItemAdapter);
        presenter = new TaskPresenter(getContext(), this);
        presenter.listTaskChecked();
        refreshListCheckTask();
        return view;
    }

    @Override
    public void viewTasks(List<Task> tasks) {
        if (!tasks.isEmpty()) {
            linearLayout.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            fastItemAdapter.clear();
            for (Task task : tasks) {
                fastItemAdapter.add(new FastItem(this, task, R.layout.task_item));
            }
            fastItemAdapter.notifyDataSetChanged();
        } else {
            swipeRefreshLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
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
        presenter.listTaskChecked();
    }

    @Override
    public void onClickTask(Task task) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Advertencia")
                .setContentText("¿Desea eliminar esta tarea?")
                .setConfirmButton("Si", sweetAlertDialog -> {
                    presenter.deleteTask(task);
                    presenter.listTaskChecked();
                    sweetAlertDialog.dismissWithAnimation();
                })
                .setCancelButton("Cancelar", sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation())
                .show();
    }

    void refreshListCheckTask () {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.listTaskChecked();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

}