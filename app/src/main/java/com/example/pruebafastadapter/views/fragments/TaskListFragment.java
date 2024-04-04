package com.example.pruebafastadapter.views.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pruebafastadapter.R;
import com.example.pruebafastadapter.data.api.HttpClient;
import com.example.pruebafastadapter.data.api.RetrofitClient;
import com.example.pruebafastadapter.data.api.services.ITaskService;
import com.example.pruebafastadapter.data.models.DeviceToken;
import com.example.pruebafastadapter.data.models.Task;
import com.example.pruebafastadapter.presenters.ITaskPresenter;
import com.example.pruebafastadapter.presenters.TaskPresenter;
import com.example.pruebafastadapter.utils.IFastItem;
import com.example.pruebafastadapter.utils.NetworkConnection;
import com.example.pruebafastadapter.utils.FastItem;
import com.example.pruebafastadapter.views.ITaskView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TaskListFragment extends Fragment implements ITaskView, IFastItem {
    private RecyclerView recyclerViewTask;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BottomSheetDialog btnSheetDialog;
    private CheckBox cbxCheckTask;
    private TextView txtMessageListEmpty;
    private ImageButton btnSincronize, btnCheckTask;
    private ImageView imgSincronized;
    private LinearLayout linearLayout;
    private ITaskPresenter presenter;
    private FastItemAdapter<FastItem> fastItemAdapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task_list, container, false);
        btnSheetDialog = new BottomSheetDialog(getContext());
        recyclerViewTask = view.findViewById(R.id.recyclerViewListTask);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        btnSincronize = view.findViewById(R.id.btnSincronizeListTask);
        imgSincronized = view.findViewById(R.id.imgSincronizedTask);
        linearLayout = view.findViewById(R.id.linearLayoutListTask);

        recyclerViewTask.setHasFixedSize(true);
        recyclerViewTask.setLayoutManager(new LinearLayoutManager(getContext()));
        fastItemAdapter  = new FastItemAdapter<>();
        recyclerViewTask.setItemAnimator(null);
        recyclerViewTask.setAdapter(fastItemAdapter);

        presenter = new TaskPresenter(getContext(), this);
        presenter.listTask();

        btnSincronize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertSincronize("Sincronización api-local", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        if(NetworkConnection.isConnectedToNetwork(getContext())) {
                            presenter.sincronizedTasksFromApiToRepository();
                            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Descarga exitosa")
                                    .show();
                        } else {
                            sweetAlertDialog.dismissWithAnimation();
                            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Opps ...")
                                    .setContentText("Lo siento, la sincronización falló debido a la falta de conexión a internet 🚫📶🛜")
                                    .show();
                        }
                    }
                });
            }
        });
        refreshListTask();
        registerDeviceId();
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        String token = sharedPreferences.getString("FCMToken", null);
//        Log.e("TOKEN SHARED", token);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.listTask();
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
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
        createBottomSheetDialog(task);
        btnSheetDialog.show();
    }


    private void registerDeviceId() {
//        FirebaseMessaging.getInstance().subscribeToTopic("CrudTask")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
//                        String msg = "Suscrito";
//                        if (!task.isSuccessful()) {
//                            msg = "Suscripcion errónea";
//                        }
//                        Log.e("FCM Topic", msg);
//                    }
//                });
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Fallo", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        String tokenGuardado = getActivity().getSharedPreferences("MyPrefs", 0)
                                .getString("FCMToken", null);
                        if (token != null) {
                            if (tokenGuardado == null || !token.equals(tokenGuardado)) {
                                DeviceToken deviceToken = new DeviceToken();
                                deviceToken.setDeviceToken(token);
                                presenter.registerDeviceToken(deviceToken);
                                saveDeviceToken(token);
                            }
                        }
                    }
                });
    }

    private void saveDeviceToken(String deviceToken) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FCMToken", deviceToken);
        editor.apply();
    }

    private void createBottomSheetDialog(Task task) {
        view = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
        Button btnUpdateTask = view.findViewById(R.id.btnActualizarUpdateTask);
        btnCheckTask = view.findViewById(R.id.btnCheckTask);
        EditText txtContent = view.findViewById(R.id.txtContentUpdateTask);
        txtContent.setText(task.getContent());

        btnUpdateTask.setOnClickListener(v -> {
            String content = txtContent.getText().toString();
            btnSheetDialog.dismiss();
            if (content.isEmpty()) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("El contenido no puede estar vacío")
                        .show();
            } else {
                task.setContent(content);
                task.setUpdated(convertDateToString());
                task.setIsSincronized(false);
                presenter.updateTask(task);
                presenter.listTask();
            }
        });

        btnCheckTask.setOnClickListener(v -> {
            showAlertCheckTask("Completar tarea", "¿Desea completar la tarea?", 3, task);
            btnSheetDialog.dismiss();
        });
        btnSheetDialog.setContentView(view);
        presenter.listTask();
    }

    private void showAlertSincronize(String mensaje, SweetAlertDialog.OnSweetClickListener listener) {
        SweetAlertDialog alert = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
        alert.setTitle("Advertencia");
        alert.setContentText(mensaje);
        alert.setConfirmButton("Aceptar", listener);
        alert.setCancelButton("Cancelar", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                alert.dismissWithAnimation();
            }
        });
        alert.show();
    }

    private void showAlertCheckTask(String title, String content, int typeAlert, Task task) {
        SweetAlertDialog alert = new SweetAlertDialog(getContext(),typeAlert);
        alert.setTitleText(title);
        alert.setContentText(content);
        alert.setConfirmButton("Si", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                task.setIsCheck(true);
                task.setUpdated(convertDateToString());
                task.setIsSincronized(false);
                presenter.updateTask(task);
                presenter.listTask();
                alert.dismissWithAnimation();
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Felecidades!")
                        .setContentText("La tarea se completó con éxito!")
                        .show();
            }
        });
        alert.setCancelButton("Cancelar", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
        alert.show();
    }

    void refreshListTask() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.listTask();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private String convertDateToString() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }
}