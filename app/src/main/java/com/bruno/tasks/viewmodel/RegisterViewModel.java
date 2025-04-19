package com.bruno.tasks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bruno.tasks.service.listener.APIListener;
import com.bruno.tasks.service.listener.Feedback;
import com.bruno.tasks.service.model.PersonModel;
import com.bruno.tasks.service.repository.PersonRepository;

public class RegisterViewModel extends AndroidViewModel {


    private final MutableLiveData<Feedback> mCreate = new MutableLiveData<>();
    public LiveData<Feedback> create = this.mCreate;

    private final PersonRepository mPersonRepository ;
    public RegisterViewModel(@NonNull Application application) {
        super(application);
        this.mPersonRepository = new PersonRepository(application);
    }

    public void create(String name, String email, String password) {
        mPersonRepository.create(name,email,password, new APIListener<PersonModel>() {
            @Override
            public void onSuccess(PersonModel result) {
                result.setEmail(email);
                mPersonRepository.saveUserData(result);
                mCreate.setValue(new Feedback());
            }

            @Override
            public void onFailure(String message) {
                mCreate.setValue(new Feedback(message));
            }
        });
    }
}
