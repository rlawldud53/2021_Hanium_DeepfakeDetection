package com.example.deepfake4;

import android.graphics.drawable.Drawable;

public class VideoDTO {
    String name;
    String phoneNum;
    double probability;
    String result;
    Drawable resId;
    String phone;

    public VideoDTO() {}

    public VideoDTO(String name, String phoneNum, double probability, String result, Drawable resId, String phone) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.probability = probability;
        this.result = result;
        this.resId = resId;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAge() { return result; }

    public void setAge(String result) {
        this.result = result;
    }

    public double getProb() {return probability;}

    public void setProb(double prob) {this.probability = prob;}

    public Drawable getResId() {
        return resId;
    }

    public void setResId(Drawable resId) {
        this.resId = resId;
    }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }
}
