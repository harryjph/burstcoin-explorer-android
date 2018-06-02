package com.harrysoft.burstcoinexplorer.events.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.harrysoft.burstcoinexplorer.burst.service.BurstInfoService;
import com.harrysoft.burstcoinexplorer.burst.service.BurstNetworkService;

import javax.inject.Inject;

public class EventsViewModelFactory implements ViewModelProvider.Factory {

    private final BurstNetworkService burstNetworkService;
    private final BurstInfoService burstInfoService;

    @Inject
    public EventsViewModelFactory(BurstNetworkService burstNetworkService, BurstInfoService burstInfoService) {
        this.burstNetworkService = burstNetworkService;
        this.burstInfoService = burstInfoService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EventsViewModel(burstNetworkService, burstInfoService);
    }
}
