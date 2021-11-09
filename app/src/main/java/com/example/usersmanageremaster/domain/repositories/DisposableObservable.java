package com.example.usersmanageremaster.domain.repositories;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public class DisposableObservable<T> extends DisposableObserver<T> {

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
