package com.harrysoft.burstcoinexplorer.di;

import android.content.Context;

import com.harrysoft.burstcoinexplorer.burst.api.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.api.BurstInfoService;
import com.harrysoft.burstcoinexplorer.burst.api.BurstNetworkService;
import com.harrysoft.burstcoinexplorer.burst.api.BurstPriceService;
import com.harrysoft.burstcoinexplorer.burst.api.CMCPriceService;
import com.harrysoft.burstcoinexplorer.burst.api.PoCCBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.api.PoCCNetworkService;
import com.harrysoft.burstcoinexplorer.burst.api.RepoInfoService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class BurstModule {
    @Singleton
    @Provides
    BurstBlockchainService provideBurstBlockchainService(Context context) {
        return new PoCCBlockchainService(context);
    }

    @Singleton
    @Provides
    BurstNetworkService provideBurstNetworkService(Context context) {
        return new PoCCNetworkService(context);
    }

    @Singleton
    @Provides
    BurstPriceService provideBurstPriceService(Context context) {
        return new CMCPriceService(context);
    }

    @Singleton
    @Provides
    BurstInfoService provideBurstInfoService(Context context) {
        return new RepoInfoService(context);
    }
}
