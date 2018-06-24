package com.harry1453.burst.explorer.service

import com.harry1453.burst.explorer.repository.ConfigRepository

object BurstServiceProviders {
    @JvmStatic
    fun getBurstServiceProvider(objectService: ObjectService, configRepository: ConfigRepository): BurstServiceProvider {
        return BurstServiceProvider(objectService, configRepository)
    }

    @JvmStatic
    fun getObjectService(networkService: NetworkService): ObjectService {
        return NetworkObjectService(networkService, GsonDeserializerService())
    }

    @JvmStatic
    fun getObjectService(networkService: NetworkService, deserializerService: DeserializerService): ObjectService {
        return NetworkObjectService(networkService, deserializerService)
    }
}
