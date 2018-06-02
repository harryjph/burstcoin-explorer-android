package com.harrysoft.burstcoinexplorer.observe.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.harrysoft.burstcoinexplorer.burst.service.BurstNetworkService;

import javax.inject.Inject;

public class ObserveViewModelFactory implements ViewModelProvider.Factory {

    private final BurstNetworkService burstNetworkService;

    @Inject
    public ObserveViewModelFactory(BurstNetworkService burstNetworkService) {
        this.burstNetworkService = burstNetworkService;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ObserveViewModel(burstNetworkService);
    }
}
