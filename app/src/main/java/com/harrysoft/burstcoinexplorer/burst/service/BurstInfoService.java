package com.harrysoft.burstcoinexplorer.burst.service;

import com.harrysoft.burstcoinexplorer.burst.entity.EventInfo;

import java.util.List;

import io.reactivex.Single;

public interface BurstInfoService {
    Single<List<EventInfo>> getEvents();
}
