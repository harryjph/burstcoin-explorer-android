package com.harrysoft.burstcoinexplorer.burst.service;

import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;

import io.reactivex.Single;

public interface BurstNetworkService {
    Single<NetworkStatus> fetchNetworkStatus();
}
