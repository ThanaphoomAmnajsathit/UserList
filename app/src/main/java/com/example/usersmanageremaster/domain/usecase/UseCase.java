package com.example.usersmanageremaster.domain.usecase;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class UseCase <T,Parms> {
    private final CompositeDisposable compositeDisposable;
    public UseCase() {
        compositeDisposable = new CompositeDisposable();
    }
    public abstract Observable<T> buildObservable(Parms parms);

    protected DisposableObserver<T> getDisposableObservable() {
        return getDisposableObservable();
    }
    public void execute(DisposableObserver<T> disposable, Parms parms) {
        Observable<T> observable = this.buildObservable(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        addDispose(observable.subscribeWith(disposable));
    }
    public void execute(Parms parms) {
        Observable<T> observable = this.buildObservable(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        addDispose(observable.subscribeWith(this.getDisposableObservable()));
    }
    public void clearDispose() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
    private void addDispose(Disposable disposable) {
        if (disposable != null) {
            compositeDisposable.add(disposable);
        }
    }
    public boolean isDispose() {
        return compositeDisposable.isDisposed();
    }
}
