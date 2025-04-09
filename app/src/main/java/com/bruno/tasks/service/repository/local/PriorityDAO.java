package com.bruno.tasks.service.repository.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.bruno.tasks.service.model.PriorityModel;

import java.util.List;

@Dao
public interface PriorityDAO {

    @Insert
    void save(List<PriorityModel> list);

    @Query("SELECT * FROM Priority")
    List<PriorityModel> list();

    @Query("SELECT description FROM Priority WHERE id = :id")
    String getDescription(int id);

    @Query("DELETE FROM Priority")
    void clear();
}
