package com.harry1453.burst.explorer.service

import io.reactivex.Single

interface NetworkService {
    fun fetchData(url: String): Single<String>
}
