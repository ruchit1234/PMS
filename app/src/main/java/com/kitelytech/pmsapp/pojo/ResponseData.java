package com.kitelytech.pmsapp.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseData {

    @SerializedName("Message")
    private List<MessageItem> message;

    public void setMessage(List<MessageItem> message){
        this.message = message;
    }

    public List<MessageItem> getMessage(){
        return message;
    }
}