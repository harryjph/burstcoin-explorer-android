package com.harry1453.burst.explorer.service;

import com.harry1453.burst.explorer.entity.BurstPrice;

import io.reactivex.Single;

public interface BurstPriceService {
    Single<BurstPrice> fetchPrice(String currencyCode);
}
