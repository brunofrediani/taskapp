package com.bruno.tasks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bruno.tasks.service.listener.APIListener;
import com.bruno.tasks.service.listener.Feedback;
import com.bruno.tasks.service.model.PriorityModel;
import com.bruno.tasks.service.model.TaskModel;
import com.bruno.tasks.service.repository.PriorityRepository;
import com.bruno.tasks.service.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private final PriorityRepository mPriorityRepository;
    private final TaskRepository mTaskRepository;

    private final MutableLiveData<List<PriorityModel>> mPriorityList = new MutableLiveData<>();
    public LiveData<List<PriorityModel>> priorityList = this.mPriorityList;

    private final MutableLiveData<Feedback> mTaskSave = new MutableLiveData<>();
    public LiveData<Feedback> taskSave = this.mTaskSave;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        this.mPriorityRepository = new PriorityRepository(application);
        this.mTaskRepository = new TaskRepository(application);
    }
    public void getPriorityList(){
        List<PriorityModel> list = this.mPriorityRepository.getPriorityList();
        this.mPriorityList.setValue(list);

    }
    public void save(TaskModel task){
        this.mTaskRepository.save(task, new APIListener<Boolean>() {

            @Override
            public void onSuccess(Boolean result) {
                mTaskSave.setValue(new Feedback());
            }

            @Override
            public void onFailure(String message) {
                mTaskSave.setValue(new Feedback(message));

            }
        });
    }
}
