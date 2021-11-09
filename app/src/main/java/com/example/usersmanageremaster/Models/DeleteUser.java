package com.example.usersmanageremaster.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteUser {

    @Expose
    @SerializedName("code")
    public String Code;
    @Expose
    @SerializedName("message")
    public String Message;
    @Expose
    @SerializedName("user_id")
    public String user_id;
    @Expose
    @SerializedName("image_name")
    public String image_name;

    public DeleteUser(String user_id ,String image_name) {
        setUser_id(user_id);
        setImage_name(image_name);
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
