package com.bruno.tasks.service.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bruno.tasks.R;
import com.bruno.tasks.service.constants.TaskConstants;
import com.bruno.tasks.service.listener.APIListener;
import com.bruno.tasks.service.model.PersonModel;
import com.bruno.tasks.service.repository.local.SecurityPreferences;
import com.bruno.tasks.service.repository.remote.PersonService;
import com.bruno.tasks.service.repository.remote.RetrofitClient;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonRepository extends BaseRepository{

    private final PersonService mPersonService;
    private final SecurityPreferences mSecurityPreferences;

    public PersonRepository(Context context){
        super(context);
        this.mPersonService = RetrofitClient.createService(PersonService.class);
        this.mContext = context;
        this.mSecurityPreferences = new SecurityPreferences(context);
    }

    public void create(String name, String email, String password,APIListener<PersonModel> listener) {
        Call<PersonModel> call = this.mPersonService.create(name,email,password,true);
        call.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(@NonNull Call<PersonModel> call, @NonNull Response<PersonModel> response) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    listener.onSuccess(response.body());

                } else{
                    listener.onFailure(handleFailure(response.errorBody()));

                }
            }

            @Override
            public void onFailure(@NonNull Call<PersonModel> call,@NonNull Throwable throwable) {
                listener.onFailure(mContext.getString(R.string.ERROR_UNEXPECTED));

            }
        });
    }
    public void login(String email, String password, APIListener<PersonModel> listener){
        Call<PersonModel> call = this.mPersonService.login(email, password);
        call.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(@NonNull Call<PersonModel> call,@NonNull Response<PersonModel> response) {
                if (response.code() == TaskConstants.HTTP.SUCCESS){
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(handleFailure(response.errorBody()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PersonModel> call,@NonNull Throwable throwable) {
                listener.onFailure(mContext.getString(R.string.ERROR_UNEXPECTED));
            }
        });
    }
    public void saveUserData(PersonModel person){
        this.mSecurityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, person.getToken());
        this.mSecurityPreferences.store(TaskConstants.SHARED.PERSON_KEY, person.getPersonKey());
        this.mSecurityPreferences.store(TaskConstants.SHARED.PERSON_NAME, person.getName());

        RetrofitClient.saveHeaders(person.getToken(), person.getPersonKey());
    }
    public PersonModel getUserData(){
        PersonModel person = new PersonModel();
        person.setToken(this.mSecurityPreferences.getStore(TaskConstants.SHARED.TOKEN_KEY));
        person.setPersonKey(this.mSecurityPreferences.getStore(TaskConstants.SHARED.PERSON_KEY));
        person.setName(this.mSecurityPreferences.getStore(TaskConstants.SHARED.PERSON_NAME));
        return person;
    }
}
