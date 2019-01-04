package com.kitelytech.pmsapp.pojo;

import com.google.gson.annotations.SerializedName;

public class MessageItem{


    @SerializedName("Emp_Email")
    private String empEmail;

    public String getTBD_Id() {
        return TBD_Id;
    }

    @SerializedName("TBD_Id")
    private String TBD_Id;

    @SerializedName("Emp_First_Name")
    private String empFirstName;

    @SerializedName("Emp_Address")
    private String empAddress;

    @SerializedName("Joining_Date")
    private String joiningDate;

    @SerializedName("Is_Resign")
    private int isResign;

    public int getEmpid() {
        return empid;
    }

    @SerializedName("Emp_Id")
    private int empid;

    @SerializedName("Emp_Last_Name")
    private String empLastName;

    @SerializedName("Emp_Photo")
    private String empPhoto;

    @SerializedName("Resign_Date")
    private String resignDate;

    @SerializedName("Emp_Phone_No")
    private String empPhoneNo;

    @SerializedName("Emp_Password")
    private String empPassword;

    @SerializedName("sms_status")
    private int smsStatus;

    public void setEmpEmail(String empEmail){
        this.empEmail = empEmail;
    }

    public String getEmpEmail(){
        return empEmail;
    }

    public void setEmpFirstName(String empFirstName){
        this.empFirstName = empFirstName;
    }

    public String getEmpFirstName(){
        return empFirstName;
    }

    public void setEmpAddress(String empAddress){
        this.empAddress = empAddress;
    }

    public String getEmpAddress(){
        return empAddress;
    }

    public void setJoiningDate(String joiningDate){
        this.joiningDate = joiningDate;
    }

    public String getJoiningDate(){
        return joiningDate;
    }

    public void setIsResign(int isResign){
        this.isResign = isResign;
    }

    public int getIsResign(){
        return isResign;
    }

    public String getEmpLastName() {
        return empLastName;
    }

    public void setEmpPhoto(String empPhoto){
        this.empPhoto = empPhoto;
    }

    public String getEmpPhoto(){
        return empPhoto;
    }

    public void setResignDate(String resignDate){
        this.resignDate = resignDate;
    }

    public String getResignDate(){
        return resignDate;
    }

    public void setEmpPhoneNo(String empPhoneNo){
        this.empPhoneNo = empPhoneNo;
    }

    public String getEmpPhoneNo(){
        return empPhoneNo;
    }

    public void setEmpPassword(String empPassword){
        this.empPassword = empPassword;
    }

    public String getEmpPassword(){
        return empPassword;
    }

    public void setSmsStatus(int smsStatus){
        this.smsStatus = smsStatus;
    }

    public int getSmsStatus(){
        return smsStatus;
    }
}