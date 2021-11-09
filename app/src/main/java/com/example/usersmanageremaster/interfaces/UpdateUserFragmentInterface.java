package com.example.usersmanageremaster.interfaces;

public interface UpdateUserFragmentInterface {

    interface view{
        void onUpdateGenderMale();

        void onUpdateGenderFemale();

        void setErrorFirstName();

        void setErrorLastName();

        void setErrorEmail();

        void setErrorPhone();

        void onPopBack();
    }

    interface presenter{
        void processUpdateUser(
                String img
                ,String firstName
                ,String lastName
                ,String email
                ,String phone
                ,String gender);

        void onInsertSuccessfully();

        void onError();


        }
}
