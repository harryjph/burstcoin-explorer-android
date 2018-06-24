package com.harry1453.burst.explorer.service;

import com.harry1453.burst.explorer.entity.NetworkStatus;

import io.reactivex.Single;

public interface BurstNetworkService {
    Single<NetworkStatus> fetchNetworkStatus();
}
