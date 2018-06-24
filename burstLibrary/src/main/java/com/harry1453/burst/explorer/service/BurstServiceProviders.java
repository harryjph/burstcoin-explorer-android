package com.harry1453.burst.explorer.service;

import com.harry1453.burst.explorer.repository.ConfigRepository;

public final class BurstServiceProviders {
    public static BurstServiceProvider getBurstServiceProvider(ObjectService objectService, ConfigRepository configRepository) {
        return new BurstServiceProvider(objectService, configRepository);
    }

    public static ObjectService getObjectService(NetworkService networkService) {
        return new NetworkObjectService(networkService, new GsonDeserializerService());
    }

    public static ObjectService getObjectService(NetworkService networkService, DeserializerService deserializerService) {
        return new NetworkObjectService(networkService, deserializerService);
    }
}
