package com.kitelytech.pmsapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {
    public Integer getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    @SerializedName("Project_Id")
    @Expose
    private Integer projectId;
    @SerializedName("Project_Name")
    @Expose
    private String projectName;
}
