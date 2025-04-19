package com.bruno.tasks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.bruno.tasks.service.repository.PersonRepository;

public class MainViewModel extends AndroidViewModel {

    private final PersonRepository mPersonRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.mPersonRepository = new PersonRepository(application);
    }

    public void logout() {
        this.mPersonRepository.clearUserData();
    }
}
