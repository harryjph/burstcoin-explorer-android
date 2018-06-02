package com.harrysoft.burstcoinexplorer.events.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.harrysoft.burstcoinexplorer.burst.entity.EventInfo;
import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;
import com.harrysoft.burstcoinexplorer.burst.service.BurstInfoService;
import com.harrysoft.burstcoinexplorer.burst.service.BurstNetworkService;
import com.harrysoft.burstcoinexplorer.events.entity.EventsList;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class EventsViewModel extends ViewModel implements SwipeRefreshLayout.OnRefreshListener {

    private final BurstNetworkService burstNetworkService;
    private final BurstInfoService burstInfoService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<EventsList> eventsList = new MutableLiveData<>();
    private final MutableLiveData<Integer> errorMessageVisibility = new MutableLiveData<>();
    private final MutableLiveData<Boolean> refreshing = new MutableLiveData<>();

    @Nullable
    private BigInteger blockHeight;
    @Nullable
    private List<EventInfo> eventInfoList;

    EventsViewModel(BurstNetworkService burstNetworkService, BurstInfoService burstInfoService) {
        this.burstNetworkService = burstNetworkService;
        this.burstInfoService = burstInfoService;

        // Update immediately
        refreshing.postValue(true);
        onRefresh();
    }

    private void onNetworkStatus(NetworkStatus networkStatus) {
        blockHeight = networkStatus.blockHeight;
        updateEventsList();
    }

    private void onEventInfoList(EventInfo[] newEventInfoList) {
        refreshing.postValue(false);
        errorMessageVisibility.postValue(View.GONE);
        eventInfoList = Arrays.asList(newEventInfoList);
        updateEventsList();
    }

    @Override
    public void onRefresh() {
        compositeDisposable.add(burstNetworkService.fetchNetworkStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNetworkStatus, t -> onError()));
        compositeDisposable.add(burstInfoService.getEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventInfoList, t -> onError()));
    }

    private void updateEventsList() {
        if (blockHeight != null && eventInfoList != null) {
            eventsList.postValue(new EventsList(blockHeight, eventInfoList));
        } else {
            eventsList.postValue(EventsList.EMPTY);
        }
    }

    private void onError() {
        blockHeight = null;
        eventInfoList = null;
        eventsList.postValue(EventsList.EMPTY);
        errorMessageVisibility.postValue(View.VISIBLE);
        refreshing.postValue(false);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }

    public LiveData<EventsList> getEventsList() { return eventsList; }
    public LiveData<Integer> getErrorMessageVisibility() { return errorMessageVisibility; }
    public LiveData<Boolean> getRefreshing() { return refreshing; }
}
