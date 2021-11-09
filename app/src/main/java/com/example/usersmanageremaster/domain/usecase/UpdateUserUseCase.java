package com.example.usersmanageremaster.domain.usecase;

import android.util.Log;

import com.example.usersmanageremaster.Models.UpdateUser;
import com.example.usersmanageremaster.domain.repositories.UserRepository;
import com.example.usersmanageremaster.presentations.presenters.UpdateUserFragmentPresenter;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserUseCase {

    private UserRepository userRepository;
    private UpdateUserFragmentPresenter listener;

    //---User Model---
    private String user_id;
    private String filePath;
    private String oldFileName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String gender;

    //----Constructor Update User With new Image---
    public UpdateUserUseCase(
            UserRepository userRepository
            , UpdateUserFragmentPresenter listener
            , String user_id
            , String filePath
            , String oldFileName
            , String firstName
            , String lastName
            , String email
            , String phone
            , String gender
    ){
        this.userRepository = userRepository;
        this.listener = listener;
        setUser_id(user_id);
        setFilePath(filePath);
        setOldFileName(oldFileName);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPhone(phone);
        setGender(gender);


        try {
            uploadImage();
        }finally {
            startUpdateUserWithNewImage();
        }

        Log.e(">>>>>","WithNewImage");
    }

    //----Constructor Update User WithOut Image---
    public UpdateUserUseCase(
            UserRepository userRepository
            , UpdateUserFragmentPresenter listener
            , String user_id
            , String firstName
            , String lastName
            , String email
            , String phone
            , String gender
    ){
        this.userRepository = userRepository;
        this.listener = listener;
        setUser_id(user_id);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPhone(phone);
        setGender(gender);

        startUpdateUserWithOutImage();
        Log.e(">>>>>","WithOutImage");

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
                    listener.onInsertSuccessfully();
                }else {
                    Log.e("Image Upload error",response.message());
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("onFailure",t.getMessage());
            }
        });
    }

    private void startUpdateUserWithNewImage(){
        final String directoryServer = "http://10.0.2.2/users_api/img/";
        File file = new File(filePath);
        File oldFile = new File(oldFileName);

        UpdateUser updateUser = new UpdateUser(
                Integer.parseInt(user_id)
                ,firstName
                ,lastName
                ,phone
                ,email
                ,gender
                ,directoryServer + file.getName()
                ,oldFile.getName()
        );

        Call<UpdateUser> call = userRepository.updateUser(updateUser);

        call.enqueue(new Callback<UpdateUser>() {
            @Override
            public void onResponse(Call<UpdateUser> call, Response<UpdateUser> response) {
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().Code.equals("200")){
                        Log.d("Update status","Successfully");
                        listener.onInsertSuccessfully();
                    }else {
                        Log.e("Update status","not 200");
                        listener.onError();
                    }
                }else {
                    Log.e("Update status",response.message());
                    Log.e("Update status",String.valueOf(response.code()));
                    listener.onError();
                }
            }

            @Override
            public void onFailure(Call<UpdateUser> call, Throwable t) {
                Log.e("Update onFailure",t.getMessage());
                //listener.onInsertSuccessfully();
            }
        });
    }

    private void startUpdateUserWithOutImage(){

        UpdateUser updateUser = new UpdateUser(
                Integer.parseInt(user_id)
                ,firstName
                ,lastName
                ,phone
                ,email
                ,gender
        );

        Call<UpdateUser> call = userRepository.updateUser(updateUser);

        call.enqueue(new Callback<UpdateUser>() {
            @Override
            public void onResponse(Call<UpdateUser> call, Response<UpdateUser> response) {
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().Code.equals("200")){
                        Log.d("Update status","Successfully");
                        listener.onInsertSuccessfully();
                    }else {
                        Log.e("Update status","not 200");
                        listener.onError();
                    }
                }else {
                    Log.e("Update status",response.message());
                    Log.e("Update status",String.valueOf(response.code()));
                    listener.onError();
                }
            }

            @Override
            public void onFailure(Call<UpdateUser> call, Throwable t) {
                Log.e("Update onFailure",t.getMessage());
                listener.onInsertSuccessfully();
            }
        });
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setOldFileName(String oldFileName) {
        this.oldFileName = oldFileName;
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
