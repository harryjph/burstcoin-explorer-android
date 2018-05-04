package com.harrysoft.burstcoinexplorer.di;

import android.content.Context;

import com.harrysoft.burstcoinexplorer.burst.api.BurstAPIService;
import com.harrysoft.burstcoinexplorer.burst.api.BurstPriceService;
import com.harrysoft.burstcoinexplorer.burst.api.CMCPriceService;
import com.harrysoft.burstcoinexplorer.burst.api.PoccAPIService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BurstModule {
    @Singleton
    @Provides
    BurstAPIService provideBurstApiService(Context context) {
        return new PoccAPIService(context);
    }

    @Singleton
    @Provides
    BurstPriceService provideBurstPriceService(Context context) {
        return new CMCPriceService(context);
    }
}
