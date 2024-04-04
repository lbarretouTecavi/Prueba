package com.example.pruebafastadapter.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pruebafastadapter.R;
import com.example.pruebafastadapter.data.db.FirebaseHelper;
import com.example.pruebafastadapter.data.models.Task;
import com.example.pruebafastadapter.presenters.ITaskPresenter;
import com.example.pruebafastadapter.presenters.TaskPresenter;
import com.example.pruebafastadapter.utils.FastItem;
import com.example.pruebafastadapter.utils.IFastItem;
import com.example.pruebafastadapter.views.ITaskView;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.List;

public class TaskListRealtimeFragment extends Fragment implements ITaskView, IFastItem {
    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private List<Task> list;
    private RecyclerView recyclerView;
    private FastItemAdapter<FastItem> fastItemAdapter;
    private ITaskPresenter presenter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task_list_realtime, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewListTaskRealtime);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fastItemAdapter  = new FastItemAdapter<>();
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(fastItemAdapter);
        presenter = new TaskPresenter(getContext(), this);
        presenter.listTaskFromFirebase();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClickTask(Task task) {

    }

    @Override
    public void viewTasks(List<Task> tasks) {
        if (!tasks.isEmpty()){
//            txtContenteList.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            fastItemAdapter.clear();
            for (Task task : tasks) {
                fastItemAdapter.add(new FastItem(this, task, R.layout.task_item));
            }
            fastItemAdapter.notifyDataSetChanged();
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
        fastItemAdapter.clear();
        presenter.listTaskFromFirebase();
    }
}