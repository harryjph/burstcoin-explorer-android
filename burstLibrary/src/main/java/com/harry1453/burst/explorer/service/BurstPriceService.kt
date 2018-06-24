package com.harry1453.burst.explorer.service

import com.harry1453.burst.explorer.entity.BurstPrice

import io.reactivex.Single

interface BurstPriceService {
    fun fetchPrice(currencyCode: String): Single<BurstPrice>
}
