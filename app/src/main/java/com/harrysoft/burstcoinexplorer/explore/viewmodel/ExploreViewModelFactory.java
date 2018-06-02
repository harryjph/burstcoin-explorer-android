package com.harrysoft.burstcoinexplorer.explore.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.service.BurstPriceService;
import com.harrysoft.burstcoinexplorer.main.repository.PreferenceRepository;

import javax.inject.Inject;

public class ExploreViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application application;
    private final BurstBlockchainService burstBlockchainService;
    private final BurstPriceService burstPriceService;
    private final PreferenceRepository preferenceRepository;

    @Inject
    ExploreViewModelFactory(Application application, BurstBlockchainService burstBlockchainService, BurstPriceService burstPriceService, PreferenceRepository preferenceRepository) {
        super(application);
        this.application = application;
        this.burstBlockchainService = burstBlockchainService;
        this.burstPriceService = burstPriceService;
        this.preferenceRepository = preferenceRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ExploreViewModel(application, burstBlockchainService, burstPriceService, preferenceRepository);
    }
}
