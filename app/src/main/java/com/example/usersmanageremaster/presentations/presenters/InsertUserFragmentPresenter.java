package com.example.usersmanageremaster.presentations.presenters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.usersmanageremaster.domain.repositories.UserRepository;
import com.example.usersmanageremaster.domain.services.MainServices;
import com.example.usersmanageremaster.domain.usecase.InsertUserUseCase;
import com.example.usersmanageremaster.interfaces.InsertUserFragmentInterface;
import com.example.usersmanageremaster.presentations.views.fragments.InsertUserFragment;

import org.jetbrains.annotations.NotNull;

import java.net.URI;

public class InsertUserFragmentPresenter implements InsertUserFragmentInterface.presenter {

    private final InsertUserFragmentInterface.view view;

    //---User Detail---
    private String fileImgPath;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String gender;

    public InsertUserFragmentPresenter(InsertUserFragment view) {
        this.view = view;
    }

    @Override
    public void CheckNameCondition(
              String fileImgPath
            , String firstName
            , String lastName
            , String email
            , String phone
            , String gender
        ) {

        boolean[] status = new boolean[4];

        if (fileImgPath != null){
            setFileImgPath(fileImgPath);
        }else {
            //----if user did not upload image
            //application will set default image
            setFileImgPath("default.png");
        }

        if (firstName.isEmpty()) {
            view.setErrorFirstName();
        }else {
            setFirstName(firstName);
            status[0] = true;
        }

        if (lastName.isEmpty()) {
            view.setErrorLastName();
        }else {
            setLastName(lastName);
            status[1] = true;
        }

        if (!emailPattern(email)) {
            view.setErrorEmail();
        }else {
            setEmail(email);
            status[2] = true;
        }

        if (phone.isEmpty()) {
            view.setErrorPhone();
        }else {
            setPhone(phone);
            status[3] = true;
        }

        if (gender.equalsIgnoreCase("Male")){
            setGender("1");
        }else {
            setGender("2");
        }

        startInsertUser(status);

    }

    private boolean emailPattern(String email) {
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            return false;
        } else {
            if (email.trim().matches(emailPattern)) {
                return true;
            } else {
                return false;
            }
        }
    }

    private void startInsertUser(@NotNull boolean[] status){

        if (status[0] && status[1] && status[2] && status[3]){
            view.onInsertUser();

            try {
                UserRepository repository = new
                        MainServices()
                        .getRetrofit()
                        .create(UserRepository.class);

                InsertUserUseCase insertUserUseCase = new
                        InsertUserUseCase(
                                repository
                        ,this
                        ,fileImgPath
                        ,firstName
                        ,lastName
                        ,email
                        ,phone
                        ,gender);

            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            Log.w("Insert status","User Information Incorrect");
        }
    }

    public void setFileImgPath(String fileImgPath) {
        this.fileImgPath = fileImgPath;
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

    @Override
    public void onError(){
        Log.e("insert status","FAIL");
    }

    @Override
    public void onInsertSuccessfully() {
        view.onInsertSuccessfully();
    }

}
