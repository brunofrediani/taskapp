package com.bruno.tasks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.bruno.tasks.service.listener.APIListener;
import com.bruno.tasks.service.model.PersonModel;
import com.bruno.tasks.service.repository.PersonRepository;

public class LoginViewModel extends AndroidViewModel {

    private final PersonRepository mPersonRepository ;
    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.mPersonRepository = new PersonRepository(application);
    }

    public void login(String email, String password) {
        this.mPersonRepository.login(email, password, new APIListener<PersonModel>() {
            @Override
            public void onSuccess(PersonModel result) {

            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
