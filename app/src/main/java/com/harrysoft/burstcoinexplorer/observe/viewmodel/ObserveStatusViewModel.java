package com.harrysoft.burstcoinexplorer.observe.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.harry1453.burst.explorer.entity.NetworkStatus;

public class ObserveStatusViewModel extends ViewModel {

    private final MutableLiveData<NetworkStatus.PeersData.PeersStatus> peersStatus = new MutableLiveData<>();

    public void setPeersStatus(NetworkStatus.PeersData.PeersStatus peersStatus) {
        this.peersStatus.postValue(peersStatus);
    }

    public LiveData<NetworkStatus.PeersData.PeersStatus> getPeersStatus() { return peersStatus; }
}
