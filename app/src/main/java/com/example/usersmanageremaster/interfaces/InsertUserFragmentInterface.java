package com.example.usersmanageremaster.interfaces;

import android.content.Context;
import android.net.Uri;

public interface InsertUserFragmentInterface {

    interface view{

        void onInsertUser();

        void setErrorFirstName();

        void setErrorLastName();

        void setErrorEmail();

        void setErrorPhone();

        void onInsertSuccessfully();
    }

    interface presenter{

        void CheckNameCondition(
                String fileImgPath
                ,String firstName
                ,String lastName
                ,String email
                ,String phone
                ,String gender);

        void onError();

        void onInsertSuccessfully();
    }

}
