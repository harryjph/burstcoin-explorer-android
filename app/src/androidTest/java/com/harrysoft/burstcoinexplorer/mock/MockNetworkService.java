package com.harrysoft.burstcoinexplorer.mock;

import android.content.Context;

import com.harry1453.burst.explorer.service.NetworkService;
import com.harrysoft.burstcoinexplorer.main.service.AndroidNetworkService;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;

public class MockNetworkService implements NetworkService {

    private final AndroidNetworkService androidNetworkService;

    public MockNetworkService(Context context) {
        androidNetworkService = new AndroidNetworkService(context);
    }

    @NotNull
    @Override
    public Single<String> fetchData(@NotNull String url) {
        return androidNetworkService.fetchData(url);
    }
}
