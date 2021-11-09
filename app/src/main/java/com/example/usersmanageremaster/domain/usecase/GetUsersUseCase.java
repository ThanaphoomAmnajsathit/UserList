package com.example.usersmanageremaster.domain.usecase;

import android.util.Log;

import com.example.usersmanageremaster.Models.ReturnUsers;
import com.example.usersmanageremaster.domain.repositories.DisposableObservable;
import com.example.usersmanageremaster.domain.repositories.UserRepository;
import com.example.usersmanageremaster.presentations.presenters.UserListFragmentPresenter;
import com.example.usersmanageremaster.presentations.views.fragments.UserListFragment;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import retrofit2.Response;

public class GetUsersUseCase extends UseCase<Response<ReturnUsers>, Void> {

    private final String token = "d94ad5c0b23bc4ef0b39425b2ef1e15e";

    private final UserRepository userRepository;
    private final UserListFragmentPresenter listener;
    private int count;
    private int offset;
    private String search;


    public GetUsersUseCase(UserRepository userRepository, UserListFragmentPresenter listener, int count, int offset) {
        this.userRepository = userRepository;
        this.listener = listener;
        this.count = count;
        this.offset = offset;
    }

    public GetUsersUseCase(
            UserRepository userRepository
            , UserListFragmentPresenter listener
            , String search){
        this.userRepository = userRepository;
        this.listener = listener;
        this.search = search;
    }

    @Override
    public Observable<Response<ReturnUsers>> buildObservable(Void aVoid) {
        if (search != null){
            return userRepository.searchUsers(search);
        }else {
            return userRepository.getUsers(token,String.valueOf(count), String.valueOf(offset));
        }
    }

    @Override
    protected DisposableObservable<Response<ReturnUsers>> getDisposableObservable() {
        return new DisposableGetUsers();
    }

    class DisposableGetUsers extends DisposableObservable<Response<ReturnUsers>> {

        @Override
        public void onNext(Response<ReturnUsers> response) {
            super.onNext(response);
            if (response.isSuccessful() && response.body() != null) {
                if (response.body().code.equals("200")) {

                    listener.onSetUserList(response.body().users);

                } else {
                    Log.d("Error","Code NOT 200");
                    listener.onError();
                }
            } else {
                Log.d("Error","BODY NULL");
                listener.onError();
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            listener.onError();
            Log.d("ONGTEST", e.getMessage().toString());
        }


    }
}
