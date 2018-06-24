package com.harry1453.burst.explorer.entity

import org.jetbrains.annotations.Nullable
import java.math.BigInteger

data class Block (
        @JvmField val transactionCount: BigInteger,
        @JvmField val timestamp: BigInteger,
        @JvmField val blockID: BigInteger,
        @JvmField val total: BurstValue,
        @JvmField val size: BigInteger,
        @JvmField val fee: BurstValue,
        @JvmField val blockReward: BurstValue,
        @JvmField val transactionIDs: List<BigInteger>,
        @JvmField val blockNumber: BigInteger,
        @JvmField val generatorID: BigInteger,
        @Nullable @JvmField var generator: Account?
) {
    fun setGenerator(generator: Account) {
        this.generator = generator
    }
}