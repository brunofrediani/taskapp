package com.bruno.tasks.service.repository.local;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public abstract class TaskDatabase extends RoomDatabase{

    // Singleton
    private static TaskDatabase INSTANCE;

    // Singleton
    public static TaskDatabase getDataBase(Context context) {
        synchronized (TaskDatabase.class) {
            INSTANCE = Room.databaseBuilder(context, TaskDatabase.class, "taskDB")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
