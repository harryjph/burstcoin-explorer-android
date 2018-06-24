package com.harry1453.burst.explorer.service

import io.reactivex.Single

interface ObjectService {
    fun <T> fetchObject(url: String, objectClass: Class<T>): Single<T>
}
