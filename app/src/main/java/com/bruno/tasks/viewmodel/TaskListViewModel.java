package com.bruno.tasks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bruno.tasks.service.constants.TaskConstants;
import com.bruno.tasks.service.listener.APIListener;
import com.bruno.tasks.service.listener.Feedback;
import com.bruno.tasks.service.model.TaskModel;
import com.bruno.tasks.service.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class TaskListViewModel extends AndroidViewModel {
    private final TaskRepository mTaskRepository;

    private final MutableLiveData<List<TaskModel>> mTaskList = new MutableLiveData<>();
    public LiveData<List<TaskModel>> taskList = this.mTaskList;

    private final MutableLiveData<Feedback> mFeedback = new MutableLiveData<>();
    public LiveData<Feedback> feedback = this.mFeedback;

    public TaskListViewModel(@NonNull Application application) {
        super(application);
        this.mTaskRepository = new TaskRepository(application);
    }

    public void list(int filter){
        APIListener<List<TaskModel>> listener = new APIListener<List<TaskModel>>() {
            @Override
            public void onSuccess(List<TaskModel> result) {
                mTaskList.setValue(result);
            }

            @Override
            public void onFailure(String message) {
                mTaskList.setValue(new ArrayList<>());
                mFeedback.setValue(new Feedback(message));
            }
        };

        if (filter == TaskConstants.TASKFILTER.NO_FILTER){
            this.mTaskRepository.allTasks(listener);
        } else if (filter == TaskConstants.TASKFILTER.NEXT_7_DAYS) {
            this.mTaskRepository.nextWeekTasks(listener);
        } else {
            this.mTaskRepository.overdueTasks(listener);
        }
    }

}
