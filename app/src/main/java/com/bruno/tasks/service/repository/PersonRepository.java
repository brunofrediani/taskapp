package com.bruno.tasks.service.repository;

import android.content.Context;
import android.util.Log;

import com.bruno.tasks.R;
import com.bruno.tasks.service.constants.TaskConstants;
import com.bruno.tasks.service.listener.APIListener;
import com.bruno.tasks.service.model.PersonModel;
import com.bruno.tasks.service.repository.local.SecurityPreferences;
import com.bruno.tasks.service.repository.remote.PersonService;
import com.bruno.tasks.service.repository.remote.RetrofitClient;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonRepository {

    private PersonService mPersonService;
    private Context mContext;
    private SecurityPreferences mSecurityPreferences;

    public PersonRepository(Context context){
        this.mPersonService = RetrofitClient.createService(PersonService.class);
        this.mContext = context;
        this.mSecurityPreferences = new SecurityPreferences(context);
    }

    public void create(String name, String email, String password) {
        Call<PersonModel> call = this.mPersonService.create(name,email,password,true);
        call.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {
                PersonModel person = response.body();
                int code = response.code();
                String s ="";
                Log.d("retornoAPI", String.valueOf(code));
            }

            @Override
            public void onFailure(Call<PersonModel> call, Throwable throwable) {

            }
        });
    }
    public void login(String email, String password, APIListener<PersonModel> listener){
        Call<PersonModel> call = this.mPersonService.login(email, password);
        call.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {
                if (response.code() == TaskConstants.HTTP.SUCCESS){
                    listener.onSuccess(response.body());
                } else {
                    try {
                        String json = response.errorBody().string();
                        String str = new Gson().fromJson(json, String.class);
                        listener.onFailure(str);
                    } catch (IOException e) {
                        e.printStackTrace();                    }
                }
            }

            @Override
            public void onFailure(Call<PersonModel> call, Throwable throwable) {
                listener.onFailure(mContext.getString(R.string.ERROR_UNEXPECTED));
            }
        });
    }
    public void saveUserData(PersonModel person){
        this.mSecurityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, person.getToken());
        this.mSecurityPreferences.store(TaskConstants.SHARED.PERSON_KEY, person.getPersonKey());
        this.mSecurityPreferences.store(TaskConstants.SHARED.PERSON_NAME, person.getName());
    }
}
