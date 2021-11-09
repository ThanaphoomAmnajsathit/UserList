package com.example.usersmanageremaster.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsertUser {

    @Expose
    @SerializedName("code")
    public String Code;
    @Expose
    @SerializedName("message")
    public String Message;
    @Expose
    @SerializedName("user_first_name")
    public String user_first_name;
    @Expose
    @SerializedName("user_last_name")
    public String user_last_name;
    @Expose
    @SerializedName("user_phone")
    public String user_phone;
    @Expose
    @SerializedName("user_email")
    public String user_email;
    @Expose
    @SerializedName("user_gender")
    public String user_gender;
    @Expose
    @SerializedName("user_image")
    public String user_image_name;

    public InsertUser(
              String user_first_name
            , String user_last_name
            , String user_phone
            , String user_email
            , String user_gender
            , String user_image_name) {
        this.user_first_name = user_first_name;
        this.user_last_name = user_last_name;
        this.user_phone = user_phone;
        this.user_email = user_email;
        this.user_gender = user_gender;
        this.user_image_name = user_image_name;
    }

    public String getUser_first_name() {
        return user_first_name;
    }

    public void setUser_first_name(String user_first_name) {
        this.user_first_name = user_first_name;
    }

    public String getUser_last_name() {
        return user_last_name;
    }

    public void setUser_last_name(String user_last_name) {
        this.user_last_name = user_last_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }
}
