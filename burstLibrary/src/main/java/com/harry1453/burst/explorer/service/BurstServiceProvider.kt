package com.harry1453.burst.explorer.service

import com.harry1453.burst.explorer.repository.ConfigRepository

class BurstServiceProvider internal constructor(private val objectService: ObjectService, private val configRepository: ConfigRepository) {
    val burstBlockchainService: BurstBlockchainService
        get() = NodeBlockchainService(configRepository, objectService)

    val burstInfoService: BurstInfoService
        get() = RepoInfoService(objectService)

    val burstNetworkService: BurstNetworkService
        get() = PoCCNetworkService(objectService)

    val burstPriceService: BurstPriceService
        get() = CMCPriceService(objectService)
}
