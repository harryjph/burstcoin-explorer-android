package com.harrysoft.burstcoinexplorer.burst.service;

import com.harrysoft.burstcoinexplorer.burst.entity.BurstPrice;

import io.reactivex.Single;

public interface BurstPriceService {
    Single<BurstPrice> fetchPrice(String currencyCode);
}
