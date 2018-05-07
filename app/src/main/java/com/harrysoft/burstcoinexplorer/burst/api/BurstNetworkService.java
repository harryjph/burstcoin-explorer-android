package com.harrysoft.burstcoinexplorer.burst.api;

import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;

import io.reactivex.Single;

public interface BurstNetworkService {
    Single<NetworkStatus> fetchNetworkStatus();
}
