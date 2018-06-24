package com.harry1453.burst.explorer.service

import com.harry1453.burst.explorer.entity.NetworkStatus

import io.reactivex.Single

interface BurstNetworkService {
    val networkStatus: Single<NetworkStatus>
}
