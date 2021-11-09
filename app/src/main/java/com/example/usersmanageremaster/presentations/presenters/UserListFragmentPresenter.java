package com.example.usersmanageremaster.presentations.presenters;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usersmanageremaster.Models.User;
import com.example.usersmanageremaster.domain.repositories.UserRepository;
import com.example.usersmanageremaster.domain.services.MainServices;
import com.example.usersmanageremaster.domain.usecase.DeleteUserUseCase;
import com.example.usersmanageremaster.domain.usecase.GetUsersUseCase;
import com.example.usersmanageremaster.interfaces.UserListFragmentInterface;

import java.util.List;

public class UserListFragmentPresenter implements UserListFragmentInterface.presenter {

    private UserListFragmentInterface.view view;
    private GetUsersUseCase getUsersUseCase;
    private DeleteUserUseCase deleteUserUseCase;

    //-----Constructor for Get Users List-----
    public UserListFragmentPresenter(UserListFragmentInterface.view view,int count,int offset){
        this.view = view;
        UserRepository repository = new MainServices().getRetrofit().create(UserRepository.class);
        getUsersUseCase = new GetUsersUseCase(repository,this, count, offset);
        getUsersUseCase.execute(null);
    }

    //-----Constructor for Delete User-----
    public UserListFragmentPresenter(UserListFragmentInterface.view view , String userId ,String image_name){
        this.view = view;
        UserRepository repository = new MainServices().getRetrofit().create(UserRepository.class);
        deleteUserUseCase = new DeleteUserUseCase(repository ,this,userId , image_name);
    }

    @Override
    public void genderBottomSheetCondition(
            int gender
            , ImageView iv_gender
            , TextView tv_gender)
    {
        if (gender == 1){
            view.onSetSheetGenderMan(iv_gender,tv_gender);
        }else if (gender == 2){
            view.onSetSheetGenderFemale(iv_gender,tv_gender);
        }
    }

    @Override
    public void onSetUserList(List<User> users) {
        view.onSetUserList(users);
    }


    @Override
    public void onError() {
        Log.d("Error","Error");
    }

}
