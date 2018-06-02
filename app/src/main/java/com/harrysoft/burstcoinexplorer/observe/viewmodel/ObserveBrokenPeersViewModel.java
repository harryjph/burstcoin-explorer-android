package com.harrysoft.burstcoinexplorer.observe.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;

import java.util.List;

public class ObserveBrokenPeersViewModel extends ViewModel {

    private final MutableLiveData<List<NetworkStatus.BrokenPeer>> brokenPeers = new MutableLiveData<>();

    public void setBrokenPeers(List<NetworkStatus.BrokenPeer> brokenPeers) {
        this.brokenPeers.postValue(brokenPeers);
    }

    public LiveData<List<NetworkStatus.BrokenPeer>> getBrokenPeers() { return brokenPeers; }
}
