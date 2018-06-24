package com.harrysoft.burstcoinexplorer.burst.service;

import io.reactivex.Single;

public interface ObjectService {
    <T> Single<T> fetchObject(String url, Class<T> objectClass);
}
