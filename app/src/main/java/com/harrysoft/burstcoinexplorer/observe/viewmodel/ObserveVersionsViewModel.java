package com.harrysoft.burstcoinexplorer.observe.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.harry1453.burst.explorer.entity.NetworkStatus;

import java.util.List;

public class ObserveVersionsViewModel extends ViewModel {

    private final MutableLiveData<List<NetworkStatus.PeersData.PeerVersion>> peerVersions = new MutableLiveData<>();

    public void setPeerVersions(List<NetworkStatus.PeersData.PeerVersion> peerVersions) {
        this.peerVersions.postValue(peerVersions);
    }

    public LiveData<List<NetworkStatus.PeersData.PeerVersion>> getPeerVersions() { return peerVersions; }
}
