package com.harrysoft.burstcoinexplorer.di;

import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.service.BurstInfoService;
import com.harrysoft.burstcoinexplorer.burst.service.BurstNetworkService;
import com.harrysoft.burstcoinexplorer.burst.service.BurstPriceService;
import com.harrysoft.burstcoinexplorer.burst.service.BurstServiceProviders;
import com.harrysoft.burstcoinexplorer.burst.service.ObjectService;
import com.harrysoft.burstcoinexplorer.main.repository.PreferenceRepository;

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
