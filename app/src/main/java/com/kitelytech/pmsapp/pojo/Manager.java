package com.kitelytech.pmsapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Manager {
    @SerializedName("Managers")
    @Expose
    private String managers;
    @SerializedName("Hours_Exceed_Flag")
    @Expose
    private Integer hoursExceedFlag;
    @SerializedName("Total_Hours")
    @Expose
    private String totalHours;

    public String getManagers() {
        return managers;
    }

    public void setManagers(String managers) {
        this.managers = managers;
    }

    public Integer getHoursExceedFlag() {
        return hoursExceedFlag;
    }

    public void setHoursExceedFlag(Integer hoursExceedFlag) {
        this.hoursExceedFlag = hoursExceedFlag;
    }

    public String getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(String totalHours) {
        this.totalHours = totalHours;
    }
}
