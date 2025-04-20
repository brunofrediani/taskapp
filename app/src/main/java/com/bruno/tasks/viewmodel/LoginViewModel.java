package com.bruno.tasks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bruno.tasks.service.listener.APIListener;
import com.bruno.tasks.service.listener.Feedback;
import com.bruno.tasks.service.model.PersonModel;
import com.bruno.tasks.service.model.PriorityModel;
import com.bruno.tasks.service.repository.PersonRepository;
import com.bruno.tasks.service.repository.PriorityRepository;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {

    private final PersonRepository mPersonRepository ;
    private final PriorityRepository mPriorityRepository ;

    private final MutableLiveData<Feedback> mLogin = new MutableLiveData<>();
    public LiveData<Feedback> login = this.mLogin;

    private final MutableLiveData<Boolean> mFringerprint = new MutableLiveData<>();
    public LiveData<Boolean> fingerprint = this.mFringerprint;


    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.mPersonRepository = new PersonRepository(application);
        this.mPriorityRepository = new PriorityRepository(application);
    }

    public void login(String email, String password) {
        this.mPersonRepository.login(email, password, new APIListener<PersonModel>() {
            @Override
            public void onSuccess(PersonModel result) {
                result.setEmail(email);
                mPersonRepository.saveUserData(result);
                mLogin.setValue(new Feedback());
            }

            @Override
            public void onFailure(String message) {
                mLogin.setValue(new Feedback(message));
            }
        });
    }

    public void isFingerprintAvailable(){
        PersonModel person = this.mPersonRepository.getUserData();
        boolean everLogged = !"".equals(person.getName());

        this.mPersonRepository.saveUserData(person);

        if (!everLogged){
            this.mPriorityRepository.all(new APIListener<List<PriorityModel>>() {
                @Override
                public void onSuccess(List<PriorityModel> result) {
                    mPriorityRepository.save(result);
                }

                @Override
                public void onFailure(String message) {

                }
            });
        }

        this.mFringerprint.setValue(everLogged);
    }

}
