package com.example.usersmanageremaster.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @Expose
    @SerializedName("user_id")
    public Integer user_id;
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
    public String user_image;

    public Integer getUser_id() {
        return user_id;
    }

    public String getUser_first_name() {
        return user_first_name;
    }

    public String getUser_last_name() {
        return user_last_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getUser_gender() {
        return user_gender;
    }

    @Override
    public String toString() {
        return   user_first_name + " " + user_last_name ;
    }
}
