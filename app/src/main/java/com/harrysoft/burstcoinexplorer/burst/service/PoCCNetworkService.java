package com.harrysoft.burstcoinexplorer.burst.service;

import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;

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
