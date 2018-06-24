package com.harrysoft.burstcoinexplorer.mock;

import android.content.Context;

import com.harrysoft.burstcoinexplorer.burst.service.AndroidNetworkService;
import com.harrysoft.burstcoinexplorer.burst.service.NetworkService;

import io.reactivex.Single;

public class MockNetworkService implements NetworkService {

    private final AndroidNetworkService androidNetworkService;

    public MockNetworkService(Context context) {
        androidNetworkService = new AndroidNetworkService(context);
    }

    @Override
    public Single<String> fetchData(String url) {
        return androidNetworkService.fetchData(url);
    }
}
