package com.harry1453.burst.explorer.service;

import com.harry1453.burst.explorer.repository.ConfigRepository;

public final class BurstServiceProvider {

    private final ObjectService objectService;
    private final ConfigRepository configRepository;

    BurstServiceProvider(ObjectService objectService, ConfigRepository configRepository) {
        this.objectService = objectService;
        this.configRepository = configRepository;
    }

    public BurstBlockchainService getBurstBlockchainService() {
        return new NodeBlockchainService(configRepository, objectService);
    }

    public BurstInfoService getBurstInfoService() {
        return new RepoInfoService(objectService);
    }

    public BurstNetworkService getBurstNetworkService() {
        return new PoCCNetworkService(objectService);
    }

    public BurstPriceService getBurstPriceService() {
        return new CMCPriceService(objectService);
    }
}
