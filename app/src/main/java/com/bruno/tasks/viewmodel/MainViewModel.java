package com.bruno.tasks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bruno.tasks.service.model.PersonModel;
import com.bruno.tasks.service.repository.PersonRepository;

public class MainViewModel extends AndroidViewModel {

    private final PersonRepository mPersonRepository;

    private final MutableLiveData<PersonModel> mUserData = new MutableLiveData<>();
    public LiveData<PersonModel> userData = this.mUserData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.mPersonRepository = new PersonRepository(application);
    }

    public void logout() {
        this.mPersonRepository.clearUserData();
    }

    public void loadUserData() {
        this.mUserData.setValue(this.mPersonRepository.getUserData());
    }
}
