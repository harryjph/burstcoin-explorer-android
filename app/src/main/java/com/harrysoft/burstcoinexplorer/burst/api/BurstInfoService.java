package com.harrysoft.burstcoinexplorer.burst.api;

import com.harrysoft.burstcoinexplorer.burst.entity.EventInfo;

import io.reactivex.Single;

public interface BurstInfoService {
    Single<EventInfo[]> getEvents();
}
