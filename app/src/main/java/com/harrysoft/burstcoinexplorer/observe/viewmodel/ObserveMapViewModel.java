package com.harrysoft.burstcoinexplorer.observe.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.harry1453.burst.explorer.entity.NetworkStatus;
import com.harry1453.burst.explorer.service.RepoInfoService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ObserveMapViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<String> url = new MutableLiveData<>();

    public void onNetworkStatus(NetworkStatus networkStatus) {
        compositeDisposable.add(RepoInfoService.getNetworkMapDisplayURL(networkStatus)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(url::postValue, Throwable::printStackTrace));
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }

    public LiveData<String> getURL() { return url; }
}
