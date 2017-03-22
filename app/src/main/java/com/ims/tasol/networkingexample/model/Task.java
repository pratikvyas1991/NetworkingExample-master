
package com.ims.tasol.networkingexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Task {

    @SerializedName("task")
    @Expose
    private String task;
    @SerializedName("taskData")
    @Expose
    private TaskData taskData;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public TaskData getTaskData() {
        return taskData;
    }

    public void setTaskData(TaskData taskData) {
        this.taskData = taskData;
    }

}
