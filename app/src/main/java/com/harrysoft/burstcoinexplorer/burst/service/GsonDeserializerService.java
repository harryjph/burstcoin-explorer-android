package com.harrysoft.burstcoinexplorer.burst.service;

import com.google.gson.Gson;
import com.harrysoft.burstcoinexplorer.burst.service.entity.NullResponseException;

import io.reactivex.Single;

public final class GsonDeserializerService implements DeserializerService {

    private final Gson gson = new Gson();

    @Override
    public <T> Single<T> deserializeObject(String objectData, Class<T> objectClass) {
        return Single.create(e -> {
            if (objectData != null) {
                T entity;

                try {
                    entity = gson.fromJson(objectData, objectClass);
                } catch (Exception ex) {
                    e.onError(ex);
                    return;
                }

                e.onSuccess(entity);
            } else {
                e.onError(new NullResponseException());
            }
        });
    }
}
