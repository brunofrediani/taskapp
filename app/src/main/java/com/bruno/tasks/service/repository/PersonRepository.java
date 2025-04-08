package com.bruno.tasks.service.repository;

import android.util.Log;

import com.bruno.tasks.service.model.PersonModel;
import com.bruno.tasks.service.repository.remote.PersonService;
import com.bruno.tasks.service.repository.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonRepository {

    private PersonService mPersonService;

    public PersonRepository(){
        this.mPersonService = RetrofitClient.createService(PersonService.class);
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
                 String s ="";

             }
         });
    }
}
