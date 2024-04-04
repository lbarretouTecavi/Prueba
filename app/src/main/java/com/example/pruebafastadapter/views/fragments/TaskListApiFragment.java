package com.example.pruebafastadapter.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pruebafastadapter.R;
import com.example.pruebafastadapter.data.models.Task;
import com.example.pruebafastadapter.presenters.ITaskPresenter;
import com.example.pruebafastadapter.presenters.TaskPresenter;
import com.example.pruebafastadapter.utils.IFastItem;
import com.example.pruebafastadapter.utils.NetworkConnection;
import com.example.pruebafastadapter.utils.FastItem;
import com.example.pruebafastadapter.views.ITaskView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TaskListApiFragment extends Fragment implements ITaskView, IFastItem {
    private RecyclerView recyclerViewTask;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BottomSheetDialog btnSheetDialog;
    private TextView txtMessageListEmpty, txtNotificationBadge;
    private ImageButton btnSincronizeTask;
    private ITaskPresenter presenter;
    private FastItemAdapter<FastItem> fastItemAdapter;
    private TaskSincronizedFragmentDialog sincronizedFragmentDialog = new TaskSincronizedFragmentDialog();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list_api, container, false);
        btnSheetDialog = new BottomSheetDialog(getContext());
        txtMessageListEmpty = view.findViewById(R.id.txtMessageListApiEmpty);
        recyclerViewTask = view.findViewById(R.id.recyclerViewListApiTask);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutApi);
        btnSincronizeTask = view.findViewById(R.id.btnUploadTaskList);
        txtNotificationBadge = view.findViewById(R.id.notificationBadge);

        recyclerViewTask.setHasFixedSize(true);
        recyclerViewTask.setLayoutManager(new LinearLayoutManager(getContext()));
        fastItemAdapter  = new FastItemAdapter<>();
        recyclerViewTask.setItemAnimator(null);
        recyclerViewTask.setAdapter(fastItemAdapter);
        presenter = new TaskPresenter(getContext(), this);

        if (NetworkConnection.isConnectedToNetwork(getContext())) {
            presenter.listTaskFromApi();
            btnSincronizeTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sincronizedFragmentDialog.show(getParentFragmentManager(), "TaskSincronizedFragmentDialog");
//                    SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
//                    dialog.setTitleText("¿Desea enviar los datos hacia la api?");
//                    dialog.setContentText("Se sincronizarán todos los datos guardados localmente en el dispositivo");
//                    dialog.setConfirmButton("Si", new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    List<Task> list = presenter.listTasksFromRepository();
//                                    if (!list.isEmpty()) {
//                                        presenter.sincronizedTasksFromRepositoryToApi(list);
//                                        dialog.dismissWithAnimation();
//                                    } else {
//                                        dialog.dismissWithAnimation();
//                                        showAlertErrorWarning("No existen datos almacenados en el dispositivo", 3);
//                                    }
//                                }
//                            });
//                    dialog.setCancelButton("Cancelar", new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    sweetAlertDialog.dismiss();
//                                }
//                            });
//                    dialog.show();
                }
            });
        } else {
            showAlertErrorWarning("Lo siento, operación inválida debido a la falta de conexión a internet 🚫📶🛜", 1);
        }
        setBadgeDrawableNotification();
        refreshListTaskApi();
        return view;
    }

    @Override
    public void viewTasks(List<Task> tasks) {
        fastItemAdapter.clear();
        for (Task task : tasks) {
            fastItemAdapter.add(new FastItem(this, task, R.layout.task_item));
        }
        fastItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void viewMessage(String message) {
        if (getContext() != null) {
            showAlertErrorWarning(message,2);
        }
    }

    @Override
    public void viewMessageError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void refreshRecyclerView() {

    }

    @Override
    public void onClickTask(Task task) {

    }

    void setBadgeDrawableNotification() {
        List<Task> taskList = presenter.listTaskNotSincronized();
        if (!taskList.isEmpty()) {
            int numberNotifications = taskList.size();
            txtNotificationBadge.setVisibility(View.VISIBLE);
            txtNotificationBadge.setText(String.valueOf(numberNotifications).concat("+"));
        } else {
            txtNotificationBadge.setVisibility(View.GONE);
        }
    }

    void refreshListTaskApi() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (NetworkConnection.isConnectedToNetwork(getContext())) {
                presenter.listTaskFromApi();
            } else {
                fastItemAdapter.clear();
                btnSincronizeTask.setOnClickListener(v -> showAlertErrorWarning("Lo siento, operación inválida debido a la falta de conexión a internet 🚫📶🛜", 1));
                showAlertErrorWarning("Lo siento, operación inválida debido a la falta de conexión a internet 🚫📶🛜", 1);
            }
            setBadgeDrawableNotification();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    void showAlertErrorWarning(String message, int typeAlert) {
        new SweetAlertDialog(getContext(),typeAlert)
                .setTitleText("Oops...")
                .setContentText(message)
                .show();
    }
}