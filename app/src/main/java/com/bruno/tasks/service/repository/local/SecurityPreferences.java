package com.bruno.tasks.service.repository.local;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences
 */
public class SecurityPreferences {

    private SharedPreferences mSharedPreferences;

    public SecurityPreferences(Context context) {
        this.mSharedPreferences = context.getSharedPreferences("TasksShared", Context.MODE_PRIVATE);
    }

    public void store(String key, String value) {
        this.mSharedPreferences.edit().putString(key, value).apply();
    }

    public String getStore(String key){
        return this.mSharedPreferences.getString(key, "");
    }
    public void remove(String key){
        this.mSharedPreferences.edit().remove(key).apply();
    }
}
