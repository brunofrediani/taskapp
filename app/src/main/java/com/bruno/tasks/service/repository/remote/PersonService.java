package com.bruno.tasks.service.repository.remote;

import com.bruno.tasks.service.model.PersonModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PersonService  {

    @FormUrlEncoded
    @POST("Authentication/Create")
    Call<PersonModel>  create(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("receiveNews") Boolean receiveNews);

    @FormUrlEncoded
    @POST("Authentication/Login")
    Call<PersonModel>  login(@Field("email") String email,@Field("password") String password);
}
