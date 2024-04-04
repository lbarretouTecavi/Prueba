package com.example.pruebafastadapter.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pruebafastadapter.R;
import com.example.pruebafastadapter.data.api.HttpClient;
import com.example.pruebafastadapter.data.models.DeviceToken;
import com.example.pruebafastadapter.data.models.Task;
import com.example.pruebafastadapter.databinding.TaskViewBinding;
import com.example.pruebafastadapter.presenters.TaskPresenter;
import com.example.pruebafastadapter.views.fragments.TaskAddFragment;
import com.example.pruebafastadapter.views.fragments.TaskListApiFragment;
import com.example.pruebafastadapter.views.fragments.TaskListCheckFragment;
import com.example.pruebafastadapter.views.fragments.TaskListFragment;
import com.example.pruebafastadapter.views.fragments.TaskListRealtimeFragment;
import com.example.pruebafastadapter.views.fragments.TaskSincronizedFragmentDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.Date;

public class TaskView extends AppCompatActivity{
    TaskViewBinding binding;

    @SuppressLint({"NonConstantResourceId", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TaskViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        askNotificationPermission();

        loadFragment(new TaskListFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.bottomNavigationMenuListTask) {
                loadFragment(new TaskListFragment());
            } else if (itemId == R.id.bottomNavigationMenuAddTask) {
                loadFragment(new TaskAddFragment());
            } else if (itemId == R.id.bottomNavigationMenuListCheckTask) {
                loadFragment(new TaskListCheckFragment());
            } else if (itemId == R.id.bottomNavigationMenuListTaskRealtime) {
                loadFragment(new TaskListRealtimeFragment());
            } else if (itemId == R.id.bottomNavigationMenuListApiTask) {
                loadFragment(new TaskListApiFragment());
            }

            return true;
        });
    }

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

            } else {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutView, fragment);
        fragmentTransaction.commit();
    }


}