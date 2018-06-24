package com.harry1453.burst.explorer.service;

import com.google.gson.Gson;

import io.reactivex.Single;

public final class GsonDeserializerService implements DeserializerService {

    private final Gson gson = new Gson();

    @Override
    public <T> Single<T> deserializeObject(String objectData, Class<T> objectClass) {
        return Single.fromCallable(() -> gson.fromJson(objectData, objectClass));
    }
}
