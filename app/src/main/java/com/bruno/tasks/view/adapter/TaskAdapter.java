package com.bruno.tasks.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bruno.tasks.R;
import com.bruno.tasks.service.listener.TaskListener;
import com.bruno.tasks.service.model.TaskModel;
import com.bruno.tasks.view.viewholder.TaskViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private List<TaskModel> mList = new ArrayList<>();
    private TaskListener mListener;

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_list, parent, false);
        return new TaskViewHolder(item, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
         TaskModel taskModel = this.mList.get(position);
         holder.bindData(taskModel);
    }

    @Override
    public int getItemCount() {
        return this.mList.size();
    }

    /**
     * Atribui o tratamento de eventos
     */
    public void attachListener(TaskListener listener) {
        this.mListener = listener;
    }

    /**
     * Atualiza listagem
     */
    public void updateList(List<TaskModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }
}
