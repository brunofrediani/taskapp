package com.bruno.tasks.service.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bruno.tasks.R;
import com.bruno.tasks.service.constants.TaskConstants;
import com.bruno.tasks.service.listener.APIListener;
import com.bruno.tasks.service.model.TaskModel;
import com.bruno.tasks.service.repository.remote.RetrofitClient;
import com.bruno.tasks.service.repository.remote.TaskService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskRepository extends BaseRepository{

    private final TaskService mTaskService;
    public TaskRepository(Context context) {
        super(context);
    this.mTaskService = RetrofitClient.createService(TaskService.class);
    }

    public void save(TaskModel task, APIListener<Boolean> listener){
        Call<Boolean> call = this.mTaskService.create(
                task.getPriorityId(),
                task.getDescription(),
                task.getDueDate(),
                task.isComplete()
        );
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(handleFailure(response.errorBody()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable throwable) {
                listener.onFailure(mContext.getString(R.string.ERROR_UNEXPECTED));

            }
        });
    }
}
