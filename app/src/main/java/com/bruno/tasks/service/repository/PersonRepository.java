package com.bruno.tasks.service.repository;

import android.content.Context;
import android.util.Log;

import com.bruno.tasks.R;
import com.bruno.tasks.service.constants.TaskConstants;
import com.bruno.tasks.service.listener.APIListener;
import com.bruno.tasks.service.model.PersonModel;
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

    public PersonRepository(Context context){
        this.mPersonService = RetrofitClient.createService(PersonService.class);
        this.mContext = context;
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
}
