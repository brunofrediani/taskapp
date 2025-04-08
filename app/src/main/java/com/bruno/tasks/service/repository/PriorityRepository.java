package com.bruno.tasks.service.repository;

import com.bruno.tasks.service.listener.APIListener;
import com.bruno.tasks.service.model.PriorityModel;
import com.bruno.tasks.service.repository.remote.PriorityService;
import com.bruno.tasks.service.repository.remote.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriorityRepository {

    private PriorityService mPriorityService;

    public PriorityRepository() {
        this.mPriorityService = RetrofitClient.createService(PriorityService.class);
    }

    public void all(APIListener<List<PriorityModel>> listener){
        Call<List<PriorityModel>> call = this.mPriorityService.all();
        call.enqueue(new Callback<List<PriorityModel>>() {
            @Override
            public void onResponse(Call<List<PriorityModel>> call, Response<List<PriorityModel>> response) {
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<PriorityModel>> call, Throwable throwable) {
                //TODO
            }
        });
    }
}
