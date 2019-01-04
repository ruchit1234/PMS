package com.kitelytech.pmsapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("Emp_Id")
    private int id;

    @SerializedName("Emp_First_Name")
    private String fname;

    @SerializedName("Emp_Last_Name")
    private String lname;

    @SerializedName("Emp_Email")
    private String empemail;

    @SerializedName("Emp_Address")
    private String empaddress;

    @SerializedName("Joining_Date")
    private String empjoiningdata;

    @SerializedName("user")
    private User user;
    @SerializedName("Createdby")
    @Expose
    private String createdby;
    @SerializedName("User_Img")
    @Expose
    private String userImg;
    @SerializedName("sms_notify")
    @Expose
    private Integer smsNotify;

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public Integer getSmsNotify() {
        return smsNotify;
    }

    public void setSmsNotify(Integer smsNotify) {
        this.smsNotify = smsNotify;
    }


    public Result(String message, User user) {

        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
