package com.bruno.tasks.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bruno.tasks.R;
import com.bruno.tasks.service.constants.TaskConstants;
import com.bruno.tasks.service.listener.Feedback;
import com.bruno.tasks.service.listener.TaskListener;
import com.bruno.tasks.service.model.TaskModel;
import com.bruno.tasks.view.adapter.TaskAdapter;
import com.bruno.tasks.viewmodel.TaskListViewModel;

import java.util.List;


public class TaskListFragment extends Fragment {

    private final TaskAdapter mAdapter = new TaskAdapter();
    private TaskListViewModel mViewModel;
    private TaskListener mListener;
    private int mTaskFilter = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_task_list, container, false);
        this.mViewModel = new ViewModelProvider(this).get(TaskListViewModel.class);

        // Lista de tarefas
        RecyclerView recycler = root.findViewById(R.id.recycler_task_list);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(this.mAdapter);

        this.mListener = new TaskListener() {

            @Override
            public void onListClick(int id) {
                Intent intent = new Intent(getContext(),TaskActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(TaskConstants.BUNDLE.TASKID,id);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int id) {

            }

            @Override
            public void onCompleteClick(int id) {

            }

            @Override
            public void onUndoClick(int id) {

            }
        };
        this.mAdapter.attachListener(this.mListener);

        // Cria os observadores
        this.loadObservers();

        //pegar o valor de argument que está definido no mobile navigation xml
        Bundle bundle = getArguments();
        if (bundle != null){
           this.mTaskFilter = bundle.getInt(TaskConstants.TASKFILTER.KEY);
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mViewModel.list(this.mTaskFilter);
    }

    private void loadObservers() {
        this.mViewModel.taskList.observe(getViewLifecycleOwner(), new Observer<List<TaskModel>>() {
            @Override
            public void onChanged(List<TaskModel> taskModels) {
            mAdapter.updateList(taskModels);
            }
        });

        this.mViewModel.feedback.observe(getViewLifecycleOwner(), new Observer<Feedback>() {
            @Override
            public void onChanged(Feedback feedback) {
                if (!feedback.isSuccess()){
                    toast(feedback.getMessage());
                }
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
