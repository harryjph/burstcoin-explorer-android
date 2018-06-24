package com.harrysoft.burstcoinexplorer.burst.service;

import io.reactivex.Single;

public interface DeserializerService {
    <T> Single<T> deserializeObject(String objectData, Class<T> objectClass);
}
