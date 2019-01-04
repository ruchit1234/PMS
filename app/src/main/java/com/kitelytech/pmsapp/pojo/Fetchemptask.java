package com.kitelytech.pmsapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Fetchemptask {
    @SerializedName("Message")
    @Expose
    private ArrayList<Taskmsg> message = null;

    public ArrayList<Taskmsg> getRate_Data() {
        return Rate_Data;
    }

    public ArrayList<Project> getProject() {
        return project;
    }

    @SerializedName("Project")
    @Expose
    private ArrayList<Project> project = null;
    @SerializedName("Rate_Data")
    @Expose
    private ArrayList<Taskmsg> Rate_Data = null;
    @SerializedName("Managers")
    @Expose
    private ArrayList<Manager> managers = null;

    public ArrayList<Taskmsg> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<Taskmsg> message) {
        this.message = message;
    }

    public ArrayList<Manager> getManagers() {
        return managers;
    }

    public void setManagers(ArrayList<Manager> managers) {
        this.managers = managers;
    }

}
