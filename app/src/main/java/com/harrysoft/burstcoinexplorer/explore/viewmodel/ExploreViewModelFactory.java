package com.harrysoft.burstcoinexplorer.explore.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.harry1453.burst.explorer.repository.ConfigRepository;
import com.harry1453.burst.explorer.service.BurstPriceService;

import javax.inject.Inject;

import burst.kit.service.BurstNodeService;

public class ExploreViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application application;
    private final BurstNodeService burstNodeService;
    private final BurstPriceService burstPriceService;
    private final ConfigRepository configRepository;

    @Inject
    ExploreViewModelFactory(Application application, BurstNodeService burstNodeService, BurstPriceService burstPriceService, ConfigRepository configRepository) {
        super(application);
        this.application = application;
        this.burstNodeService = burstNodeService;
        this.burstPriceService = burstPriceService;
        this.configRepository = configRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ExploreViewModel(application, burstNodeService, burstPriceService, configRepository);
    }
}
