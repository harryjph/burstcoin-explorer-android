package com.harrysoft.burstcoinexplorer.di;

import android.content.Context;

import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.service.BurstInfoService;
import com.harrysoft.burstcoinexplorer.burst.service.BurstNetworkService;
import com.harrysoft.burstcoinexplorer.burst.service.BurstPriceService;
import com.harrysoft.burstcoinexplorer.burst.service.CMCPriceService;
import com.harrysoft.burstcoinexplorer.burst.service.PoCCBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.service.PoCCNetworkService;
import com.harrysoft.burstcoinexplorer.burst.service.RepoInfoService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class BurstServiceModule {
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
