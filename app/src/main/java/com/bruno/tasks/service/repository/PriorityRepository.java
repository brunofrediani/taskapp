package com.bruno.tasks.service.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bruno.tasks.R;
import com.bruno.tasks.service.constants.TaskConstants;
import com.bruno.tasks.service.listener.APIListener;
import com.bruno.tasks.service.model.PriorityModel;
import com.bruno.tasks.service.repository.local.PriorityDAO;
import com.bruno.tasks.service.repository.local.TaskDatabase;
import com.bruno.tasks.service.repository.remote.PriorityService;
import com.bruno.tasks.service.repository.remote.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriorityRepository extends BaseRepository{

    private final PriorityService mPriorityService;
    private final PriorityDAO mPriorityDAO;

    public PriorityRepository(Context context) {
        super(context);
        this.mContext = context;
        this.mPriorityService = RetrofitClient.createService(PriorityService.class);
        this.mPriorityDAO = TaskDatabase.getDataBase(context).priorityDAO();
    }

    public void all(APIListener<List<PriorityModel>> listener){
        Call<List<PriorityModel>> call = this.mPriorityService.all();
        call.enqueue(new Callback<List<PriorityModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<PriorityModel>> call, @NonNull Response<List<PriorityModel>> response) {
                if (response.code() == TaskConstants.HTTP.SUCCESS){
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(handleFailure(response.errorBody()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PriorityModel>> call,@NonNull Throwable throwable) {
                listener.onFailure(mContext.getString(R.string.ERROR_UNEXPECTED));
            }
        });
    }
    public List<PriorityModel> getPriorityList(){
        return this.mPriorityDAO.list();
    }

    public String getDescription(int id){
        return this.mPriorityDAO.getDescription(id);
    }

    public void save(List<PriorityModel> list) {
        this.mPriorityDAO.clear();
        this.mPriorityDAO.save(list);
    }
}
