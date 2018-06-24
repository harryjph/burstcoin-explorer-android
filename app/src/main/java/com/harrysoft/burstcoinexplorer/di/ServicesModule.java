package com.harrysoft.burstcoinexplorer.di;

import android.content.Context;

import com.harry1453.burst.explorer.service.BurstServiceProviders;
import com.harry1453.burst.explorer.service.NetworkService;
import com.harry1453.burst.explorer.service.ObjectService;
import com.harrysoft.burstcoinexplorer.main.service.AndroidNetworkService;

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
