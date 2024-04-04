package com.example.pruebafastadapter.views.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pruebafastadapter.R;
import com.example.pruebafastadapter.data.models.Notification;
import com.example.pruebafastadapter.data.models.Task;
import com.example.pruebafastadapter.presenters.ITaskPresenter;
import com.example.pruebafastadapter.presenters.TaskPresenter;
import com.example.pruebafastadapter.utils.FastItem;
import com.example.pruebafastadapter.utils.IFastItem;
import com.example.pruebafastadapter.views.ITaskView;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TaskSincronizedFragmentDialog extends DialogFragment implements IFastItem, ITaskView {
    private RecyclerView recyclerViewListTaskNotSincronized;
    private Button btnClose, btnSubirCambios;
    private TextView txtContenteList;
    private Notification notification;
    private FastItemAdapter<FastItem> fastItemAdapter;
    private ITaskPresenter presenter;
    private View view;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return createDialog();
    }

    private Dialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_dialog_task_sincronized, null);
        return builder.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dialog_task_sincronized, container, false);
        recyclerViewListTaskNotSincronized = view.findViewById(R.id.recyclerViewListTaskNotSincronized);
        btnClose = view.findViewById(R.id.btnDialogFragmentClose);
        btnSubirCambios = view.findViewById(R.id.btnSubirListTask);
        txtContenteList = view.findViewById(R.id.txtContentListTaskNotSincronized);

        recyclerViewListTaskNotSincronized.setHasFixedSize(true);
        recyclerViewListTaskNotSincronized.setLayoutManager(new LinearLayoutManager(getContext()));
        fastItemAdapter = new FastItemAdapter<>();
        recyclerViewListTaskNotSincronized.setItemAnimator(null);
        recyclerViewListTaskNotSincronized.setAdapter(fastItemAdapter);

        presenter = new TaskPresenter(getContext(), this);
        presenter.listTaskAllNotSincronized();

        btnClose.setOnClickListener(v -> dismiss());
        btnSubirCambios.setOnClickListener(v -> {
            SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                    dialog.setTitleText("¿Desea enviar los datos hacia la api?");
                    dialog.setContentText("Se sincronizarán todos los datos guardados localmente en el dispositivo");
                    dialog.setConfirmButton("Si", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    List<Task> list = presenter.listTaskNotSincronized();
                                    if (!list.isEmpty()) {
                                        presenter.syncAllTasksFromRepositoryToApi(list);
                                        presenter.sendNotificationAllDevices(
                                                new Notification("Nuevos datos","Se cargaron y/o actualizaron nuevos datos en el servidor.", recoveryTokenDevice())
                                        );
                                        dialog.dismissWithAnimation();
                                    } else {
                                        dialog.dismissWithAnimation();
                                        showAlertErrorWarning("No existen datos almacenados en el dispositivo", 3);
                                    }
                                    dismiss();
                                }
                            });
                    dialog.setCancelButton("Cancelar", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });
                    dialog.show();
        });
        return view;
    }

    @Override
    public void onClickTask(Task task) {

    }

    @Override
    public void viewTasks(List<Task> tasks) {
        if (!tasks.isEmpty()){
            txtContenteList.setVisibility(View.GONE);
            recyclerViewListTaskNotSincronized.setVisibility(View.VISIBLE);
            fastItemAdapter.clear();
            for (Task task : tasks) {
                fastItemAdapter.add(new FastItem(this, task, R.layout.task_sincronized_item));
            }
            fastItemAdapter.notifyDataSetChanged();
        } else {
            recyclerViewListTaskNotSincronized.setVisibility(View.GONE);
            txtContenteList.setVisibility(View.VISIBLE);
        }
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

    private String recoveryTokenDevice() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("FCMToken", null);
    }

    void showAlertErrorWarning(String message, int typeAlert) {
        SweetAlertDialog alert = new SweetAlertDialog(getContext(),typeAlert);
        alert.setTitleText("Oops...");
        alert.setTitleText(message);
        alert.show();
    }
}