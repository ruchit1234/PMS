package com.kitelytech.pmsapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Fetchempproj {

    @SerializedName("Message")
    private ArrayList<Message> message = null;
    @SerializedName("Emp_Id")
    @Expose
    private Integer empId;

    public ArrayList<Message> getMessage() {
        return message;
    }

    public Integer getEmpId() {
        return empId;
    }
}