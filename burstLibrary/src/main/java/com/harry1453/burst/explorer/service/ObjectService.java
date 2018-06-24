package com.harry1453.burst.explorer.service;

import io.reactivex.Single;

public interface ObjectService {
    <T> Single<T> fetchObject(String url, Class<T> objectClass);
}
