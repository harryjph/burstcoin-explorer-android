package com.harry1453.burst.explorer.service

import com.harry1453.burst.explorer.entity.EventInfo

import io.reactivex.Single

interface BurstInfoService {
    val events: Single<List<EventInfo>>
}
