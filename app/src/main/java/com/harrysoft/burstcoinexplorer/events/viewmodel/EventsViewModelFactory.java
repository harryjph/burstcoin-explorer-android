package com.harrysoft.burstcoinexplorer.events.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.service.BurstInfoService;

import javax.inject.Inject;

public class EventsViewModelFactory implements ViewModelProvider.Factory {

    private final BurstBlockchainService burstBlockchainService;
    private final BurstInfoService burstInfoService;

    @Inject
    EventsViewModelFactory(BurstBlockchainService burstBlockchainService, BurstInfoService burstInfoService) {
        this.burstBlockchainService = burstBlockchainService;
        this.burstInfoService = burstInfoService;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EventsViewModel(burstBlockchainService, burstInfoService);
    }
}
