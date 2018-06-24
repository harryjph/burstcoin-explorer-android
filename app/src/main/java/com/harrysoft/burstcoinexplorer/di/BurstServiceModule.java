package com.harrysoft.burstcoinexplorer.di;

import com.harry1453.burst.explorer.repository.PreferenceRepository;
import com.harry1453.burst.explorer.service.BurstBlockchainService;
import com.harry1453.burst.explorer.service.BurstInfoService;
import com.harry1453.burst.explorer.service.BurstNetworkService;
import com.harry1453.burst.explorer.service.BurstPriceService;
import com.harry1453.burst.explorer.service.BurstServiceProviders;
import com.harry1453.burst.explorer.service.ObjectService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class BurstServiceModule {
    @Singleton
    @Provides
    BurstBlockchainService provideBurstBlockchainService(ObjectService objectService, PreferenceRepository preferenceRepository) {
        return BurstServiceProviders.getBurstServiceProvider(objectService, preferenceRepository).getBurstBlockchainService();
    }

    @Singleton
    @Provides
    BurstNetworkService provideBurstNetworkService(ObjectService objectService, PreferenceRepository preferenceRepository) {
        return BurstServiceProviders.getBurstServiceProvider(objectService, preferenceRepository).getBurstNetworkService();
    }

    @Singleton
    @Provides
    BurstPriceService provideBurstPriceService(ObjectService objectService, PreferenceRepository preferenceRepository) {
        return BurstServiceProviders.getBurstServiceProvider(objectService, preferenceRepository).getBurstPriceService();
    }

    @Singleton
    @Provides
    BurstInfoService provideBurstInfoService(ObjectService objectService, PreferenceRepository preferenceRepository) {
        return BurstServiceProviders.getBurstServiceProvider(objectService, preferenceRepository).getBurstInfoService();
    }
}
