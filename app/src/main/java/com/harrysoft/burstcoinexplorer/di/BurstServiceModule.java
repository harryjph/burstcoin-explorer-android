package com.harrysoft.burstcoinexplorer.di;

import com.harry1453.burst.explorer.repository.ConfigRepository;
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
    BurstBlockchainService provideBurstBlockchainService(ObjectService objectService, ConfigRepository configRepository) {
        return BurstServiceProviders.getBurstServiceProvider(objectService, configRepository).getBurstBlockchainService();
    }

    @Singleton
    @Provides
    BurstNetworkService provideBurstNetworkService(ObjectService objectService, ConfigRepository configRepository) {
        return BurstServiceProviders.getBurstServiceProvider(objectService, configRepository).getBurstNetworkService();
    }

    @Singleton
    @Provides
    BurstPriceService provideBurstPriceService(ObjectService objectService, ConfigRepository configRepository) {
        return BurstServiceProviders.getBurstServiceProvider(objectService, configRepository).getBurstPriceService();
    }

    @Singleton
    @Provides
    BurstInfoService provideBurstInfoService(ObjectService objectService, ConfigRepository configRepository) {
        return BurstServiceProviders.getBurstServiceProvider(objectService, configRepository).getBurstInfoService();
    }
}
