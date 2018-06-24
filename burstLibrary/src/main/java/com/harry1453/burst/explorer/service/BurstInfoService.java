package com.harry1453.burst.explorer.service;

import com.harry1453.burst.explorer.entity.EventInfo;

import java.util.List;

import io.reactivex.Single;

public interface BurstInfoService {
    Single<List<EventInfo>> getEvents();
}
