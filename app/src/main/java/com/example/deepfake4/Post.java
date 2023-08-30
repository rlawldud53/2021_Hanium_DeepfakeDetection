package com.example.deepfake4;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("isSuccess")
    private boolean isSuccess;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public boolean getisSuccess(){
        return isSuccess;
    }
    public void setisSuccess(boolean isSuccess){
        this.isSuccess=isSuccess;
    }
    public int getCode(){
        return code;
    }
    public void setCode(int code){
        this.code=code;
    }
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message=message;
    }
}
