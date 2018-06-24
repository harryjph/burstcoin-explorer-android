package com.harry1453.burst.explorer.service;

import io.reactivex.Single;

public interface NetworkService {
    Single<String> fetchData(String url);
}
