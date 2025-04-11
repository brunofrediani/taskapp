package com.bruno.tasks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bruno.tasks.service.listener.APIListener;
import com.bruno.tasks.service.listener.Feedback;
import com.bruno.tasks.service.model.TaskModel;
import com.bruno.tasks.service.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class TaskListViewModel extends AndroidViewModel {
    private TaskRepository mTaskRepository;

    private MutableLiveData<List<TaskModel>> mTaskList = new MutableLiveData<>();
    public LiveData<List<TaskModel>> taskList = this.mTaskList;

    private MutableLiveData<Feedback> mFeedback = new MutableLiveData<>();
    public LiveData<Feedback> feedback = this.mFeedback;

    public TaskListViewModel(@NonNull Application application) {
        super(application);
        this.mTaskRepository = new TaskRepository(application);
    }

    public void list(){
        this.mTaskRepository.allTasks(new APIListener<List<TaskModel>>() {
            @Override
            public void onSuccess(List<TaskModel> result) {
                mTaskList.setValue(result);
            }

            @Override
            public void onFailure(String message) {
                mTaskList.setValue(new ArrayList<>());
                mFeedback.setValue(new Feedback(message));
            }
        });
    }

}
