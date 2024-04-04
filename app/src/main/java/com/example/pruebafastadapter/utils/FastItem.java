package com.example.pruebafastadapter.utils;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pruebafastadapter.R;
import com.example.pruebafastadapter.data.models.Task;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class FastItem extends AbstractItem<FastItem, FastItem.ViewHolder> {
    private final IFastItem mTaskAdapter;
    private Task task;
    private int viewType;

    public FastItem(IFastItem mTaskAdapter, Task task, int viewType) {
        this.mTaskAdapter = mTaskAdapter;
        this.task = task;
        this.viewType = viewType;
    }

    @Override
    public int getType() {
//        return R.id.fastadapter_sampleitem_id;
        return viewType;
    }

    @Override
    public int getLayoutRes() {
        if (viewType == R.layout.task_item) {
            return R.layout.task_item;
        } else {
            return R.layout.task_sincronized_item;
        }
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        holder.bindView(this, payloads);
        if (viewType == R.layout.task_item) {
            holder.linearLayout.setOnClickListener(v -> mTaskAdapter.onClickTask(task));
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.unbindView(this);
    }


    public static class ViewHolder extends FastAdapter.ViewHolder<FastItem> {
        private TextView txtContent, txtUpdated, txtIdSincronized,
                txtContentTaskSincronized, txtUpdatedTaskSincronized;
        private CheckBox cbxIsCheck;
        private LinearLayout linearLayout;
        private ImageView imgSincronized, imgNotSincronized, imgNotSincronizedTaskSincronized, imgCheckTask;

        public ViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txtContentTask);
            txtUpdated = itemView.findViewById(R.id.txtUpdatedTask);
            txtIdSincronized = itemView.findViewById(R.id.txtIdSincronizedTask);
            cbxIsCheck = itemView.findViewById(R.id.cbxIsCheckTask);
            linearLayout = itemView.findViewById(R.id.layoutContentItem);
            imgSincronized = itemView.findViewById(R.id.imgSincronizedTask);
            imgNotSincronized = itemView.findViewById(R.id.imgNotSincronizedTask);
            txtContentTaskSincronized = itemView.findViewById(R.id.txtContentTaskSincronized);
            txtUpdatedTaskSincronized = itemView.findViewById(R.id.txtUpdatedTaskSincronized);
            imgCheckTask = itemView.findViewById(R.id.imgCheckTask);
            imgNotSincronizedTaskSincronized = itemView.findViewById(R.id.imgNotSincronizedTaskSincronized);
        }

        @Override
        public void bindView(FastItem item, List<Object> payloads) {
            if (item.viewType == R.layout.task_item) {
                txtContent.setText(item.task.getContent());
                txtUpdated.setText("Actualizado: ".concat(String.valueOf(item.task.getUpdated())));
                txtIdSincronized.setText("ID: ".concat(item.task.getIdUnique()));
                cbxIsCheck.setChecked(item.task.getIsCheck());
                if(item.task.getIsSincronized() == true){
                    imgSincronized.setVisibility(View.VISIBLE);
                    imgNotSincronized.setVisibility(View.INVISIBLE);
                }else {
                    imgSincronized.setVisibility(View.INVISIBLE);
                    imgNotSincronized.setVisibility(View.VISIBLE);
                }
            } else {
                txtContentTaskSincronized.setText(item.task.getContent());
                txtUpdatedTaskSincronized.setText("Actualizado: ".concat(String.valueOf(item.task.getUpdated())));
                imgNotSincronizedTaskSincronized.setVisibility(View.VISIBLE);
                if(item.task.getIsCheck() == true){
                    imgCheckTask.setVisibility(View.VISIBLE);
                }else {
                    imgCheckTask.setVisibility(View.INVISIBLE);
                }
            }

        }

        @Override
        public void unbindView(FastItem item) {
            if (item.viewType == R.layout.task_item) {
                txtContent.setText("");
                txtUpdated.setText("");
                txtIdSincronized.setText("");
                cbxIsCheck.setChecked(false);
                imgSincronized.setVisibility(View.INVISIBLE);
            } else {
                txtContentTaskSincronized.setText("");
                txtUpdatedTaskSincronized.setText("");
                imgCheckTask.setVisibility(View.INVISIBLE);
                imgNotSincronizedTaskSincronized.setVisibility(View.INVISIBLE);
            }
        }
    }
}
