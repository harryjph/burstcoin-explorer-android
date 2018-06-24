package com.harry1453.burst.explorer.service;

import com.harry1453.burst.explorer.entity.NetworkStatus;

import io.reactivex.Single;

public final class PoCCNetworkService implements BurstNetworkService {

    private static final String NETWORK_STATUS_URL = "https://explore.burst.cryptoguru.org/api/v1/observe/";

    private final ObjectService objectService;

    PoCCNetworkService(ObjectService objectService) {
        this.objectService = objectService;
    }

    @Override
    public Single<NetworkStatus> fetchNetworkStatus() {
        return objectService.fetchObject(NETWORK_STATUS_URL, NetworkStatusApiResponse.class)
                .map(networkStatusApiResponse -> networkStatusApiResponse.data);
    }

    private class NetworkStatusApiResponse {
        NetworkStatus data;
    }
}
