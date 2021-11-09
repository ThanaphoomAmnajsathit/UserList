package com.example.usersmanageremaster.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReturnUsers {
    @Expose
    @SerializedName("code")
    public String code;
    @Expose
    @SerializedName("massage")
    public String massage;
    @Expose
    @SerializedName("users")
    public List<User> users;
}
