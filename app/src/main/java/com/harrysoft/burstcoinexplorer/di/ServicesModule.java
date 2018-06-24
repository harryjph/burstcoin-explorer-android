package com.harrysoft.burstcoinexplorer.di;

import android.content.Context;

import com.harrysoft.burstcoinexplorer.burst.service.AndroidNetworkService;
import com.harrysoft.burstcoinexplorer.burst.service.BurstServiceProviders;
import com.harrysoft.burstcoinexplorer.burst.service.NetworkService;
import com.harrysoft.burstcoinexplorer.burst.service.ObjectService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class ServicesModule {
    @Singleton
    @Provides
    NetworkService provideNetworkService(Context context) {
        return new AndroidNetworkService(context);
    }

    @Singleton
    @Provides
    ObjectService provideObjectService(NetworkService networkService) {
        return BurstServiceProviders.getObjectService(networkService);
    }
}
