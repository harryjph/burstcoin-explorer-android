package com.harrysoft.burstcoinexplorer.burst.service;

import com.harrysoft.burstcoinexplorer.burst.entity.EventInfo;

import io.reactivex.Single;

public interface BurstInfoService {
    Single<EventInfo[]> getEvents();
}
