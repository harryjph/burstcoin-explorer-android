package com.harrysoft.burstcoinexplorer.observe.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.harry1453.burst.explorer.entity.NetworkStatus;

import java.util.List;

public class ObserveVersionsViewModel extends ViewModel {

    private final MutableLiveData<List<NetworkStatus.PeersData.PeerVersion>> peerVersions = new MutableLiveData<>();
    private final MutableLiveData<Long> peerCount = new MutableLiveData<>();

    public void setPeerVersions(List<NetworkStatus.PeersData.PeerVersion> peerVersions) {
        this.peerVersions.postValue(peerVersions);
        long peerCount = 0;
        for (NetworkStatus.PeersData.PeerVersion peerVersion : peerVersions) {
            peerCount += peerVersion.count;
        }
        this.peerCount.postValue(peerCount);
    }

    public LiveData<List<NetworkStatus.PeersData.PeerVersion>> getPeerVersions() { return peerVersions; }
    public LiveData<Long> getPeerCount() { return peerCount; }
}
