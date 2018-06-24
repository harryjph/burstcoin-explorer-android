package com.harry1453.burst.explorer.service;

import io.reactivex.Single;

public interface DeserializerService {
    <T> Single<T> deserializeObject(String objectData, Class<T> objectClass);
}
