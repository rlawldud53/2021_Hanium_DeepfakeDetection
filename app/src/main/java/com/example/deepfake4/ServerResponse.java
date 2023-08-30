package com.example.deepfake4;

import com.google.gson.annotations.SerializedName;


public class ServerResponse {

    // variable name should be same as in the json response from php
    @SerializedName("success")
    boolean success;
    @SerializedName("probability")
    String probability;

    public String getMessage() {
        return probability;
    }

    public boolean getSuccess() {
        return success;
    }

}
