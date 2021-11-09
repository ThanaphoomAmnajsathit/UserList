package com.example.usersmanageremaster.domain.usecase;

import android.util.Log;

import com.example.usersmanageremaster.Models.DeleteUser;
import com.example.usersmanageremaster.domain.repositories.UserRepository;
import com.example.usersmanageremaster.presentations.presenters.InsertUserFragmentPresenter;
import com.example.usersmanageremaster.presentations.presenters.UserListFragmentPresenter;
import com.example.usersmanageremaster.presentations.views.fragments.UserListFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteUserUseCase {

    private final UserRepository userRepository;
    private final UserListFragmentPresenter listener;

    public DeleteUserUseCase(
            UserRepository userRepository
            , UserListFragmentPresenter listener
            , String userId
            , String image_name){
        this.userRepository = userRepository;
        this.listener = listener;
        deleteUser(userId , image_name);
        Log.e("======",image_name);
    }

    private void deleteUser(String userId , String image_name){
        DeleteUser deleteUser = new DeleteUser(userId , image_name);
        Call<DeleteUser> call = userRepository.deleteUser(deleteUser);

        call.enqueue(new Callback<DeleteUser>() {
            @Override
            public void onResponse(Call<DeleteUser> call, Response<DeleteUser> response) {
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().Code.equals("200")){
                        Log.d("Delete User status","Successfully");
                    }else {
                        Log.e("Delete User status","Not 200");
                        listener.onError();
                    }
                }else {
                    Log.e("Delete User status",response.message());
                    Log.e("Delete User status",String.valueOf(response.code()));
                    listener.onError();
                }
            }

            @Override
            public void onFailure(Call<DeleteUser> call, Throwable t) {
                Log.e("Delete User onFailure",t.getMessage());
            }
        });
    }
}
