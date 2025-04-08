package com.bruno.tasks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.bruno.tasks.service.repository.PersonRepository;

public class RegisterViewModel extends AndroidViewModel {

    private PersonRepository mPersonRepository = new PersonRepository();
    public RegisterViewModel(@NonNull Application application) {
        super(application);
    }

    public void create(String name, String email, String password) {
        mPersonRepository.create(name,email,password);
    }
}
