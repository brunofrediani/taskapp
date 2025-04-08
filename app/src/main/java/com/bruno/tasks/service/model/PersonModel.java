package com.bruno.tasks.service.model;

import com.google.gson.annotations.SerializedName;

public class PersonModel {

    @SerializedName("token")
    private String token;

    @SerializedName("personKey")
    private String personKey;

    @SerializedName("name")
    private String name;

    public String getToken() {
        return token;
    }
    public String getPersonKey() {
        return personKey;
    }

    public String getName() {
        return name;
    }
}
