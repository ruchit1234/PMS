package com.kitelytech.pmsapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Project {
    @SerializedName("Project_Name")
    @Expose
    private String projectName;
    @SerializedName("Project_Id")
    @Expose
    private Integer projectId;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

}
