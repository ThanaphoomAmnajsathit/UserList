package com.example.usersmanageremaster.presentations.presenters;

import android.util.Log;

import com.example.usersmanageremaster.domain.repositories.UserRepository;
import com.example.usersmanageremaster.domain.services.MainServices;
import com.example.usersmanageremaster.domain.usecase.InsertUserUseCase;
import com.example.usersmanageremaster.domain.usecase.UpdateUserUseCase;
import com.example.usersmanageremaster.interfaces.UpdateUserFragmentInterface;

import org.jetbrains.annotations.NotNull;

public class UpdateUserFragmentPresenter implements UpdateUserFragmentInterface.presenter {

    private UpdateUserFragmentInterface.view view;

    private String user_id;
    private String image_user;
    private String old_image;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String gender;

    public UpdateUserFragmentPresenter(
            UpdateUserFragmentInterface.view view
            ,String user_id
            ,String image_user
            ,String firstName
            ,String lastName
            ,String email
            ,String phone
            ,String gender){
        this.view = view;
        setUser_id(user_id);
        setImage_user(image_user);
        setOld_image(image_user);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPhone(phone);
        setGender(gender);
    }


    public void genderCondition(String gender){
        if (gender.equalsIgnoreCase("1")){
            view.onUpdateGenderMale();
        }else if (gender.equalsIgnoreCase("2")){
            view.onUpdateGenderFemale();
        }
    }

    @Override
    public void processUpdateUser(
            String img
            ,String firstName
            ,String lastName
            ,String email
            ,String phone
            ,String gender){

            CheckDataCondition(img,old_image,firstName,lastName,email,phone,gender);
        }



    private void CheckDataCondition(
            String fileImgPath
            , String old_image
            , String firstName
            , String lastName
            , String email
            , String phone
            , String gender
    ) {

        boolean[] status = new boolean[4];
        boolean imageIsNewChange = false;

        if (fileImgPath != null){
            setImage_user(fileImgPath);
            Log.e("fileImgPathfileImgPath",fileImgPath);
            imageIsNewChange = true;
        }

        if (old_image != null){
            setOld_image(old_image);
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

        //----Start Update User Here---
        if (imageIsNewChange){

            startUpdateUserWithNewImage(status);
        }else {

            startUpdateUserWithOutImage(status);
        }
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


    //-------Call UseCase-----
    private void startUpdateUserWithNewImage(@NotNull boolean[] status){
        if (status[0] && status[1] && status[2] && status[3]){

            try {
                UserRepository repository = new
                        MainServices()
                        .getRetrofit()
                        .create(UserRepository.class);

                UpdateUserUseCase updateUserUseCase = new
                        UpdateUserUseCase(
                        repository
                        ,this
                        ,user_id
                        ,image_user
                        ,old_image
                        ,firstName
                        ,lastName
                        ,email
                        ,phone
                        ,gender);

            }catch (Exception e){
                e.printStackTrace();
            }

            //Log.e("NEW IMAGE IS",image_user);

        }else {
            Log.w("Insert status","User Information Incorrect");
        }
    }

    private void startUpdateUserWithOutImage(@NotNull boolean[] status){
        if (status[0] && status[1] && status[2] && status[3]){

            try {
                UserRepository repository = new
                        MainServices()
                        .getRetrofit()
                        .create(UserRepository.class);

                UpdateUserUseCase updateUserUseCase = new
                        UpdateUserUseCase(
                        repository
                        ,this
                        ,user_id
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

    @Override
    public void onInsertSuccessfully() {
        Log.e("UpdateUserStatus","Update Successfully");
        view.onPopBack();
    }

    @Override
    public void onError(){
        Log.e("UpdateUserStatus","ERROR");
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setImage_user(String image_user) {
        this.image_user = image_user;
    }

    public void setOld_image(String old_image) {
        this.old_image = old_image;
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
