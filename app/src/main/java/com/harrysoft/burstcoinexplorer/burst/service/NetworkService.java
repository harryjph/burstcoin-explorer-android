package com.harrysoft.burstcoinexplorer.burst.service;

import io.reactivex.Single;

public interface NetworkService {
    Single<String> fetchData(String url);
}
