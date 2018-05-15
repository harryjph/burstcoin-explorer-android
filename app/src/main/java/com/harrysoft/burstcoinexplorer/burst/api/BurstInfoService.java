package com.harrysoft.burstcoinexplorer.burst.api;

import com.harrysoft.burstcoinexplorer.burst.entity.ForkInfo;

import io.reactivex.Single;

public interface BurstInfoService {
    Single<ForkInfo[]> getForks();
}
