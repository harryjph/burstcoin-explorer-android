package com.harry1453.burst.explorer.service

import com.google.gson.Gson

import io.reactivex.Single

class GsonDeserializerService : DeserializerService {

    private val gson = Gson()

    override fun <T> deserializeObject(objectData: String, objectClass: Class<T>): Single<T> {
        return Single.fromCallable { gson.fromJson(objectData, objectClass) }
    }
}
