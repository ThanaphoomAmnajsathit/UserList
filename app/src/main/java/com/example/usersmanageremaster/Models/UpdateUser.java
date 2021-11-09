package com.example.usersmanageremaster.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateUser {
    @Expose
    @SerializedName("code")
    public String Code;
    @Expose
    @SerializedName("message")
    public String Message;
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
    @SerializedName("user_new_image")
    public String user_image_name;
    @Expose
    @SerializedName("user_old_image")
    public String user_old_name;

    //----Constructor Update User With new Image---
    public UpdateUser(
            Integer user_id
            , String user_first_name
            , String user_last_name
            , String user_phone
            , String user_email
            , String user_gender
            , String user_image_name
            , String user_old_name) {
        this.user_id = user_id;
        this.user_first_name = user_first_name;
        this.user_last_name = user_last_name;
        this.user_phone = user_phone;
        this.user_email = user_email;
        this.user_gender = user_gender;
        this.user_image_name = user_image_name;
        this.user_old_name = user_old_name;
    }

    //----Constructor Update User WithOut Image---
    public UpdateUser(
            Integer user_id
            , String user_first_name
            , String user_last_name
            , String user_phone
            , String user_email
            , String user_gender) {
        this.user_id = user_id;
        this.user_first_name = user_first_name;
        this.user_last_name = user_last_name;
        this.user_phone = user_phone;
        this.user_email = user_email;
        this.user_gender = user_gender;
    }
}
