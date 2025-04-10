package com.bruno.tasks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bruno.tasks.service.model.PriorityModel;
import com.bruno.tasks.service.repository.PriorityRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private final PriorityRepository mPriorityRepository;

    private MutableLiveData<List<PriorityModel>> mPriorityList = new MutableLiveData<>();
    public LiveData<List<PriorityModel>> priorityList = this.mPriorityList;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        this.mPriorityRepository = new PriorityRepository(application);
    }
    public void getPriorityList(){
        List<PriorityModel> list = this.mPriorityRepository.getPriorityList();
        this.mPriorityList.setValue(list);

    }
}
