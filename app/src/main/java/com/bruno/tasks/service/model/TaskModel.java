package com.bruno.tasks.service.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TaskModel {

    @SerializedName("Id")
    private int id;
    @SerializedName("PriorityId")
    private int priorityId;
    @SerializedName("Description")
    private String description;
    @SerializedName("DueDate")
    private Date dueDate;
    @SerializedName("Complete")
    private boolean complete;
}
