package com.harrysoft.burstcoinexplorer.observe.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.harry1453.burst.explorer.entity.NetworkStatus;
import com.harry1453.burst.explorer.service.BurstNetworkService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ObserveViewModel extends ViewModel {

    private final BurstNetworkService burstNetworkService;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<NetworkStatus> networkStatus = new MutableLiveData<>();
    private final MutableLiveData<Boolean> refreshing = new MutableLiveData<>();

    ObserveViewModel(BurstNetworkService burstNetworkService) {
        this.burstNetworkService = burstNetworkService;

        fetchNetworkStatus();
    }

    public void fetchNetworkStatus() {
        refreshing.postValue(true);
        compositeDisposable.add(burstNetworkService.fetchNetworkStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNetworkStatus, throwable -> onNetworkStatus(null)));
    }

    private void onNetworkStatus(@Nullable NetworkStatus networkStatus) {
        this.networkStatus.postValue(networkStatus);
        refreshing.postValue(false);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }

    public LiveData<NetworkStatus> getNetworkStatus() { return networkStatus; }
    public LiveData<Boolean> getRefreshing() { return refreshing; }
}
