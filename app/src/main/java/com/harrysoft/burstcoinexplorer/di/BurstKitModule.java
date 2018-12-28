package com.harrysoft.burstcoinexplorer.di;

import com.harry1453.burst.explorer.repository.ConfigRepository;
import com.harrysoft.burstcoinexplorer.main.service.AndroidSchedulerAssigner;

import javax.inject.Singleton;

import burst.kit.burst.BurstCrypto;
import burst.kit.service.BurstNodeService;
import dagger.Module;
import dagger.Provides;

@Module
public class BurstKitModule {
    @Singleton
    @Provides
    public BurstNodeService provideBurstNodeService(ConfigRepository configRepository) { // TODO address switching
        return BurstNodeService.getInstance(configRepository.getNodeAddress(), new AndroidSchedulerAssigner());
    }

    @Singleton
    @Provides
    public BurstCrypto provideBurstCrypto() {
        return BurstCrypto.getInstance();
    }
}
