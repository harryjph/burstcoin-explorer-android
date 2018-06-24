package com.harrysoft.burstcoinexplorer.burst.service;

import com.harrysoft.burstcoinexplorer.main.repository.PreferenceRepository;

public final class BurstServiceProvider {

    private final ObjectService objectService;
    private final PreferenceRepository preferenceRepository;

    BurstServiceProvider(ObjectService objectService, PreferenceRepository preferenceRepository) {
        this.objectService = objectService;
        this.preferenceRepository = preferenceRepository;
    }

    public BurstBlockchainService getBurstBlockchainService() {
        return new NodeBlockchainService(preferenceRepository, objectService);
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
