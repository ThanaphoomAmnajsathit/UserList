package com.example.usersmanageremaster.interfaces;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.usersmanageremaster.Models.User;

import java.util.List;

public interface UserListFragmentInterface {

    interface view{

        void onSetUserList(List<User> users);

        void onSetSheetGenderMan(ImageView iv_gender_btm, TextView user_gender);

        void onSetSheetGenderFemale(ImageView iv_gender_btm, TextView user_gender);

    }

    interface presenter{

        void genderBottomSheetCondition(int gender, ImageView iv_gender, TextView tv_gender);

        void onSetUserList(List<User> users);

        void onError();
    }
}
