package com.harrysoft.burstcoinexplorer.burst.api;

import com.harrysoft.burstcoinexplorer.burst.entity.BurstPrice;

import io.reactivex.Single;

public interface BurstPriceService {
    Single<BurstPrice> fetchPrice();
}
