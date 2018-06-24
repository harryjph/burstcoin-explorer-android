package com.harrysoft.burstcoinexplorer.burst.service;

import com.harrysoft.burstcoinexplorer.main.repository.PreferenceRepository;

public final class BurstServiceProviders {
    public static BurstServiceProvider getBurstServiceProvider(ObjectService objectService, PreferenceRepository preferenceRepository) {
        return new BurstServiceProvider(objectService, preferenceRepository);
    }

    public static ObjectService getObjectService(NetworkService networkService) {
        return new NetworkObjectService(networkService, new GsonDeserializerService());
    }

    public static ObjectService getObjectService(NetworkService networkService, DeserializerService deserializerService) {
        return new NetworkObjectService(networkService, deserializerService);
    }
}
