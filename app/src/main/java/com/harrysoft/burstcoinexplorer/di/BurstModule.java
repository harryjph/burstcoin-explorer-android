package com.harrysoft.burstcoinexplorer.di;

import android.content.Context;

import com.harrysoft.burstcoinexplorer.burst.api.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.api.BurstPriceService;
import com.harrysoft.burstcoinexplorer.burst.api.CMCPriceService;
import com.harrysoft.burstcoinexplorer.burst.api.PoccBlockchainService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BurstModule {
    @Singleton
    @Provides
    BurstBlockchainService provideBurstApiService(Context context) {
        return new PoccBlockchainService(context);
    }

    @Singleton
    @Provides
    BurstPriceService provideBurstPriceService(Context context) {
        return new CMCPriceService(context);
    }
}
