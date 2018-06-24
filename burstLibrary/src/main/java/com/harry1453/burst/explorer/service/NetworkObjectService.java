package com.harry1453.burst.explorer.service;

import io.reactivex.Single;

public final class NetworkObjectService implements ObjectService {

    private final NetworkService networkService;
    private final DeserializerService deserializerService;

    NetworkObjectService(NetworkService networkService, DeserializerService deserializerService) {
        this.networkService = networkService;
        this.deserializerService = deserializerService;
    }

    @Override
    public <T> Single<T> fetchObject(String url, Class<T> objectClass) {
        return networkService.fetchData(url)
                .flatMap(objectData -> deserializerService.deserializeObject(objectData, objectClass));
    }
}
