package com.example.usersmanageremaster.domain.usecase;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.usersmanageremaster.FileUtils;
import com.example.usersmanageremaster.Models.InsertUser;
import com.example.usersmanageremaster.domain.repositories.UserRepository;
import com.example.usersmanageremaster.presentations.presenters.InsertUserFragmentPresenter;
import com.github.dhaval2404.imagepicker.util.FileUtil;

import java.io.File;
import java.net.URI;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertUserUseCase {
    private final UserRepository userRepository;
    private final InsertUserFragmentPresenter listener;


    //---User Model---
    private String filePath ;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String gender;

    public InsertUserUseCase(
             UserRepository userRepository
            , InsertUserFragmentPresenter listener
            , String fileImgPath
            , String firstName
            , String lastName
            , String email
            , String phone
            , String gender
    ){

        this.userRepository = userRepository;
        this.listener = listener;
        setFilePath(fileImgPath);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPhone(phone);
        setGender(gender);

        try {
            uploadImage();
            Log.e("FilePath",filePath);
        }finally {
            startInsertUser();
        }
    }

    private void uploadImage(){

        File file = new File(filePath);

        RequestBody requestBody = RequestBody
                .create(MediaType.parse("multipart/from-data"),file);

        MultipartBody.Part part = MultipartBody.Part
                .createFormData("sendimage",file.getName(),requestBody);


        Call<ResponseBody> call = userRepository.uploadImage(part);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){
                    Log.e("Image Uploaded Successfully",response.message());
                }else {
                    Log.e("Image Upload error",response.message());
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("====",t.getMessage());
            }
        });

    }

    private void startInsertUser(){
        final String directoryServer = "http://10.0.2.2/users_api/img/";
        File file = new File(filePath);

        InsertUser insertUser = new InsertUser(
                firstName
                , lastName
                , phone
                , email
                , gender
                , directoryServer + file.getName());

        Call<InsertUser> call = userRepository.insertUser(insertUser);

        call.enqueue(new Callback<InsertUser>() {
            @Override
            public void onResponse(Call<InsertUser> call, Response<InsertUser> response) {
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().Code.equals("200")){
                        Log.d("Insert status","Successfully");
                        listener.onInsertSuccessfully();
                    }else {
                        Log.e("Insert status","not 200");
                        Log.e("Insert status",response.body().Message.toString());
                        listener.onError();
                    }
                }else {
                    Log.e("Insert status",response.message());
                    Log.e("Insert status",String.valueOf(response.code()));
                    listener.onError();
                }
            }

            @Override
            public void onFailure(Call<InsertUser> call, Throwable t) {
                Log.e("Insert onFailure",t.getMessage());
            }
        });
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
