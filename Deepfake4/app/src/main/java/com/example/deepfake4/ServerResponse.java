package com.example.deepfake4;

import com.google.gson.annotations.SerializedName;


public class ServerResponse {

    // variable name should be same as in the json response from php
    @SerializedName("success")
    boolean success;
    @SerializedName("probability")
    String probability;
    @SerializedName("video_path")
    String video_path;

    public String getMessage() {
        return probability;
    }

    public String getVideo_path(){
        return video_path;
    }

    public boolean getSuccess() {
        return success;
    }

}
